package com.infina.corso.service;

import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.request.UpdateAccountRequest;
import com.infina.corso.dto.response.AccountResponse;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.model.Account;
import com.infina.corso.model.enums.CustomerType;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    Account createAccount(CreateAccountRequest createAccountRequest);
    Account updateAccount(Long id, UpdateAccountRequest updateAccountRequest); // Güncelleme metodu
    void deleteAccount(Long id);
    GetAccountByIdResponse getAccountById(Long id);
    List<Account> getAllAccounts();
    Account getAccountByAccountNumber(String accountNumber);
    List<Account> getAccountsByCustomerId(Long customerId);
    /*List<Account> getAccountsByCurrencyAndCustomerType(String currency, CustomerType customerType); */
    List<Account> getAccountsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
