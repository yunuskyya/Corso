package com.infina.corso.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {
    //private String accountNumber;
    private String currency;
    private BigDecimal balance;

    public AccountResponse(String usd, BigDecimal bigDecimal) {
        this.currency = usd;
        this.balance = bigDecimal;
    }
}
