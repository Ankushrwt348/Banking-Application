package com.Ankush.Bank;

import java.time.Year;

public class AccountUtil {
    public static String ACCOUNT_EXIST_CODE = "001";
    public static String ACCOUNT_EXIST_MESSAGE = "User Already Exists";

    public static String ACCOUNT_CREATION_CODE = "002";
    public static String ACCOUNT_CREATION_MESSAGE = "The Account is Created";

    public static String ACCOUNT_NOT_EXIST_CODE = "003";
    public static String ACCOUNT_NOT_EXIST_MESSAGE = "User with provide AccountNumber does not exists";

    public static String ACCOUNT_FOUND_CODE = "004";
    public static String ACCOUNT_FOUND_MESSAGE = "The Account is Found";

    public static String ACCOUNT_CREDITED_CODE = "005";
    public static String ACCOUNT_CREDITED_MESSAGE = "The Amount is credited";

    public static String INSUFFICIENT_BALANCE_CODE = "006";
    public static String INSUFFICIENT_BALANCE_MESSAGE = "INSUFFICIENT_BALANCE";

    public static String ACCOUNT_DEBITED_CODE = "005";
    public static String ACCOUNT_DEBITED_MESSAGE = "The Amount is Debited";

    public static String generateAccountNumber(){
        Year cyear = Year.now();
        int min=100000;
        int max=999999;
        int randomNumber = (int) (Math.random()*(max-min+1)+min);
        String year = String.valueOf(cyear);
        String randnum = String.valueOf(randomNumber);
        StringBuilder accountNumber = new StringBuilder();
        accountNumber.append(year).append(randnum);
        return accountNumber.toString();
    }
}
