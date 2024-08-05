package com.infina.corso.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {
    private String accountNumber;
    private String iban;
    private String bankName;
    private String currency;
    private BigDecimal balance;
}
