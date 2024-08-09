package com.infina.corso.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateAccountRequest {
    private BigDecimal balance;
}
