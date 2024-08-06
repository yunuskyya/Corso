package com.infina.corso.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountRequest {
    private String accountNumber;
    private String bankName;
    private String currency;
    private BigDecimal balance;
}
