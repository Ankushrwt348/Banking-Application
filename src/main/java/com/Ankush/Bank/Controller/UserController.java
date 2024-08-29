package com.Ankush.Bank.Controller;

import com.Ankush.Bank.Service.UserService;
import com.Ankush.Bank.dto.BankResponse;
import com.Ankush.Bank.dto.CreditRequest;
import com.Ankush.Bank.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/create-user")
    public BankResponse createUser(@RequestBody UserRequest user) {
       return userService.createAccount(user);
    }

    @GetMapping("/balanceEnquiry")
    public BankResponse getBalanceEnquiry() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accNumber = authentication.getName();
        return userService.bankEnquiry(accNumber);
    }

    @GetMapping("/nameEnquiry")
    public String getName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accNumber = authentication.getName();
        return  userService.nameEnquiry(accNumber);
    }


    @PostMapping("/credit")
    public BankResponse creditUser(@RequestBody CreditRequest request) {
        return userService.creditAccount(request);
    }



    @PostMapping("/debit")
    public BankResponse debitUser(@RequestBody BigDecimal amount) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountNumber = authentication.getName();
        return userService.debitAccount(amount, accountNumber);
    }

}
