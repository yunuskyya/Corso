package com.infina.corso.repository;

import com.infina.corso.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Integer> {

    Price findById(Long id);

}
