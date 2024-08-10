package com.infina.corso.model;

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

    private String currencyCode;

    private String ibanName;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private  Customer customer;
}
