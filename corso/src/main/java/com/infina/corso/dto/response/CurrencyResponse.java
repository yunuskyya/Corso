package com.infina.corso.dto.response;

import com.infina.corso.model.Currency;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CurrencyResponse {
    private boolean success;
    private List<Currency> result;
}
