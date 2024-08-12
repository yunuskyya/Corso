package com.infina.corso.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MoneyTransferRequestForAddMoney {

    private Long customer_id;

    private String currencyCode;

    private BigDecimal amount;

    private String receiver;

    private String sender;
}
