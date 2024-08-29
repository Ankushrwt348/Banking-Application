package com.Ankush.Bank.Service;

import com.Ankush.Bank.dto.BankResponse;
import com.Ankush.Bank.dto.CreditRequest;
import com.Ankush.Bank.dto.UserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.math.BigDecimal;

public interface UserService extends UserDetailsService {
    BankResponse createAccount(UserRequest userRequest);

    BankResponse bankEnquiry(String accNumber);

    String nameEnquiry(String accNumber);

    BankResponse creditAccount(CreditRequest creditRequest);

    BankResponse debitAccount(BigDecimal amount,String accNumber);
}
