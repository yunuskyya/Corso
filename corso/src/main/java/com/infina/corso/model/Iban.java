package com.infina.corso.model;

import com.infina.corso.model.enums.CurrencyName;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "iban")
public class Iban {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String iban;

    @Enumerated(EnumType.STRING)
    private CurrencyName currencyType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private  Customer customer;
}
