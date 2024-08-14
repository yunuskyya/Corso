package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.exception.InsufficientFundsException;
import com.infina.corso.exception.UserNotFoundException;
import com.infina.corso.model.*;
import com.infina.corso.repository.*;
import com.infina.corso.service.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private final CustomerRepository customerRepository;
    private final SystemDateRepository systemDateRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, ModelMapperConfig modelMapperConfig, UserServiceImpl userServiceImpl, CurrencyServiceImp currencyService, CustomerService customerService, AccountRepository accountRepository, UserRepository userRepository, AccountService accountService, SystemDateService systemDateService, CustomerRepository customerRepository, SystemDateRepository systemDateRepository) {
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
        this.customerRepository = customerRepository;
        this.systemDateRepository = systemDateRepository;
    }

    @Transactional
    public void transactionSave(TransactionRequest transactionRequest) {
        if (!systemDateService.isDayClosedStarted()) {
            try {
                LocalDate systemDate = systemDateService.getSystemDate();
                AccountRequestTransaction accountRequestTransaction = accountService.checkIfAccountExists(transactionRequest.getAccount_id(), transactionRequest.getPurchasedCurrency());
                if (accountRequestTransaction.getAccountNo() != null) {
                    Transaction transaction = modelMapperConfig.modelMapperForRequest().map(transactionRequest, Transaction.class);
                    transaction.setSystemDate(systemDate);
                    Optional<Account> account = accountRepository.findById(transactionRequest.getAccount_id());
                    account.get().getTransactions().add(transaction);
                    boolean isCrossRate = !transactionRequest.getSoldCurrency().equals("TL") && !transactionRequest.getPurchasedCurrency().equals("TL");
                    if (isCrossRate) {
                        Double rate = calculateCurrencyRate(transactionRequest.getSoldCurrency(), transactionRequest.getPurchasedCurrency());
                        transaction.setRate(rate);
                        double transactionAmountInSoldCurrency = transactionRequest.getAmount();
                        BigDecimal newBalance = calculateTransactionCostForCross(account.get(), transactionRequest.getAmount(), rate, transaction);
                        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                            throw new InsufficientFundsException("Insufficient funds for account number: " + account.get().getAccountNumber());
                                    }
                        account.get().setBalance(newBalance);
                    } else {
                        BigDecimal newBalance;
                        if (transaction.getSoldCurrency().equals("TL")) {
                            transaction.setTransactionType('A');
                            transaction.setRate(Double.parseDouble(currencyService.findByCode(transactionRequest.getPurchasedCurrency()).getSelling()));
                            newBalance = calculateNewBalanceForTRY(account.get(), transaction.getAmount(), transaction.getPurchasedCurrency(), transaction.getTransactionType(),transaction);
                        } else {
                            transaction.setTransactionType('S');
                            transaction.setRate(Double.parseDouble(currencyService.findByCode(transactionRequest.getSoldCurrency()).getBuying()));
                            newBalance = calculateNewBalanceForTRY(account.get(), transaction.getAmount(), transaction.getSoldCurrency(), transaction.getTransactionType(), transaction);
                        }
                        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
                            throw new InsufficientFundsException("Insufficient funds for account number: " + account.get().getAccountNumber());
                        }
                        account.get().setBalance(newBalance);
                    }
                    Account accountPurchasedCurrency = accountRepository.findByAccountNumber(accountRequestTransaction.getAccountNo());
                    BigDecimal amountToAdd = BigDecimal.valueOf(transactionRequest.getAmount());
                    BigDecimal updatedBalancePurchasedCurrency = accountPurchasedCurrency.getBalance().add(amountToAdd);
                    accountPurchasedCurrency.setBalance(updatedBalancePurchasedCurrency);
                    User user = userRepository.findById(transactionRequest.getUser_id())
                            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + transactionRequest.getUser_id()));
                    transaction.setUser(user);
                    transaction.setAccount(account.get());
                    transactionRepository.save(transaction);
                    user.getTransactions().add(transaction);
                    accountRepository.save(account.get());
                    accountRepository.save(accountPurchasedCurrency);
                    userRepository.save(user);
                } else
                    throw new AccountNotFoundException("Account not found with id: " + transactionRequest.getAccount_id());
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
    public Double calculateCurrencyRate(String soldCurrency, String purchasedCurrency) {
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

    private BigDecimal calculateTransactionCostForCross(Account account, double amount, double rate, Transaction transaction) {
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateNewBalanceForCross(amount, rate);
        transaction.setCost(cost.doubleValue());
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
    private BigDecimal calculateTransactionCostForTRY(char transactionType, double amount, String currencyCode) {
        Currency currency = currencyService.findByCode(currencyCode);
        double currencyPrice;
        BigDecimal cost;
        if (transactionType == 'S') {
            cost = new BigDecimal(amount);
        } else {
            currencyPrice = Double.parseDouble(currency.getSelling());
            cost = new BigDecimal(currencyPrice * amount);
        }
        return cost;
    }

    private BigDecimal calculateNewBalanceForTRY(Account account, double amount, String purchasedCurrency, char transactionType, Transaction transaction) {
        BigDecimal balance = account.getBalance();
        BigDecimal cost = calculateTransactionCostForTRY(transactionType, amount, purchasedCurrency);
        transaction.setCost(cost.doubleValue());
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

    public List<TransactionResponse> collectAllTransactionForDayClose (){
        LocalDate localDate = systemDateRepository.findById(1).get().getDate();
        List<Transaction> transactionList = transactionRepository.findBySystemDate(localDate);
        return convertTractionListAsDto(transactionList);
    }



    //Entity listesinin Dto listesine çevrimi
    private List<TransactionResponse> convertTractionListAsDto(List<Transaction> transactionList) {
        return transactionList.stream()
                .map(transaction -> {
                    TransactionResponse response = modelMapperConfig.modelMapperForTransaction()
                            .map(transaction, TransactionResponse.class);
                    // Transaction'dan Account'a ve Account'tan Customer'a ulaşarak ad ve soyadını alıyoruz.
                    Customer customer = transaction.getAccount().getCustomer();
                    response.setName(customer.getName());
                    response.setSurname(customer.getSurname());
                    return response;
                })
                .collect(Collectors.toList());

    }

}