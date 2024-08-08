package com.infina.corso.controller;

import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.request.UpdateAccountRequest;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.dto.response.GetAllAccountResponse;
import com.infina.corso.model.Account;
import com.infina.corso.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/accounts")
@Tag(name = "Account Management", description = "Operations related to account management")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Operation(summary = "Get all accounts", description = "Retrieve a list of all accounts.")
    public ResponseEntity<List<GetAllAccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve an account by ID.")
    public GetAccountByIdResponse getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get accounts by customer ID", description = "Retrieve a list of accounts by customer ID.")
    public ResponseEntity<List<GetAllAccountResponse>> getAccountsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));}

    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new account with the given details.")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest createAccountRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(createAccountRequest));
    }


    @PutMapping("/update/{customerId}/{accountId}")
    @Operation(summary = "Update an account", description = "Update an account by ID.")
    public ResponseEntity<GetAccountByIdResponse> updateAccount(
            @PathVariable Long customerId,
            @PathVariable Long accountId,
            @RequestBody UpdateAccountRequest updateAccountRequest) {
        GetAccountByIdResponse updatedAccount = accountService.updateAccount(customerId, accountId, updateAccountRequest);
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account", description = "Delete an account by ID.")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}