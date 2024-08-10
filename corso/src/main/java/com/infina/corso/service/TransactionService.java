package com.infina.corso.service;

import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.model.Account;
import com.infina.corso.model.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    void transactionSave(TransactionRequest transactionRequest);
    List<TransactionResponse> collectTransactionsForSelectedUser(int id);
    List<TransactionResponse> collectAllTransactions();
}