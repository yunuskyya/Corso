package com.infina.corso.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String currency;
    private BigDecimal balance;
    private boolean isActive;
    private boolean isDeleted;
    private LocalDateTime createdAt;
}
