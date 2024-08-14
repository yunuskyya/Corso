package com.infina.corso.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TransactionResponse {
    private String name;
    private String surname;
    private int user_id;
    private String purchasedCurrency;
    private String soldCurrency;
    private int amount;
    private LocalDate transactionSystemDate;
    private Double rate;
    private Double cost;
}
