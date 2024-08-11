package com.infina.corso.service;

import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.CurrencyResponse;
import com.infina.corso.model.Currency;

public interface CurrencyService {
   // Currency findByCode(TransactionRequest transactionRequest) ;
    Currency findByCode(String code) ;
    CurrencyResponse getCurrencyRates() ;
}
