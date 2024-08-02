package com.infina.corso.service.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infina.corso.dto.response.CurrencyResponse;
import com.infina.corso.model.Currency;
import com.infina.corso.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
public class CurrencyService {

    private static final String API_URL = "https://api.collectapi.com/economy/allCurrency";
    private static final String API_KEY = "apikey 4lsKq5b8POPVmTb8nmK8zT:0GfAzShp6bNHJ93JGlyYKT";

    private  final CurrencyRepository currencyRepository;
    private  final ObjectMapper objectMapper;

    public CurrencyService(ObjectMapper objectMapper, CurrencyRepository currencyRepository) {
        this.objectMapper = objectMapper;
        this.currencyRepository = currencyRepository;
    }

    public CurrencyResponse getCurrencyRates() throws Exception {
        // HTTP Client ve isteği oluşturma
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("content-type", "application/json")
                .header("authorization", "apikey " + API_KEY)
                .build();

        // İsteği gönderme ve yanıtı alma

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        CurrencyResponse currencyResponse = objectMapper.readValue(responseBody, CurrencyResponse.class);
        List<Currency> currencies = currencyResponse.getResult();
        currencyRepository.saveAllAndFlush(currencies);

        for (Currency currency : currencies) {
            System.out.println("Name: "+ currency.getName());
            System.out.println("Code: "+ currency.getCode());
            System.out.println("Selling Price: "+ currency.getSelling());
            System.out.println("Buying Price: "+currency.getBuying());
            System.out.println("/n");
        }

        if (response.statusCode() == 200) {
            // JSON yanıtını `CurrencyResponse` nesnesine dönüştür
            return objectMapper.readValue(response.body(), CurrencyResponse.class);
        } else {
            throw new RuntimeException("API çağrısında bir hata oluştu: " + response.statusCode());
        }
    }

}



