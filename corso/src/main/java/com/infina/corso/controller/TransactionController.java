package com.infina.corso.controller;



import com.infina.corso.dto.TransactionDTO;
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
    public void create (@RequestBody TransactionDTO transactionDto){
        transactionServiceImp.transactionSave(transactionDto);
    }

    @DeleteMapping("/delete")
    public void delete (@RequestBody TransactionDTO transactionDto){

    }

    @PostMapping("/update")
    public void update (@RequestBody TransactionDTO transactionDto){

    }

    @GetMapping("/get/all/{id}")
    public List<TransactionDTO> getAll(@PathVariable Long id){

        return transactionServiceImp.collectTransactions(id);

    }

}
