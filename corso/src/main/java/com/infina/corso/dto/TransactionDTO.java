package com.infina.corso.dto;

import jakarta.persistence.Column;

public class TransactionDTO {

    private char transactionType;
    private String purchasedCurrency;
    private String soldCurrency;
    private int amount;
    private String transactionDate;
}
