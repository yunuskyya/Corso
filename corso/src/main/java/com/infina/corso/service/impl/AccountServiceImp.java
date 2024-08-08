package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.request.UpdateAccountRequest;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.dto.response.GetAllAccountResponse;
import com.infina.corso.model.Account;
import com.infina.corso.model.Customer;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.service.AccountService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {
    private ModelMapperConfig mapper;
    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;
    private CustomerServiceImpl customerServiceImpl;
    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    public AccountServiceImp(ModelMapperConfig mapper, AccountRepository accountRepository, CustomerRepository customerRepository, CustomerServiceImpl customerServiceImpl) {
        this.mapper = mapper;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.customerServiceImpl = customerServiceImpl;
    }


    @Override
    public Account createAccount(CreateAccountRequest createAccountRequest) {
        Account account = mapper.modelMapperForResponse().map(createAccountRequest, Account.class);
        logger.info("Account created: {}", account.getAccountNumber());
        return accountRepository.save(account);

    }

    @Override
    public GetAccountByIdResponse updateAccount(Long customerId, Long accountId, UpdateAccountRequest updateAccountRequest) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));

        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id " + accountId));

        if (!existingAccount.getCustomer().getId().equals(customerId)) {
            throw new RuntimeException("Account does not belong to the specified customer");
        }

        mapper.modelMapperForRequest().map(updateAccountRequest, existingAccount);
        Account updatedAccount = accountRepository.save(existingAccount);
        return mapper.modelMapperForResponse().map(updatedAccount, GetAccountByIdResponse.class);
    }

    @Override
    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    public GetAccountByIdResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        return mapper.modelMapperForResponse().map(account, GetAccountByIdResponse.class);
    }

    @Override
    public List<GetAllAccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> mapper.modelMapperForResponse().map(account, GetAllAccountResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public Account getByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }


    @Override
    public List<GetAllAccountResponse> getAccountsByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.stream()
                .map(account -> mapper.modelMapperForResponse().map(account, GetAllAccountResponse.class))
                .collect(Collectors.toList());
    }
}

