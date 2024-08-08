package com.infina.corso.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class CreateAccountRequest {
    private String accountNumber;
    private String bankName;
    private String currency;
    private BigDecimal balance;
    private Long customerId;
}
