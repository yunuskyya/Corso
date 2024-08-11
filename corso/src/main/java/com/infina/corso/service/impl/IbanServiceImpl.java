package com.infina.corso.service.impl;

import com.infina.corso.dto.request.IbanRegisterRequest;
import com.infina.corso.exception.DuplicateIbanException;
import com.infina.corso.model.Customer;
import com.infina.corso.model.Iban;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.repository.IbanRepository;
import com.infina.corso.service.IbanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IbanServiceImpl implements IbanService {

    private final IbanRepository ibanRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapperForRequest;

    @Autowired
    public IbanServiceImpl(IbanRepository ibanRepository, CustomerRepository customerRepository, ModelMapper modelMapperForRequest) {
        this.ibanRepository = ibanRepository;
        this.customerRepository = customerRepository;
        this.modelMapperForRequest = modelMapperForRequest;
    }

    @Override
    @Transactional
    public void saveIban(IbanRegisterRequest ibanRegisterRequest) {
        Optional<Customer> customer = customerRepository.findById(ibanRegisterRequest.getCustomer_id());
        Iban iban = modelMapperForRequest.map(ibanRegisterRequest, Iban.class);
        if (checkIbanForDuplicate(iban, customer)) {
            throw new DuplicateIbanException("Bu IBAN zaten mevcut.");
        }
        customer.get().getIbans().add(iban);
        iban.setCustomer(customer.get());
        customerRepository.save(customer.get());

    }

    private boolean checkIbanForDuplicate(Iban iban, Optional<Customer> customer) {
        List<Iban> ibans = customer.get().getIbans();
        return ibans.stream()
                .anyMatch(existingIban -> existingIban.getIban().equals(iban.getIban()));
    }


    @Override
    public Optional<Iban> getIbanById(Long id) {
        return ibanRepository.findById(id);
    }

    @Override
    public List<Iban> getAllIbans() {
        return ibanRepository.findAll();
    }

    @Override
    public void deleteIban(Long id) {
        ibanRepository.deleteById(id);
    }
}
