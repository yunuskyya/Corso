package com.infina.corso.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountRequest {
    private String customerTcNo;
    private String currencyType;
}
