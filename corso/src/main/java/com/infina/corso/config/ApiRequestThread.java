package com.infina.corso.config;

import com.infina.corso.service.CurrencyService;
import lombok.Data;

@Data
public class ApiRequestThread extends Thread {

    private volatile boolean running = true;
    private final CurrencyService currencyService;

    public ApiRequestThread(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    public void run() {
        while (running) {
            try {
                currencyService.getCurrencyRates();
                triggerApiRequest();

                // Sleep for 20 seconds
                Thread.sleep(200000000); //20 saniyeye çekilecek
            } catch (InterruptedException e) {
                // Handle interruption
                running = false;
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopThread() {
        running = false;
    }

    private void triggerApiRequest() {
        // Implement your API request logic here
        System.out.println("API request triggered");
        // Example: Call your service or method that makes the API request
    }
}