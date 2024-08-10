package com.infina.corso.repository;

import com.infina.corso.model.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Long> {
}
