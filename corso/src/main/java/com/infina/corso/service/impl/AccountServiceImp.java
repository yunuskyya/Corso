package com.infina.corso.service.impl;

import com.infina.corso.model.Account;
import com.infina.corso.model.enums.CustomerType;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImp implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long id, Account account) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setIban(account.getIban());
        existingAccount.setBankName(account.getBankName());
        existingAccount.setCurrency(account.getCurrency());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setStatus(account.getStatus());
        return accountRepository.save(existingAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

   /*@Override
    public List<Account> getAccountsByCurrencyAndCustomerType(String currency, CustomerType customerType) {
        return accountRepository.findByCurrencyAndCustomerType(currency, customerType);
    }*/

    @Override
    public List<Account> getAccountsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {

        return accountRepository.findByCreatedAtBetween(startDate, endDate);
    }
}
