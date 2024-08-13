package com.infina.corso.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MoneyTransferRequestForList {
    private Long customerId;
    private LocalDate startDate ;
    private LocalDate endDate ;
    private String currencyCode;
    private Character direction;
}
