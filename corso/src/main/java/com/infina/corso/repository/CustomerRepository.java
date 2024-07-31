package com.infina.corso.repository;

import com.infina.corso.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findByEmail(String email);

    Customer deleteByEmail(String email);

}
