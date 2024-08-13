package com.infina.corso.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class GetAllAccountForEndOfDayResponse {
    private LocalDate date;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private String customerNameSurname;
}