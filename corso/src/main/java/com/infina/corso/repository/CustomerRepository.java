package com.infina.corso.repository;

import com.infina.corso.model.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    Customer findByEmail(String email);

    Customer deleteByEmail(String email);

    Page<Customer> findAllByUserId(Long userId, Pageable pageable); // findAllByBrokerId idi

    @Query("SELECT c FROM Customer c WHERE " +
            "(c.email IS NOT NULL AND c.email = :email) OR " +
            "(c.phone IS NOT NULL AND c.phone = :phone) OR " +
            "(c.tcKimlikNo IS NOT NULL AND c.tcKimlikNo = :tcKimlikNo) OR " +
            "(c.vkn IS NOT NULL AND c.vkn = :vkn)")
    Optional<Customer> isUnique(
            @Param("email") String email,
            @Param("phone") String phone,
            @Param("tcKimlikNo") String tcKimlikNo,
            @Param("vkn") String vkn);
}
