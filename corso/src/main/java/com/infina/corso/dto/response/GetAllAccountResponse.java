package com.infina.corso.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class GetAllAccountResponse {
    private Long id;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long customerId;
}
