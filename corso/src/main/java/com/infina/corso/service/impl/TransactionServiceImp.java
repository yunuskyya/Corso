package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.model.*;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CurrencyRepository;
import com.infina.corso.repository.TransactionRepository;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.TransactionService;
import com.infina.corso.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImp implements TransactionService {

    private final CustomerServiceImpl customerServiceImpl;
    private final CurrencyRepository currencyRepository;
    private final UserRepository userRepository;

    public TransactionServiceImp(TransactionRepository transactionRepository, ModelMapperConfig modelMapperConfig, UserService userService, UserServiceImpl userServiceImpl, CurrencyServiceImp currencyServiceImp, CustomerServiceImpl customerServiceImpl, AccountRepository accountRepository, CurrencyRepository currencyRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.userServiceImpl = userServiceImpl;
        this.currencyServiceImp = currencyServiceImp;
        this.customerServiceImpl = customerServiceImpl;
        this.accountRepository = accountRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
    }

    private final ModelMapperConfig modelMapperConfig;

    private final TransactionRepository transactionRepository;

    private final UserServiceImpl userServiceImpl;

    private final CurrencyServiceImp currencyServiceImp;

    private final AccountRepository accountRepository; //AccountService sınıfı oluşturulduğunda oradan çekilecek


    @Transactional
    public void transactionSave(TransactionRequest transactionRequest) {
        Transaction transaction = modelMapperConfig.modelMapperForRequest().map(transactionRequest, Transaction.class);
        //Transaction'un türü belirlenip set edilir.
        if (transaction.getSoldCurrency().equals("TL")) {
            transaction.setTransactionType('A');
        } else transaction.setTransactionType('S');
        //Gelen request içindeki account no ile ilgli Account sınıfı bulunur
        Account account = accountRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        //Yapılan transaction ilgili account sınıfı içindeki Transaction listesine eklenir
        account.getTransactions().add(transaction);
        //İlgili account içindeki bakiye değişikliği yapılır
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateTransactionCost(transaction.getTransactionType(), transaction.getAmount(), transaction.getPurchasedCurrency());
        BigDecimal newBalance = balance.subtract(cost);
        account.setBalance(newBalance);
        //account bakiyesi güncellendikten sonra veritabanına kaydedilir
        accountRepository.save(account);
        transaction.setAccount(account);
        //Transaction veritabanına kaydedilir
        System.out.println(transaction.toString());
        transactionRepository.save(transaction);

    }


    //Yapılan transaction işleminin maliyet hesabı
    public BigDecimal calculateTransactionCost(char transactionType, double amount, String currencyCode) {
        Currency currency = currencyRepository.findByCode(currencyCode);
        double currencyPrice;
        if (transactionType == 'S') {
            currencyPrice = Double.parseDouble(currency.getBuying());
        } else currencyPrice = Double.parseDouble(currency.getSelling());
        BigDecimal cost = new BigDecimal(currencyPrice * amount);
        return cost;
    }

    //UserId ile brokera ait olan tüm müşteilerinin hesaplarındaki işlemleri getiren method
    public List<TransactionResponse> collectTransactionsForSelectedUser(int id) {
        //Gelen id ile ilgili kullanıcı veritabanından bulunur --> bulunnan kullanıcının Customer listesi alınır -->
        //Customer listesinin içindeki Account listeleri alınır --> Account listeleri içindeki Transaction Listeleri bir listeye toplanır
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found for id: " + id));
        List<Customer> customerList = user.getCustomerList();
        // Customer listesinin içindeki Account listeleri alınır ve Transaction listeleri bir listeye toplanır
        List<Transaction> transactionList = customerList.stream()
                .flatMap(customer -> customer.getAccounts().stream())
                .flatMap(account -> account.getTransactions().stream())
                .collect(Collectors.toList());

        for (Transaction transaction : transactionList) {
            System.out.println(transaction.toString());
        }

        return convertTractionListAsDto(transactionList, id);
    }


    /* //Gelen id ile ilgili kullanıcı veritabanından bulunur --> bulunnan kullanıcının Customer listesi alınır--
        //--> Customer listesinin içindeki Account listeleri alınır --> Account listeleri içindeki Transaction Listeleri bir listeye toplanır
        Optional<User> user = userRepository.findById(id);
        List<Customer> customerList = user.get().getCustomerList();
        List<Account> accountList = new ArrayList<>();
        customerList.forEach(customer -> customer.getAccounts().addAll(accountList));
        List<Transaction> transactionList = new ArrayList<>();
        accountList.forEach(account -> account.getTransactions().addAll(transactionList));
        return convertTractionListAsDto(transactionList); */


    //Adminin veya Yönetici kullanıcısının brokerların yaptığı tüm işlemleri getiren method
    public List<TransactionResponse> collectAllTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return convertTractionListAsDto(transactionList);
    }

    //Entity listesinin Dto listesine çevrimi ** User ID'ye göre **
    public List<TransactionResponse> convertTractionListAsDto(List<Transaction> transactionList, int userId) {
        return transactionList.stream()
                .map(transaction -> {
                    TransactionResponse response = modelMapperConfig.modelMapperForResponse().map(transaction, TransactionResponse.class);
                    response.setUserId((int) userId);  // userId'yi DTO'ya set ediyoruz
                    return response;
                })
                .collect(Collectors.toList());
    }

    //Entity listesinin Dto listesine çevrimi
    public List<TransactionResponse> convertTractionListAsDto(List<Transaction> transactionList) {
        return transactionList.stream()
                .map(transaction -> modelMapperConfig.modelMapperForResponse()
                        .map(transaction, TransactionResponse.class))
                .collect(Collectors.toList());
    }


}
