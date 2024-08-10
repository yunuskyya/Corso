package com.infina.corso.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

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

}
