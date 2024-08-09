package com.infina.corso.service;

import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.model.Account;
import com.infina.corso.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void transactionSave(TransactionRequest transactionRequest);
    Double calculateCurrencyRate(TransactionRequest transactionRequest);
    Double rateCalculate(String soldCurrency, String purchasedCurrency);
    BigDecimal calculateTransactionCostForCross(Account account, double amount, double rate);
    BigDecimal calculateNewBalanceForCross(double amount, double rate);
    BigDecimal calculateTransactionCostForTRY(char transactionType, double amount, String currencyCode);
    BigDecimal calculateNewBalanceForTRY(Account account, double amount, String purchasedCurrency, char transactionType);
    List<TransactionResponse> collectTransactionsForSelectedUser(int id);
    List<TransactionResponse> collectAllTransactions();
    List<TransactionResponse> convertTractionListAsDto(List<Transaction> transactionList);




}