package com.infina.corso.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private String customerTcNo;
    private String currencyType;
}
