package com.infina.corso.model;

import lombok.*;

import java.math.BigDecimal;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pools")
public class Pool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  Long  brokerId;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Currency currency;

}
