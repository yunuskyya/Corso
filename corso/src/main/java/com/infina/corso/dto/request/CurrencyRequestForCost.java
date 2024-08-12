package com.infina.corso.dto.request;

import lombok.Data;

@Data
public class CurrencyRequestForCost {
    private String purchasedCurrencyCode;
    private String soldCurrencyCode;
    private double amount;
}
