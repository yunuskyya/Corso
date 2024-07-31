package com.infina.corso.model;

import jakarta.persistence.*;

@Entity
public class Price{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "from_currency_id")
    private Currency fromCurrency;

    @ManyToOne
    @JoinColumn(name = "to_currency_id")
    private Currency toCurrency;

    private Double price;

    // getters and setters
}
