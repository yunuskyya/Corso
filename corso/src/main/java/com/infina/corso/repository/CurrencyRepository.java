package com.infina.corso.repository;

import com.infina.corso.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Currency findById(Long id);

    Currency findByCode(String code);



}
