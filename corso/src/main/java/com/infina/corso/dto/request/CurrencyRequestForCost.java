package com.infina.corso.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyRequestForCost {
    private String purchasedCurrencyCode;
    private String soldCurrencyCode;
    private BigDecimal selectedAccountBalance;
}
