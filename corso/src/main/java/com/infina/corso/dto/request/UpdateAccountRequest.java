package com.infina.corso.dto.request;

import lombok.Data;

@Data
public class UpdateAccountRequest {
    private String accountNumber;
    private String bankName;
    private String currency;
    private String balance;
}
