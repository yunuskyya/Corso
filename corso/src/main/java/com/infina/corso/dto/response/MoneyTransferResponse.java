package com.infina.corso.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MoneyTransferResponse {

    private String ibanNo;

    private double amount;

    private String receiver;

    private String sender;

    private LocalDate systemDate;
}
