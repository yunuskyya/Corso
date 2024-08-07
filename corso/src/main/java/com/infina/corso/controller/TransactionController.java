package com.infina.corso.controller;



import com.infina.corso.dto.request.TransactionRequest;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.service.impl.TransactionServiceImp;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionController {

    public TransactionController(TransactionServiceImp transactionServiceImp) {
        this.transactionServiceImp = transactionServiceImp;
    }

    private final TransactionServiceImp transactionServiceImp;

    @PostMapping("/create")
    public void create (@RequestBody TransactionRequest transactionRequest){
        transactionServiceImp.transactionSave(transactionRequest);
    }

    @DeleteMapping("/delete")
    public void delete (@RequestBody TransactionResponse transactionResponse){

    }

    @PutMapping("/update")
    public void update (@RequestBody TransactionResponse transactionResponse){

    }
    //Sadece admin ve yönetici erişebilecek
    @GetMapping("/get/all")
    public List<TransactionResponse> getAll(){
        return transactionServiceImp.collectAllTransactions();

    }

    @GetMapping("/get/all/user/{userId}")
    public List<TransactionResponse> getTransactionsSelectedUser(@PathVariable int userId){
        return transactionServiceImp.collectTransactionsForSelectedUser(userId);

    }

}
