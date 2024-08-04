package com.infina.corso.service;

import com.infina.corso.model.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    //Account updateAccount(Long id, Account account);
    void deleteAccount(Long id);
    Account getAccountById(Long id);
    List<Account> getAllAccounts();
    Account getAccountByAccountNumber(String accountNumber);
}