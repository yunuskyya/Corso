package com.infina.corso.dto.request;

import lombok.Getter;
import java.util.List;

@Getter
public class TransactionRequest {

    private String accountNumber;
    private String purchasedCurrency; //Eğer alınan döviz TL ise bu işlemin türü "S(satış)" eğer alınan döviz TL değil ise "A(alış)" olarak kaydedilecek
    private String soldCurrency;
    private double amount;
    private int user_id;
}
