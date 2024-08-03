package com.infina.corso.dto.response;

import java.time.LocalDateTime;

public class TransactionResponse {

    private Long userId;
    private char transactionType;
    private String purchasedCurrency;
    private String soldCurrency;
    private int amount;
    private LocalDateTime transactionDate;
}
