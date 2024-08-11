package com.infina.corso.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infina.corso.dto.response.CurrencyResponse;
import com.infina.corso.model.Currency;
import com.infina.corso.service.CurrencyService;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;


@Service
public class CurrencyServiceImp implements CurrencyService {

    private static final String API_URL = "https://api.collectapi.com/economy/allCurrency";
    private static final String API_KEY = "apikey 4lsKq5b8POPVmTb8nmK8zT:0GfAzShp6bNHJ93JGlyYKT";

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Currency> currencyRedisTemplate;

    public CurrencyServiceImp(ObjectMapper objectMapper, RedisTemplate<String, Currency> currencyRedisTemplate) {
        this.objectMapper = objectMapper;
        this.currencyRedisTemplate = currencyRedisTemplate;
    }


    /* public Currency findByCode(TransactionRequest transactionRequest) {
        String code = transactionRequest.getPurchasedCurrency();
        return currencyRepository.findByCode(code);
    } */

    public Currency findByCode(String code) {
        ListOperations<String, Currency> listOps = currencyRedisTemplate.opsForList();
        List<Currency> currencyList = listOps.range("currencyList", 0, -1);
        if (currencyList != null) {
            for (Currency currency : currencyList) {
                String currencyCode = currency.getCode();
                if (currencyCode.equals(code)) {
                    return currency;
                }
            }
        }
        return null; // Eğer aranan kod ile eşleşen bir Currency bulunamazsa null döner
    }

    public CurrencyResponse getCurrencyRates() {
        try {
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
            if (response.statusCode() == 200) {
                //Daha önceden rediste bir liste varsa o listeyi siler o şekilde işleme devam edilir
                if (Boolean.TRUE.equals(currencyRedisTemplate.hasKey("currencyList"))) {
                    currencyRedisTemplate.delete("currencyList");
                }
                // JSON yanıtını `CurrencyResponse` nesnesine dönüştür
                CurrencyResponse currencyResponse = objectMapper.readValue(responseBody, CurrencyResponse.class);
                List<Currency> currencies = currencyResponse.getResult();
                ListOperations<String, Currency> listOps = currencyRedisTemplate.opsForList();
                listOps.rightPushAll("currencyList", currencies);
                return currencyResponse;
            } else {
                throw new RuntimeException("API çağrısında bir hata oluştu: " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("Veri alımı sırasında bir hata oluştu", e);
        }
    }



}



