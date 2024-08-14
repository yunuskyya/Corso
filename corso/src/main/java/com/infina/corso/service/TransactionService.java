package com.infina.corso.service;

import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void transactionSave(TransactionRequest transactionRequest);
    List<TransactionResponse> collectTransactionsForSelectedUser(int id);
    List<TransactionResponse> collectAllTransactions();
    Double calculateCurrencyRate(String a, String b);
    BigDecimal calculateNewBalanceForCross(double amount, double rate);
    List<TransactionResponse> collectAllTransactionForDayClose ();
}