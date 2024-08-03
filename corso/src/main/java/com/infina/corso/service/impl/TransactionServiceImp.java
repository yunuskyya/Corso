package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.model.Account;
import com.infina.corso.model.Currency;
import com.infina.corso.model.Transaction;
import com.infina.corso.model.User;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CurrencyRepository;
import com.infina.corso.repository.TransactionRepository;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.TransactionService;
import com.infina.corso.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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




    public void transactionSave(TransactionRequest transactionRequest) {
        Transaction transaction = modelMapperConfig.modelMapperForRequest().map(transactionRequest, Transaction.class);
        //Transaction'un türü belirlenip set edilir.
        if(transaction.getSoldCurrency().equals("TL")){
            transaction.setTransactionType('A');
        }else transaction.setTransactionType('S');
        //Gelen request içindeki account no ile ilgli Account sınıfı bulunur
        Account account = accountRepository.findByAccountNumber(transactionRequest.getAccountNumber());
        //Yapılan transaction ilgili account sınıfı içindeki Transaction listesine eklenir
        account.getTransactions().add(transaction);
        //İlgili account içindeki bakiye değişikliği yapılır
        BigDecimal balance = account.getBalance();
        BigDecimal cost =  calculateTransactionCost(transaction.getTransactionType(), account.getAccountNumber(), transaction.getAmount(), transaction.getPurchasedCurrency());
        BigDecimal newBalance = balance.subtract(cost);
        account.setBalance(newBalance);
        //account bakiyesi güncellendikten sonra veritabanına kaydedilir
        accountRepository.save(account);
        //Transaction veritabanına kaydedilir
        transactionRepository.save(transaction);

    }

    //Yapılan transaction işleminin maliyet hesabı
    public BigDecimal calculateTransactionCost(char transactionType, String accountNumber, double amount, String currencyCode) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        Currency currency = currencyRepository.findByCode(currencyCode);
        double currencyPrice;
        if(transactionType == 'S'){
            currencyPrice = Double.parseDouble(currency.getBuying());
        }else currencyPrice = Double.parseDouble(currency.getSelling());
        BigDecimal cost = new BigDecimal(currencyPrice*amount);
        return cost;
    }

    //UserId ile brokera ait olan tüm müşteilerinin hesaplarındaki işlemleri getiren method
    public List<TransactionResponse> collectTransactionsForSelectedUser (int id){
        //işlem yapan kullanıcının yaptığı işlemleri görebilmek için user üzerinden customerlarına
        //ve customerlar içinden transactionlar listesini çekmemiz gerekiyor ??
        // List<Transaction> transactionList = transactionRepository.findByUserId(id);
        //return convertTractionListAsDto(transactionList);
        return null;
    }

    //Adminin veya Yönetici kullanıcısının brokerların yaptığı tüm işlemleri getiren method
    public List<TransactionResponse> collectAllTransactions(){
        List<Transaction> transactionList = transactionRepository.findAll();
        return convertTractionListAsDto(transactionList);
    }

    //Converting Entity to Dto as a List
    public List<TransactionResponse> convertTractionListAsDto(List<Transaction> transactionList) {
        List<TransactionResponse> transactionResponseList = new ArrayList<>();

        for (int i = 0; i < transactionList.size(); i++) {
            TransactionResponse transactionResponse = modelMapperConfig.modelMapperForResponse().map(transactionList.get(i), TransactionResponse.class);
            transactionResponseList.add(transactionResponse);
        }

        return transactionResponseList;
    }





}
