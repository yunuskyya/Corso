package com.infina.corso.controller;

import com.infina.corso.model.Account;
import com.infina.corso.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@Tag(name = "Account Management", description = "Operations related to account management")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve a list of all accounts.")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get accounts by customer ID", description = "Retrieve a list of accounts by customer ID.")
    public ResponseEntity<List<Account>> getAccountsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
    }

    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new account with the given details.")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(account));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an account", description = "Update an account with the given details.")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account", description = "Delete an account by ID.")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }


}