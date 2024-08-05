package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.config.UserNotFoundException;
import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.model.*;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CurrencyRepository;
import com.infina.corso.repository.TransactionRepository;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.TransactionService;
import com.infina.corso.service.UserService;
import org.modelmapper.PropertyMap;
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
        BigDecimal newBalance = calculateNewBalance(account,transaction.getAmount() ,transaction.getPurchasedCurrency(),transaction.getTransactionType());
        account.setBalance(newBalance);
        //Transaction'u yapan user bulunur ve transaction user içine yerleştirilir
        User user = userRepository.findById(transactionRequest.getUser_id())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + transactionRequest.getUser_id()));
        transaction.setUser(user);
        transaction.setAccount(account);
        transactionRepository.save(transaction);
        user.getTransactions().add(transaction);
        accountRepository.save(account);
        userRepository.save(user);
    }

    private Double calculateCurrencyRate(TransactionRequest transactionRequest){
        String soldCurrency = transactionRequest.getSoldCurrency();
        String purchasedCurrency = transactionRequest.getPurchasedCurrency();
        Currency soldCurrencyEntity = currencyRepository.findByCode(soldCurrency);
        Currency purchasedCurrencyEntity = currencyRepository.findByCode(purchasedCurrency);

        return null;
    }

    private BigDecimal calculateNewBalance(Account account, int amount, String purchasedCurrency, char transactionType) {
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateTransactionCost(transactionType, amount, purchasedCurrency);
        BigDecimal newBalance = balance.subtract(cost);
        return newBalance;
    }

    //Yapılan transaction işleminin maliyet hesabı
    private BigDecimal calculateTransactionCost(char transactionType, double amount, String currencyCode) {
        Currency currency = currencyRepository.findByCode(currencyCode);
        double currencyPrice;
        if (transactionType == 'S') {
            currencyPrice = Double.parseDouble(currency.getBuying());
        } else currencyPrice = Double.parseDouble(currency.getSelling());
        BigDecimal cost = new BigDecimal(currencyPrice * amount);
        return cost;
    }

    //UserId ile brokera ait olan tüm müşteilerinin hesaplarındaki işlemleri getiren method
    @Transactional
    public List<TransactionResponse> collectTransactionsForSelectedUser(int id) {
        return convertTractionListAsDto(userServiceImpl.getAllTransactionsById(id));
    }

    //Adminin veya Yönetici kullanıcısının brokerların yaptığı tüm işlemleri getiren method
    public List<TransactionResponse> collectAllTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return convertTractionListAsDto(transactionList);
    }

    //Entity listesinin Dto listesine çevrimi
    private List<TransactionResponse> convertTractionListAsDto(List<Transaction> transactionList) {
        return transactionList.stream()
                .map(transaction -> modelMapperConfig.modelMapperForTransaction()
                        .map(transaction, TransactionResponse.class))
                .collect(Collectors.toList());
    }


}
