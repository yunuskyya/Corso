package com.infina.corso.controller;

import com.infina.corso.dto.response.CurrencyResponse;
import com.infina.corso.service.impl.CurrencyServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/currencies")
public class CurrencyController {

    private final CurrencyServiceImp currencyServiceImp;

    public CurrencyController(CurrencyServiceImp currencyServiceImp) {
        this.currencyServiceImp = currencyServiceImp;
    }
    @GetMapping()
    public CurrencyResponse getCurrencies() {
        return currencyServiceImp.getCurrencyRates();
    }

}
