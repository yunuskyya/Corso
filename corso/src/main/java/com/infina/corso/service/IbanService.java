package com.infina.corso.service;

import com.infina.corso.dto.request.IbanRegisterRequest;
import com.infina.corso.model.Iban;

import java.util.List;
import java.util.Optional;

public interface IbanService {
    void saveIban(IbanRegisterRequest ibanRegisterRequest);
    Optional<Iban> getIbanById(Long id);
    List<Iban> getAllIbans();
    //Iban updateIban(Long id, Iban iban);
    void deleteIban(Long id);
}
