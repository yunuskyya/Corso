package com.infina.corso.controller;

import com.infina.corso.dto.response.CurrencyResponse;
import com.infina.corso.service.impl.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/currencies")
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
    @GetMapping("get-all")
    public CurrencyResponse getCurrencies() throws Exception {

        return currencyService.getCurrencyRates();
    }
}
