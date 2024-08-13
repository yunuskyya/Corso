package com.infina.corso.config;


import com.infina.corso.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
public class ThreadInitializer {

    private final CurrencyService currencyService;

    @Autowired
    public ThreadInitializer(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostConstruct
    public void init() {
        ApiRequestThread apiRequestThread = new ApiRequestThread(currencyService);
        apiRequestThread.start();
    }
}