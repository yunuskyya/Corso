package com.infina.corso.repository;

import com.infina.corso.model.Account;
import com.infina.corso.model.enums.CustomerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(Long customerId);
    List<Account> findByCurrencyAndCustomerCustomerType(String currency, CustomerType customerType);
    List<Account> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    Account findByCurrencyAndCustomerId(String currency, Long customerId);
}