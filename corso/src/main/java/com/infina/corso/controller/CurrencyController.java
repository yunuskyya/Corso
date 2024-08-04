package com.infina.corso.controller;

import com.infina.corso.dto.response.CurrencyResponse;
import com.infina.corso.service.impl.CurrencyServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/currencies")
@Tag(name = "Currency Management", description = "Operations related to currency management")
public class CurrencyController {

    private final CurrencyServiceImp currencyServiceImp;

    public CurrencyController(CurrencyServiceImp currencyServiceImp) {
        this.currencyServiceImp = currencyServiceImp;
    }
    @GetMapping("get-all")
    @Operation(summary = "Get all currencies", description = "Retrieve a list of all currencies.")
    public CurrencyResponse getCurrencies() throws Exception {
        return currencyServiceImp.getCurrencyRates();
    }
}
