package com.infina.corso.repository;

import com.infina.corso.model.Iban;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IbanRepository extends JpaRepository<Iban, Long> {

    Iban findByIban(String iban);


}
