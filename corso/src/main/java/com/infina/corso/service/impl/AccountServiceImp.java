package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.request.UpdateAccountRequest;
import com.infina.corso.dto.response.AccountResponse;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.model.Account;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.service.AccountService;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImp implements AccountService {
    private ModelMapperConfig mapper;
    private AccountRepository accountRepository;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public Account createAccount(CreateAccountRequest createAccountRequest) {
        Account account = mapper.modelMapperForResponse().map(createAccountRequest, Account.class);
        logger.info("Account created: {}", account.getAccountNumber());
        return accountRepository.save(account);

    }

    @Override
    public Account updateAccount(Long id, UpdateAccountRequest updateAccountRequest) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
        mapper.modelMapperForRequest().map(updateAccountRequest, Account.class);
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
        logger.info("Account deleted: {}", id);
    }

    @Override
    public GetAccountByIdResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        return mapper.modelMapperForResponse().map(account, GetAccountByIdResponse.class);
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

}
