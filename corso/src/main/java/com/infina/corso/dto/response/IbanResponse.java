package com.infina.corso.dto.response;

import lombok.Data;

@Data
public class IbanResponse {
    private Long id;
    private String iban;
    private String currencyCode;
    private String ibanName;
}
