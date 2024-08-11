package com.infina.corso.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IbanRegisterRequest {
    String iban;
    String currencyCode;
    String ibanName;
    Long customer_id;
}
