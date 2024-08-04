package com.infina.corso.controller;



import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.service.impl.TransactionServiceImp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@Tag(name = "Transaction Management", description = "Operations related to transaction management")
public class TransactionController {

    public TransactionController(TransactionServiceImp transactionServiceImp) {
        this.transactionServiceImp = transactionServiceImp;
    }

    private final TransactionServiceImp transactionServiceImp;

    @PostMapping("/create")
    @Operation(summary = "Create a new transaction", description = "Create a new transaction with the given details.")
    public void create (@RequestBody TransactionResponse transactionResponse){
        //transactionServiceImp.transactionSave(transactionResponse);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a transaction", description = "Delete a transaction with the given details.")
    public void delete (@RequestBody TransactionResponse transactionResponse){

    }

    @PostMapping("/update")
    @Operation(summary = "Update a transaction", description = "Update a transaction with the given details.")
    public void update (@RequestBody TransactionResponse transactionResponse){

    }

    @GetMapping("/get/all/{id}")
    @Operation(summary = "Get all transactions", description = "Retrieve a list of all transactions.")
    public List<TransactionResponse> getAll(@PathVariable Long id){

        return transactionServiceImp.collectTransactions(id);

    }

}
