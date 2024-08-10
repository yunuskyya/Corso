package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.request.UpdateAccountRequest;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.dto.response.GetAllAccountResponse;
import com.infina.corso.exception.AccountAlreadyExistsException;
import com.infina.corso.exception.UserNotFoundException;
import com.infina.corso.model.Account;
import com.infina.corso.model.Customer;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.service.AccountService;
import com.infina.corso.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountServiceImp implements AccountService {
    private final CustomerServiceImpl customerServiceImpl;
    private final ModelMapperConfig mapper;
    private final  AccountRepository accountRepository;
    private final  CustomerRepository customerRepository;
    private final AuthService authService;

    private static final Logger logger = LogManager.getLogger(AccountServiceImp.class);

    public AccountServiceImp(ModelMapperConfig mapper, AccountRepository accountRepository, CustomerRepository customerRepository , CustomerServiceImpl customerServiceImpl, AuthService authService) {
        this.mapper = mapper;
        this.accountRepository = accountRepository;
        this.customerRepository = customerRepository;
        this.customerServiceImpl = customerServiceImpl;
        this.authService = authService;
    }


    @Override
    public Account createAccount(CreateAccountRequest createAccountRequest, Long customerId) {
        Account newAccount = mapper.modelMapperForRequest().map(createAccountRequest, Account.class);

        if (accountRepository.findByCurrencyAndCustomerId(createAccountRequest.getCurrency(), customerId) != null) {
            throw new AccountAlreadyExistsException("Account already exists with currency " + createAccountRequest.getCurrency());
        }
        newAccount.setAccountNumber("ACC" + new Random().nextInt(1000000));
        newAccount.setBalance(BigDecimal.valueOf(0.0));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with id " + customerId));
        newAccount.setCustomer(customer);        return accountRepository.save(newAccount);
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
        Account accountInDB = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Account not found with id {}", id);
                    throw new UserNotFoundException("Account not found with id " + id);
                });
        if (accountInDB.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            logger.error("Attempt to delete account with balance: {}", accountInDB.getBalance());
            throw new IllegalStateException("Account with id " + id + " has a balance and cannot be deleted.");
        }
        logger.info("Account deleted: {}", accountInDB.getAccountNumber());
        accountInDB.setDeleted(true);
        accountRepository.save(accountInDB);
    }

    @Override
    public GetAccountByIdResponse getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Account not found with id {}", id);
                    throw new UserNotFoundException("Account not found with id " + id);
                });
        int currentUserId = authService.getCurrentUserId();
        if (account.getCustomer().getUser().getId() != currentUserId) {
            logger.error("Attempt to access unauthorized account: {}", account.getAccountNumber());
            throw new RuntimeException("You are not authorized to see this account");
        }
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
    public AccountRequestTransaction checkIfAccountExists(String accountNumber, String currencyCode) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return customerServiceImpl.checkAccountsForPurchasedCurrency(account, currencyCode);
    }


    @Override
    public List<GetAllAccountResponse> getAccountsByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.stream()
                .map(account -> mapper.modelMapperForResponse().map(account, GetAllAccountResponse.class))
                .collect(Collectors.toList());
    }
    @Override
    public void reactivateAccount(Long id) {
        Account accountInDB = accountRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Account not found with id {}", id);
                    throw new UserNotFoundException("Account not found with id " + id);
                });
        if (!accountInDB.isDeleted()) {
            logger.warn("Attempt to reactivate an already active account: {}", accountInDB.getAccountNumber());
            throw new RuntimeException("Account with id " + id + " is already active.");
        }
        accountInDB.setDeleted(false);
        accountRepository.save(accountInDB);
        logger.info("Account reactivated: {}", accountInDB.getAccountNumber());
    }

}