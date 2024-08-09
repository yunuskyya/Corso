package com.infina.corso.dto.response;

import lombok.Data;

@Data
public class GetAccountByIdResponse {
    private String accountNumber;
    private String currency;
    private String balance;
    private boolean isActive;
    private boolean isDeleted;
}
