package com.infina.corso.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MoneyTransferResponseForList {

    private String customerNameSurname;

    private double amount;

    private String receiver;

    private String sender;

    private LocalDate systemDate;

    private String direction;

    private String currencyCode;
}
