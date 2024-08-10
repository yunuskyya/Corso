package com.infina.corso.service.impl;

import com.infina.corso.dto.request.IbanRegisterRequest;
import com.infina.corso.model.Customer;
import com.infina.corso.model.Iban;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.repository.IbanRepository;
import com.infina.corso.service.CustomerService;
import com.infina.corso.service.IbanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IbanServiceImpl implements IbanService {

    private final IbanRepository ibanRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapperForRequest;

    @Autowired
    public IbanServiceImpl(IbanRepository ibanRepository, CustomerRepository customerRepository, @Qualifier("modelMapperForRequest") ModelMapper modelMapperForRequest) {
        this.ibanRepository = ibanRepository;
        this.customerRepository = customerRepository;
        this.modelMapperForRequest = modelMapperForRequest;
    }

    @Override
    public Iban saveIban(IbanRegisterRequest ibanRegisterRequest, Long customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        Iban iban = modelMapperForRequest.map(ibanRegisterRequest, Iban.class);
        customer.get().getIbans().add(iban);
        customerRepository.save(customer.get());
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

    @Override
    public void deleteIban(Long id) {
        ibanRepository.deleteById(id);
    }
}
