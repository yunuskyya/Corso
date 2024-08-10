package com.infina.corso.controller;



import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.service.impl.TransactionServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {

    public TransactionController(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }

    private final TransactionServiceImpl transactionServiceImpl;

    @PostMapping("/create")
    public void create (@RequestBody TransactionRequest transactionRequest){
        transactionServiceImpl.transactionSave(transactionRequest);
    }

    @DeleteMapping("/delete")
    public void delete (@RequestBody TransactionResponse transactionResponse){

    }

    @PutMapping("/update")
    public void update (@RequestBody TransactionResponse transactionResponse){

    }
    //Sadece admin ve yönetici erişebilecek
    @GetMapping("/get-all")
    public List<TransactionResponse> getAll(){
        return transactionServiceImpl.collectAllTransactions();

    }

    @GetMapping("/get-all/{userId}")
    public List<TransactionResponse> getTransactionsSelectedUser(@PathVariable int userId){
        return transactionServiceImpl.collectTransactionsForSelectedUser(userId);

    }

}