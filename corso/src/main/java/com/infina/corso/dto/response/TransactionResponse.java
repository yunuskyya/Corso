package com.infina.corso.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionResponse {

    private int user_id;
    private char transactionType;
    private String purchasedCurrency;
    private String soldCurrency;
    private int amount;
    private LocalDate transactionSystemDate;
}
