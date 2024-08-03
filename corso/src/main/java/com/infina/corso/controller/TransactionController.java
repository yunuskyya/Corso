package com.infina.corso.controller;



import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.service.impl.TransactionServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    public TransactionController(TransactionServiceImp transactionServiceImp) {
        this.transactionServiceImp = transactionServiceImp;
    }

    private final TransactionServiceImp transactionServiceImp;

    @PostMapping("/create")
    public void create (@RequestBody TransactionResponse transactionResponse){
        //transactionServiceImp.transactionSave(transactionResponse);
    }

    @DeleteMapping("/delete")
    public void delete (@RequestBody TransactionResponse transactionResponse){

    }

    @PostMapping("/update")
    public void update (@RequestBody TransactionResponse transactionResponse){

    }

    @GetMapping("/get/all/{id}")
    public List<TransactionResponse> getAll(@PathVariable Long id){

        return transactionServiceImp.collectTransactions(id);

    }

}
