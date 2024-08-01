package com.infina.corso.service;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    public TransactionService(TransactionRepository transactionRepository, ModelMapperConfig modelMapperConfig) {
        this.transactionRepository = transactionRepository;
        this.modelMapperConfig = modelMapperConfig;
    }

    private final ModelMapperConfig modelMapperConfig;

    private final TransactionRepository transactionRepository;





}
