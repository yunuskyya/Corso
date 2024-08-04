package com.infina.corso.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TransactionResponse {

    private int userId;
    private char transactionType;
    private String purchasedCurrency;
    private String soldCurrency;
    private int amount;
    private LocalDateTime transactionDate;
}
