package com.infina.corso.repository;

import com.infina.corso.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Customer findByEmail(String email);

    Customer deleteByEmail(String email);

    Page<Customer> findAllByUserId(Long userId, Pageable pageable); // findAllByBrokerId idi
}
