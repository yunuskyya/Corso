package com.infina.corso.service;

import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.model.Account;
import com.infina.corso.model.enums.CustomerType;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    Account updateAccount(Long id, Account account); // Güncelleme metodu
    void deleteAccount(Long id);
    Account getAccountById(Long id);
    List<Account> getAllAccounts();
    Account getAccountByAccountNumber(String accountNumber);
    List<Account> getAccountsByCustomerId(Long customerId);
    /*List<Account> getAccountsByCurrencyAndCustomerType(String currency, CustomerType customerType); */
    List<Account> getAccountsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    AccountRequestTransaction checkIfAccountExists(String accountNumber, String currencyCode);
}
