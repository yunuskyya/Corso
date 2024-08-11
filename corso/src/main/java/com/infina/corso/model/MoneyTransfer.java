package com.infina.corso.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class MoneyTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ibanNo;

    private char direction;

    private double amount;

    private String receiver;

    private String sender;

    private LocalDate systemDate;

    private Long customer_id;

    @CreationTimestamp
    private LocalDateTime creationTime;

}
