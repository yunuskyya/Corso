package com.infina.corso.service;

import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.request.UpdateAccountRequest;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.dto.response.GetAllAccountResponse;
import com.infina.corso.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(CreateAccountRequest createAccountRequest, Long customerId);
    GetAccountByIdResponse updateAccount(Long customerId, Long accountId, UpdateAccountRequest updateAccountRequest); // Güncelleme metodu
    void deleteAccount(Long id);
    GetAccountByIdResponse getAccountById(Long id);
    List<GetAllAccountResponse> getAllAccounts();
    Account getByAccountNumber(String accountNumber);
    List<GetAllAccountResponse> getAccountsByCustomerId(Long customerId);
    AccountRequestTransaction checkIfAccountExists(String accountNumber, String currencyCode);
    void reactivateAccount(Long id);
}
