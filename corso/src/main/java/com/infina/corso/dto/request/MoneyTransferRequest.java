package com.infina.corso.dto.request;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class MoneyTransferRequest {

    private Long customer_id;

    private String currencyCode;

    private String ibanNo;

    private BigDecimal amount;

    private String receiver;

    private String sender;
}
