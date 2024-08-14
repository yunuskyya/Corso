package com.infina.corso.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {

    public Transaction(char transactionType, String purchasedCurrency, String soldCurrency, int amount) {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private char transactionType;

    @Column
    private String purchasedCurrency;

    @Column
    private String soldCurrency;

    @Column
    private int amount;

   @Column
    private Double cost;

    @Column
    private Double rate;

    @Column
    private LocalDate systemDate;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
