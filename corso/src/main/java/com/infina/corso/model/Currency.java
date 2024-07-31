package com.infina.corso.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    @OneToMany(mappedBy = "fromCurrency")
    private Set<Price> pricesFrom;

    @OneToMany(mappedBy = "toCurrency")
    private Set<Price> pricesTo;

    // getters and setters
}
