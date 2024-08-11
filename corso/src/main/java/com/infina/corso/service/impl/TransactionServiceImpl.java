package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.exception.UserNotFoundException;
import com.infina.corso.model.*;
import com.infina.corso.repository.AccountRepository;
import com.infina.corso.repository.TransactionRepository;
import com.infina.corso.repository.UserRepository;
import com.infina.corso.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final ModelMapperConfig modelMapperConfig;
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final CurrencyService currencyService;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final UserServiceImpl userServiceImpl;
    private final SystemDateService systemDateService;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ModelMapperConfig modelMapperConfig, UserServiceImpl userServiceImpl, CurrencyServiceImp currencyService, CustomerService customerService, AccountRepository accountRepository, UserRepository userRepository, AccountService accountService, SystemDateService systemDateService) {
        this.transactionRepository = transactionRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.userService = userServiceImpl;
        this.currencyService = currencyService;
        this.customerService = customerService;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.userServiceImpl = userServiceImpl;
        this.systemDateService = systemDateService;
    }

    @Transactional
    public void transactionSave(TransactionRequest transactionRequest) {
        if(!systemDateService.isDayClosedStarted()) {
            try {
                LocalDate systemDate = systemDateService.getSystemDate();
                AccountRequestTransaction accountRequestTransaction = accountService.checkIfAccountExists(transactionRequest.getAccountNumber(), transactionRequest.getPurchasedCurrency());
                if (accountRequestTransaction.getAccountNo() != null) {
                    Transaction transaction = modelMapperConfig.modelMapperForRequest().map(transactionRequest, Transaction.class);
                    transaction.setSystemDate(systemDate);
                    Account account = accountRepository.findByAccountNumber(transactionRequest.getAccountNumber());
                    account.getTransactions().add(transaction);
                    // Çapraz kur işlemi olup olmadığını kontrol et
                    boolean isCrossRate = !transactionRequest.getSoldCurrency().equals("TL") && !transactionRequest.getPurchasedCurrency().equals("TL");
                    if (isCrossRate) {
                        Double rate = calculateCurrencyRate(transactionRequest);
                        double transactionAmountInSoldCurrency = transactionRequest.getAmount();
                        BigDecimal newBalance = calculateTransactionCostForCross(account, transactionRequest.getAmount(), rate);
                        //hesap bakiye yeterlilik kontrolü
                        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                            throw new RuntimeException("Insufficient funds for account number:  " + account.getAccountNumber());
                        }
                        //maliyeti transaction içine set edilmesi
                 /* BigDecimal costBigDecimal = calculateNewBalanceForCross(transaction.getAmount(), rate);
                    double cost = costBigDecimal.doubleValue();
                    transaction.setCost(cost); */
                        account.setBalance(newBalance);
                    } else {
                        if (transaction.getSoldCurrency().equals("TL")) {
                            transaction.setTransactionType('A');
                        } else transaction.setTransactionType('S');
                        BigDecimal newBalance = calculateNewBalanceForTRY(account, transaction.getAmount(), transaction.getPurchasedCurrency(), transaction.getTransactionType());
                        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                            throw new RuntimeException("Insufficient funds for account number: " + account.getAccountNumber());
                        }
                        //maliyeti transaction içine set edilmesi
                 /* BigDecimal costDecimal = calculateTransactionCostForTRY(transaction.getTransactionType(), transactionRequest.getAmount(), transactionRequest.getPurchasedCurrency());
                    double cost = costDecimal.doubleValue();
                    transaction.setCost(cost); */
                        account.setBalance(newBalance);
                    }
                    //satın alınan döviz türündeki hesabın bakiyesinin güncellenmesi
                    Account accountPurchasedCurrency = accountRepository.findByAccountNumber(accountRequestTransaction.getAccountNo());
                    BigDecimal amountToAdd = BigDecimal.valueOf(transactionRequest.getAmount());
                    BigDecimal updatedBalancePurchasedCurrency = accountPurchasedCurrency.getBalance().add(amountToAdd);
                    accountPurchasedCurrency.setBalance(updatedBalancePurchasedCurrency);
                    //
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
            } catch (AccountNotFoundException e) {
                System.out.println("Account not found: " + e.getMessage());
            } catch (UserNotFoundException e) {
                System.out.println("User not found: " + e.getMessage());
            } catch (RuntimeException e) {
                System.out.println("Runtime exception: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected exception: " + e.getMessage());
            }
        }
    }

    //Parite işlemleri için hesaplar **************************************************
    private Double calculateCurrencyRate(TransactionRequest transactionRequest) {
        String soldCurrency = transactionRequest.getSoldCurrency();
        String purchasedCurrency = transactionRequest.getPurchasedCurrency();
        return rateCalculate(soldCurrency, purchasedCurrency);
    }

    private Double rateCalculate(String soldCurrency, String purchasedCurrency) {
        Currency soldCurrencyEntity = currencyService.findByCode(soldCurrency);
        Double a = Double.parseDouble(soldCurrencyEntity.getSelling());
        Currency purchasedCurrencyEntity = currencyService.findByCode(purchasedCurrency);
        Double b = Double.parseDouble(purchasedCurrencyEntity.getBuying());
        Double rate = a / b;
        return rate;
    }

    private BigDecimal calculateTransactionCostForCross(Account account, double amount, double rate) {
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateNewBalanceForCross(amount, rate);
        BigDecimal newBalance = balance.subtract(cost);
        return newBalance;
    }

    private BigDecimal calculateNewBalanceForCross(double amount, double rate) {
        BigDecimal amountBigDecimal = BigDecimal.valueOf(amount);
        BigDecimal rateBigDecimal = BigDecimal.valueOf(rate);
        BigDecimal cost = amountBigDecimal.multiply(rateBigDecimal);
        return cost;
    }
    //**************************************************************************

    //TRY ile yapılan işlemler için hesaplar ***********************************
    private BigDecimal calculateTransactionCostForTRY(char transactionType, double amount, String currencyCode) {
        Currency currency = currencyService.findByCode(currencyCode);
        double currencyPrice;
        if (transactionType == 'S') {
            currencyPrice = Double.parseDouble(currency.getBuying());
        } else currencyPrice = Double.parseDouble(currency.getSelling());
        BigDecimal cost = new BigDecimal(currencyPrice * amount);
        return cost;
    }

    private BigDecimal calculateNewBalanceForTRY(Account account, double amount, String purchasedCurrency, char transactionType) {
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateTransactionCostForTRY(transactionType, amount, purchasedCurrency);
        BigDecimal newBalance = balance.subtract(cost);
        return newBalance;
    }
    //****************************************************************************

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