package com.infina.corso.controller;



import com.infina.corso.dto.TransactionDTO;
import com.infina.corso.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private final TransactionService transactionService;

    @PostMapping("/create")
    public void create (@RequestBody TransactionDTO transactionDto){

    }

    @DeleteMapping("/delete")
    public void delete (@RequestBody TransactionDTO transactionDto){

    }

    @PostMapping("/update")
    public void update (@RequestBody TransactionDTO transactionDto){

    }

    @GetMapping("/get/all")
    public List<TransactionDTO> getAll(){

        return null;

    }

}
