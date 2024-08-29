package com.Ankush.Bank.Service;


import com.Ankush.Bank.AccountUtil;
import com.Ankush.Bank.Entity.User;
import com.Ankush.Bank.Repository.UserRepo;
import com.Ankush.Bank.dto.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    EmailService emailService;

   private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if( userRepo.existsByEmail(userRequest.getEmail())){
           BankResponse bankResponse = BankResponse.builder()
                   .responseCode(AccountUtil.ACCOUNT_EXIST_CODE)
                   .responseMessage(AccountUtil.ACCOUNT_EXIST_MESSAGE)
                   .accountInfo(null)
                   .build();
           return bankResponse;
        }
        User user =User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtil.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternatePhoneNumber(userRequest.getAlternatePhoneNumber())
                .status("ACTIVE")
                .build();
        User savedUser = userRepo.save(user);
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION SUCCESSFULLY")
                .messageBody("Thank you for your account creation!,\nYour Account is Created successfully \n" +
                        "AccountName "+ savedUser.getFirstName()+" "+savedUser.getLastName()+" \n"
                +"Your Account No."+" "+savedUser.getAccountNumber())
                .build();
        emailService.emailAlert(emailDetails);
        return  BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName()+" "+ user.getLastName())
                        .build())
                .build();
    }

    @Override
    public BankResponse bankEnquiry(String accNumber) {
        if(!userRepo.existsByAccountNumber(accNumber)) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User foundUser = userRepo.findByAccountNumber(accNumber);
        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtil.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(foundUser.getFirstName()+" "+ foundUser.getLastName())
                        .accountNumber(foundUser.getAccountNumber())
                        .accountBalance(foundUser.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(String accNumber) {
        if(!userRepo.existsByAccountNumber(accNumber)){
            return AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User foundUser = userRepo.findByAccountNumber(accNumber);
        return foundUser.getFirstName()+" "+foundUser.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditRequest request) {
        if(!userRepo.existsByAccountNumber(request.getAccountNumber())) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepo.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepo.save(userToCredit);
        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREDITED_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()+" "+ userToCredit.getLastName())
                        .accountNumber(userToCredit.getAccountNumber())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public BankResponse debitAccount(BigDecimal amount, String accNumber) {
        User userToDebit = userRepo.findByAccountNumber(accNumber);
        BigInteger accountBalance =userToDebit.getAccountBalance().toBigInteger();
        if(!userRepo.existsByAccountNumber(accNumber)) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else if (accountBalance.intValue() < amount.intValue()) {
            return BankResponse.builder()
                    .responseCode(AccountUtil.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtil.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName()+" "+ userToDebit.getLastName())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }else{
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(amount));
            userRepo.save(userToDebit);
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_DEBITED_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName()+" "+ userToDebit.getLastName())
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
        }

    }

    @Override
    public UserDetails loadUserByUsername(String accountNumber) throws UsernameNotFoundException {
       User user = userRepo.findByAccountNumber(accountNumber);
        if(user != null){
            return org.springframework.security.core.userdetails.User.builder()
                    .username(accountNumber)
                    .password(user.getPassword())
                    .build();
        }
        throw new UsernameNotFoundException(accountNumber);
    }
}
