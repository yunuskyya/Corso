package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.TransactionDTO;
import com.infina.corso.model.Transaction;
import com.infina.corso.repository.TransactionRepository;
import com.infina.corso.service.TransactionService;
import com.infina.corso.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionServiceImp implements TransactionService {

    public TransactionServiceImp(TransactionRepository transactionRepository, ModelMapperConfig modelMapperConfig, UserService userService) {
        this.transactionRepository = transactionRepository;
        this.modelMapperConfig = modelMapperConfig;
        this.userService = userService;
    }

    private final ModelMapperConfig modelMapperConfig;

    private final TransactionRepository transactionRepository;

    private final UserService userService;


    public void transactionSave(TransactionDTO transactionDTO) {
        //ilgili kurallar yazılacak
        //User user = userService // üzerinden bir metot ile  transactionDTO içindeki user id ile useri getirecek
        Transaction transaction = modelMapperConfig.modelMapperForRequest().map(transactionDTO, Transaction.class);
        //userService.setTransaction(transaction); //transactionu usere kaydedecek ? ya da Customera kaydedecek ?
        transactionRepository.save(transaction);

    }

    //FINDING ALL TRANSACTION's AND PUTING ON LIST
    public List collectTransactions (Long id){

        //List<Transaction> transactionList = transactionRepository.findByUserId(id);
        //return convertTractionListAsDto(transactionList);
        return null;
    }

    //Converting Entity to Dto as a List
    public List<TransactionDTO> convertTractionListAsDto(List<Transaction> transactionList) {
        List<TransactionDTO> transactionDtoList = new ArrayList<>();

        for (int i = 0; i < transactionList.size(); i++) {
            TransactionDTO transactionDto= modelMapperConfig.modelMapperForResponse().map(transactionList.get(i), TransactionDTO.class);
            transactionDtoList.add(transactionDto);
        }

        return transactionDtoList;
    }





}
