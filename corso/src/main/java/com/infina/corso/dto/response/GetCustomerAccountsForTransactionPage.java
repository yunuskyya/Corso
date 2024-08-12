package com.infina.corso.dto.response;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetCustomerAccountsForTransactionPage {
    private Long id;
    private String currency;
    private BigDecimal balance;
}
