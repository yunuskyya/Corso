package com.infina.corso.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
public class MoneyTransfer {
    @Id
    private Long id;

    private String ibanNo;

    private char direction;

    private double amount;

    private String receiver;

    private String sender;

    private LocalDate systemDate;

    @CreationTimestamp
    private LocalDateTime creationTime;

}
