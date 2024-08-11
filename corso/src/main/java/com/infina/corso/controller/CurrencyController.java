package com.infina.corso.controller;

import com.infina.corso.dto.request.CurrencyRequestForCost;
import com.infina.corso.dto.response.CurrencyResponse;
import com.infina.corso.dto.response.CurrencyResponseForCost;
import com.infina.corso.service.impl.CurrencyServiceImp;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/cost")
    public CurrencyResponseForCost getCurrencyForCost(@RequestBody CurrencyRequestForCost currency) {
        return currencyServiceImp.calculateCostCrossRate(currency);
    }

}
