package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.config.UserNotFoundException;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.model.*;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.CurrencyRepository;
import com.infina.corso.repository.TransactionRepository;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.CurrencyService;
import com.infina.corso.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImp implements TransactionService {

    private final ModelMapperConfig modelMapperConfig;
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public TransactionServiceImp(TransactionRepository transactionRepository, ModelMapperConfig modelMapperConfig, com.infina.corso.service.UserService userService, UserService userServiceImpl, CurrencyServiceImp currencyService, CustomerService customerService, AccountRepository accountRepository, CurrencyRepository currencyRepository, UserRepository userRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.userService = userServiceImpl;
        this.currencyService = currencyService;
        this.customerService = customerService;
        this.accountRepository = accountRepository;
        this.currencyRepository = currencyRepository;
        this.userRepository = userRepository;
        this.accountService = accountService;
    }

    @Transactional
    public void transactionSave(TransactionRequest transactionRequest) throws AccountNotFoundException {
        AccountRequestTransaction accountRequestTransaction = accountService.checkIfAccountExists(transactionRequest.getAccountNumber(), transactionRequest.getPurchasedCurrency());
        if (accountRequestTransaction.getAccountNo() != null) {
            Transaction transaction = modelMapperConfig.modelMapperForRequest().map(transactionRequest, Transaction.class);
            Account account = accountRepository.findByAccountNumber(transactionRequest.getAccountNumber());
            account.getTransactions().add(transaction);
            // Çapraz kur işlemi olup olmadığını kontrol et
            boolean isCrossRate = !transactionRequest.getSoldCurrency().equals("TL") && !transactionRequest.getPurchasedCurrency().equals("TL");
            if (isCrossRate) {
                Double rate = calculateCurrencyRate(transactionRequest);
                double transactionAmountInSoldCurrency = transactionRequest.getAmount();
                BigDecimal newBalance = calculateTransactionCostForCross(account, transactionRequest.getAmount(), rate);
                account.setBalance(newBalance);
            } else {
                if (transaction.getSoldCurrency().equals("TL")) {
                    transaction.setTransactionType('A');
                } else transaction.setTransactionType('S');
                BigDecimal newBalance = calculateNewBalanceForTRY(account, transaction.getAmount(), transaction.getPurchasedCurrency(), transaction.getTransactionType());
                account.setBalance(newBalance);
            }
            Account accountPurchasedCurrency = accountRepository.findByAccountNumber(accountRequestTransaction.getAccountNo());
            BigDecimal amountToAdd = BigDecimal.valueOf(transactionRequest.getAmount());
            BigDecimal updatedBalancePurchasedCurrency = accountPurchasedCurrency.getBalance().add(amountToAdd);
            accountPurchasedCurrency.setBalance(updatedBalancePurchasedCurrency);
            User user = userRepository.findById(transactionRequest.getUser_id())
                    .orElseThrow(() -> new UserNotFoundException("User not found with id: " + transactionRequest.getUser_id()));
            transaction.setUser(user);
            transaction.setAccount(account);
            transactionRepository.save(transaction);
            user.getTransactions().add(transaction);
            accountRepository.save(account);
            accountRepository.save(accountPurchasedCurrency);
            userRepository.save(user);
        } else
            throw new AccountNotFoundException("Customer does not have an account in the desired currency. Please open an account first");
    }

    //Parite işlemleri için hesaplar **************************************************
    public Double calculateCurrencyRate(TransactionRequest transactionRequest) {
        String soldCurrency = transactionRequest.getSoldCurrency();
        String purchasedCurrency = transactionRequest.getPurchasedCurrency();
        return rateCalculate(soldCurrency, purchasedCurrency);
    }

    public Double rateCalculate(String soldCurrency, String purchasedCurrency) {
        Currency soldCurrencyEntity = currencyRepository.findByCode(soldCurrency);
        Double a = Double.parseDouble(soldCurrencyEntity.getSelling());
        Currency purchasedCurrencyEntity = currencyRepository.findByCode(purchasedCurrency);
        Double b = Double.parseDouble(purchasedCurrencyEntity.getBuying());
        Double rate = a / b;
        return rate;
    }

    public BigDecimal calculateTransactionCostForCross(Account account, double amount, double rate) {
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateNewBalanceForCross(amount, rate);
        BigDecimal newBalance = balance.subtract(cost);
        return newBalance;
    }

    public BigDecimal calculateNewBalanceForCross(double amount, double rate) {
        BigDecimal amountBigDecimal = BigDecimal.valueOf(amount);
        BigDecimal rateBigDecimal = BigDecimal.valueOf(rate);
        BigDecimal cost = amountBigDecimal.multiply(rateBigDecimal);
        return cost;
    }
    //**************************************************************************

    //TRY ile yapılan işlemler için hesaplar ***********************************
    public BigDecimal calculateTransactionCostForTRY(char transactionType, double amount, String currencyCode) {
        Currency currency = currencyRepository.findByCode(currencyCode);
        double currencyPrice;
        if (transactionType == 'S') {
            currencyPrice = Double.parseDouble(currency.getBuying());
        } else currencyPrice = Double.parseDouble(currency.getSelling());
        BigDecimal cost = new BigDecimal(currencyPrice * amount);
        return cost;
    }

    public BigDecimal calculateNewBalanceForTRY(Account account, double amount, String purchasedCurrency, char transactionType) {
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateTransactionCostForTRY(transactionType, amount, purchasedCurrency);
        BigDecimal newBalance = balance.subtract(cost);
        return newBalance;
    }
    //****************************************************************************

    //UserId ile brokera ait olan tüm müşteilerinin hesaplarındaki işlemleri getiren method
    @Transactional
    public List<TransactionResponse> collectTransactionsForSelectedUser(int id) {
        return convertTractionListAsDto(userService.getAllTransactionsById(id));
    }

    //Adminin veya Yönetici kullanıcısının brokerların yaptığı tüm işlemleri getiren method
    public List<TransactionResponse> collectAllTransactions() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return convertTractionListAsDto(transactionList);
    }

    //Entity listesinin Dto listesine çevrimi
    public List<TransactionResponse> convertTractionListAsDto(List<Transaction> transactionList) {
        return transactionList.stream()
                .map(transaction -> modelMapperConfig.modelMapperForTransaction()
                        .map(transaction, TransactionResponse.class))
                .collect(Collectors.toList());
    }


}
