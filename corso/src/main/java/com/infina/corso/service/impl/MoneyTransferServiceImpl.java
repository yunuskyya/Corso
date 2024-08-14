package com.infina.corso.service.impl;

import com.infina.corso.dto.request.MoneyTransferRequestForAddMoney;
import com.infina.corso.dto.request.MoneyTransferRequestForList;
import com.infina.corso.dto.response.MoneyTransferResponseForList;
import com.infina.corso.exception.CustomerNotFoundException;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    public void saveMoneyTransfer(MoneyTransferRequestForAddMoney moneyTransfer) {
        try {
            Optional<SystemDate> systemDate = systemDateRepository.findById(1);
            MoneyTransfer transfer = modelMapperForRequest.map(moneyTransfer, MoneyTransfer.class);
            setTransferDirection(moneyTransfer, transfer);
            transfer.setCurrencyCode(moneyTransfer.getCurrencyCode());
            Optional<Customer> customer = customerRepository.findById(moneyTransfer.getCustomer_id());
            Optional<Account> account = findAccountForTransfer(moneyTransfer, transfer, customerRepository);
            updateAccountBalance(account, moneyTransfer, transfer.getDirection());
            if (transfer.getDirection() == 'G') {
                transfer.setReceiver(account.get().getAccountNumber());
            } else transfer.setSender(account.get().getAccountNumber());
            accountRepository.save(account.get());
            customerRepository.save(customer.get());
            transfer.setSystemDate(systemDate.get().getDate());
            moneyTransferRepository.save(transfer);

        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e.getMessage());
        }
    }

    public List<MoneyTransferResponseForList> filterMoneyTransfers(MoneyTransferRequestForList moneyTransferRequestForList) {
        Long customerId = moneyTransferRequestForList.getCustomerId();
        LocalDate startDate = moneyTransferRequestForList.getStartDate();
        LocalDate endDate = moneyTransferRequestForList.getEndDate();
        String currencyCode = moneyTransferRequestForList.getCurrencyCode();
        Character direction = moneyTransferRequestForList.getDirection();
        // 1. Tüm transferleri al
        List<MoneyTransfer> moneyTransferList = moneyTransferRepository.findAll();
        List<MoneyTransfer> filteredList = new ArrayList<>();

        if (customerId != null) {
            filteredList = moneyTransferList.stream()
                    .filter(moneyTransfer -> moneyTransfer.getCustomer_id().equals(customerId))
                    .collect(Collectors.toList());
        } else {
            filteredList = moneyTransferList; // Eğer customerId null ise tüm listeyi döndür
        }

        // 2. Stream başlat ve filtreleme işlemlerini uygula
        return moneyTransferList.stream()
                // 3. Tarih aralığına göre filtreleme
                .filter(transfer -> (startDate == null || !transfer.getSystemDate().isBefore(startDate)) &&
                        (endDate == null || !transfer.getSystemDate().isAfter(endDate)))
                // 4. Döviz türüne göre filtreleme
                .filter(transfer -> (currencyCode == null || transfer.getCurrencyCode().equals(currencyCode)))
                // 5. İşlem yönüne göre filtreleme
                .filter(transfer -> (direction == null || transfer.getDirection() == direction))
                // 6. DTO'ya dönüştür ve listeye topla
                .map(transfer -> {
                    MoneyTransferResponseForList response = modelMapperForResponse.map(transfer, MoneyTransferResponseForList.class);
                    response.setCustomerNameSurname(customerId != null ? getCustomerNameSurname(customerId) : "");
                    return response;
                })
                .collect(Collectors.toList());
    }

    private String getCustomerNameSurname(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> customer.getName() + " " + customer.getSurname())
                .orElse(""); // Eğer müşteri bulunamazsa boş döndür
    }


    public List<MoneyTransferResponseForList> collectAllMoneyTransfers() {
        List<MoneyTransfer> moneyTransferList = moneyTransferRepository.findAll();
        return convertMoneyTransferListToDto(moneyTransferList);
    }

    public List<MoneyTransferResponseForList> collectMoneyTransfersForEndOfDay() {
        LocalDate date = systemDateRepository.findById(1).get().getDate();
        List<MoneyTransfer> moneyTransferList = moneyTransferRepository.findBySystemDate(date);
        return convertMoneyTransferListToDto(moneyTransferList);
    }

    private List<MoneyTransferResponseForList> convertMoneyTransferListToDto(List<MoneyTransfer> moneyTransferList) {
        return moneyTransferList.stream()
                .map(moneyTransfer -> {
                    MoneyTransferResponseForList response = modelMapperForResponse
                            .map(moneyTransfer, MoneyTransferResponseForList.class);
                    Customer customer = customerRepository.findById(moneyTransfer.getCustomer_id())
                            .orElseThrow(() -> new CustomerNotFoundException(moneyTransfer.getCustomer_id())); // Özel hata sınıfını kullanıyoruz
                    String customerNameSurname = customer.getName() + " " + customer.getSurname();
                    response.setCustomerNameSurname(customerNameSurname);
                    return response;
                })
                .collect(Collectors.toList());
    }

    private MoneyTransfer setTransferDirection(MoneyTransferRequestForAddMoney moneyTransfer, MoneyTransfer transfer) {
        if (moneyTransfer.getReceiver() == null) {
            transfer.setDirection('G');
        } else {
            transfer.setDirection('Ç');
        }
        return transfer;
    }

    private Optional<Account> findAccountForTransfer(MoneyTransferRequestForAddMoney moneyTransfer, MoneyTransfer
            transfer, CustomerRepository customerRepository) {
        Optional<Customer> customer = customerRepository.findById(moneyTransfer.getCustomer_id());
        return customer.get().getAccounts().stream()
                .filter(acc -> acc.getCurrency().equals(moneyTransfer.getCurrencyCode()))
                .findFirst();
    }

    private void updateAccountBalance(Optional<Account> account, MoneyTransferRequestForAddMoney moneyTransfer,
                                      char direction) {
        BigDecimal amount = moneyTransfer.getAmount();
        if (direction == 'G') {
            account.get().setBalance(account.get().getBalance().add(amount));
        } else {
            account.get().setBalance(account.get().getBalance().subtract(amount));
        }
    }

}
