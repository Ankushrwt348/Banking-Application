package com.Ankush.Bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
    public String responseCode;
    public String responseMessage;
    public AccountInfo accountInfo;
}
