package com.infina.corso.dto.request;

import lombok.Getter;

@Getter
public class TransactionRequest {

    private String accountNumber;
    private String purchasedCurrency;
    private String soldCurrency;
    private double amount;
    private int user_id;
}
