package com.infina.corso.service.impl;

import com.infina.corso.dto.request.MoneyTransferRequest;
import com.infina.corso.model.Account;
import com.infina.corso.model.Customer;
import com.infina.corso.model.MoneyTransfer;
import com.infina.corso.model.SystemDate;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.repository.MoneyTransferRepository;
import com.infina.corso.repository.SystemDateRepository;
import com.infina.corso.service.MoneyTransferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.support.CustomSQLExceptionTranslatorRegistrar;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.Optional;

@Service
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final MoneyTransferRepository transferRepository;
    private final ModelMapper modelMapperForRequest;
    private final SystemDateRepository systemDateRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public MoneyTransferServiceImpl(MoneyTransferRepository transferRepository, @Qualifier("modelMapperForRequest") ModelMapper modelMapperForRequest, SystemDateRepository systemDateRepository, CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.transferRepository = transferRepository;
        this.modelMapperForRequest = modelMapperForRequest;
        this.systemDateRepository = systemDateRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }


    public void saveMoneyTransfer(MoneyTransferRequest moneyTransfer) {
        try {
            Optional<SystemDate> systemDate = systemDateRepository.findById(1);
            MoneyTransfer transfer = modelMapperForRequest.map(moneyTransfer, MoneyTransfer.class);
            setTransferDirection(moneyTransfer, transfer);
            Optional<Customer> customer = customerRepository.findById(moneyTransfer.getCustomerId());
            Optional<Account> account = findAccountForTransfer(moneyTransfer, transfer, customerRepository);
            updateAccountBalance(account, moneyTransfer, transfer.getDirection());
            accountRepository.save(account.get());
            transfer.setSystemDate(systemDate.get().getDate());
            transferRepository.save(transfer);
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }
    }

    private MoneyTransfer setTransferDirection(MoneyTransferRequest moneyTransfer, MoneyTransfer transfer) {
        if (moneyTransfer.getReceiver() != null && moneyTransfer.getReceiver().startsWith("TR")) {
            transfer.setDirection('Ç');
        } else {
            transfer.setDirection('G');
        }
        return transfer;
    }

    private Optional<Account> findAccountForTransfer(MoneyTransferRequest moneyTransfer, MoneyTransfer
            transfer, CustomerRepository customerRepository) {
        Optional<Customer> customer = customerRepository.findById(moneyTransfer.getCustomerId());
        return customer.get().getAccounts().stream()
                .filter(acc -> acc.getCurrency().equals(moneyTransfer.getCurrencyCode()))
                .findFirst();
    }

    private void updateAccountBalance(Optional<Account> account, MoneyTransferRequest moneyTransfer,
                                      char direction) {
        BigDecimal amount = moneyTransfer.getAmount();
        if (direction == 'G') {
            account.get().setBalance(account.get().getBalance().add(amount));
        } else {
            account.get().setBalance(account.get().getBalance().subtract(amount));
        }
    }

}
