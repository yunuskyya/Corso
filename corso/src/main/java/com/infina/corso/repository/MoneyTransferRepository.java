package com.infina.corso.repository;

import com.infina.corso.model.MoneyTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MoneyTransferRepository extends JpaRepository<MoneyTransfer, Long> {

    List<MoneyTransfer> findBySystemDate(LocalDate systemDate);

}
