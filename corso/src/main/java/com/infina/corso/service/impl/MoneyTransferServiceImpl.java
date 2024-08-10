package com.infina.corso.service.impl;

import com.infina.corso.dto.request.MoneyTransferRequest;
import com.infina.corso.dto.response.MoneyTransferResponse;
import com.infina.corso.model.*;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.repository.MoneyTransferRepository;
import com.infina.corso.repository.SystemDateRepository;
import com.infina.corso.service.MoneyTransferService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoneyTransferServiceImpl implements MoneyTransferService {

    private final MoneyTransferRepository moneyTransferRepository;
    private final ModelMapper modelMapperForRequest;
    private final ModelMapper modelMapperForResponse;
    private final SystemDateRepository systemDateRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    public MoneyTransferServiceImpl(MoneyTransferRepository moneyTransferRepository, @Qualifier("modelMapperForRequest") ModelMapper modelMapperForRequest, ModelMapper modelMapperForResponse, SystemDateRepository systemDateRepository, CustomerRepository customerRepository, AccountRepository accountRepository) {
        this.moneyTransferRepository = moneyTransferRepository;
        this.modelMapperForRequest = modelMapperForRequest;
        this.modelMapperForResponse = modelMapperForResponse;
        this.systemDateRepository = systemDateRepository;
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void saveMoneyTransfer(MoneyTransferRequest moneyTransfer) {
        try {
            Optional<SystemDate> systemDate = systemDateRepository.findById(1);
            MoneyTransfer transfer = modelMapperForRequest.map(moneyTransfer, MoneyTransfer.class);
            setTransferDirection(moneyTransfer, transfer);
            Optional<Customer> customer = customerRepository.findById(moneyTransfer.getCustomer_id());
            Optional<Account> account = findAccountForTransfer(moneyTransfer, transfer, customerRepository);
            updateAccountBalance(account, moneyTransfer, transfer.getDirection());
            accountRepository.save(account.get());
            customerRepository.save(customer.get());
            transfer.setSystemDate(systemDate.get().getDate());
            moneyTransferRepository.save(transfer);

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }
    }

    public List<MoneyTransferResponse> collectAllMoneyTransfers() {
        List<MoneyTransfer> moneyTransferList = moneyTransferRepository.findAll();
        return convertMoneyTransferListToDto(moneyTransferList);
    }

    private List<MoneyTransferResponse> convertMoneyTransferListToDto(List<MoneyTransfer> moneyTransferList) {
        return moneyTransferList.stream()
                .map(moneyTransfer -> modelMapperForResponse.map(moneyTransfer, MoneyTransferResponse.class))
                .collect(Collectors.toList());
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
        Optional<Customer> customer = customerRepository.findById(moneyTransfer.getCustomer_id());
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
