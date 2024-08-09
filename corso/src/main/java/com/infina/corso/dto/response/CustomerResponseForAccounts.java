package com.infina.corso.dto.response;

import com.infina.corso.model.Account;
import lombok.Data;

import java.util.List;

@Data
public class CustomerResponseForAccounts {
    private String name;
    private String surname;
    //private List<AccountResponse> accounts;
}
