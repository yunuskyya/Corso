package com.infina.corso.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private char transaction_type;

    @Column
    private String purchased_currency;

    @Column
    private String sold_currency;

    @Column
    private int amount;

    @CreationTimestamp
    private LocalDateTime transaction_date;

    // Currency unit price --> Price

    // Currency id  --> Currency




}
