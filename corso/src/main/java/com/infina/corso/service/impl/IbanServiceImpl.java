package com.infina.corso.service.impl;

import com.infina.corso.model.Iban;
import com.infina.corso.repository.IbanRepository;
import com.infina.corso.service.IbanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IbanServiceImpl implements IbanService {

    private final IbanRepository ibanRepository;

    @Autowired
    public IbanServiceImpl(IbanRepository ibanRepository) {
        this.ibanRepository = ibanRepository;
    }

    @Override
    public Iban saveIban(Iban iban) {
        return ibanRepository.save(iban);
    }

    @Override
    public Optional<Iban> getIbanById(Long id) {
        return ibanRepository.findById(id);
    }

    @Override
    public List<Iban> getAllIbans() {
        return ibanRepository.findAll();
    }

    /*
    @Override
    public Iban updateIban(Long id, Iban iban) {
        Optional<Iban> existingIban = ibanRepository.findById(id);
        if (existingIban.isPresent()) {
            Iban updatedIban = existingIban.get();
            updatedIban.setIban(iban.getIban());
            updatedIban.setCurrencyType(iban.getCurrencyType());
            updatedIban.setCustomer(iban.getCustomer());
            return ibanRepository.save(updatedIban);
        } else {
            throw new RuntimeException("Iban not found with id: " + id);
        }
    }

     */

    @Override
    public void deleteIban(Long id) {
        ibanRepository.deleteById(id);
    }
}
