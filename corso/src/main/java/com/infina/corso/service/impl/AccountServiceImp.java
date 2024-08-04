package com.infina.corso.service.impl;

import com.infina.corso.model.Account;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /*
    @Override
    public Account updateAccount(Long id, Account account) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            Account updatedAccount = existingAccount.get();
            updatedAccount.setAccountNumber(account.getAccountNumber());
            updatedAccount.setIban(account.getIban());
            updatedAccount.setBankName(account.getBankName());
            updatedAccount.setCurrency(account.getCurrency());
            updatedAccount.setBalance(account.getBalance());
            updatedAccount.setDeleted(account.isDeleted());
            updatedAccount.setCustomer(account.getCustomer());
            return accountRepository.save(updatedAccount);
        } else {
            throw new RuntimeException("Account not found with id: " + id);
        }
    }

     */

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found with id: " + id));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }
}
