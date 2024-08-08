package com.infina.corso.controller;

import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.request.UpdateAccountRequest;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.dto.response.GetAllAccountResponse;
import com.infina.corso.model.Account;
import com.infina.corso.service.AccountService;
import com.infina.corso.shared.GenericMessage;
import com.infina.corso.shared.Messages;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public GenericMessage getAccountsByCustomerId(@PathVariable Long customerId) {
        return new GenericMessage(Messages.getMessageForLocale("corso.get.accounts.by.customer.id.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }

    @PostMapping
    @Operation(summary = "Create a new account", description = "Create a new account with the given details.")
    @PreAuthorize("hasRole('ROLE_BROKER') OR hasRole('ROLE_MANAGER')")
    public GenericMessage createAccount(@RequestParam Long customerId, @RequestBody CreateAccountRequest createAccountRequest) {
        accountService.createAccount(createAccountRequest, customerId);
        return new GenericMessage(Messages.getMessageForLocale("corso.create.account.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }

    @PutMapping("/update/{customerId}/{accountId}")
    @Operation(summary = "Update an account", description = "Update an account by ID.")
    public GenericMessage updateAccount(
            @PathVariable Long customerId,
            @PathVariable Long accountId,
            @RequestBody UpdateAccountRequest updateAccountRequest) {
        GetAccountByIdResponse updatedAccount = accountService.updateAccount(customerId, accountId, updateAccountRequest);
        return new GenericMessage(Messages.getMessageForLocale("corso.update.account.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an account", description = "Delete an account by ID.")
    public GenericMessage deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return new GenericMessage(Messages.getMessageForLocale("corso.delete.account.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }
}