package com.infina.corso.controller;

import com.infina.corso.dto.request.CreateAccountRequest;
import com.infina.corso.dto.response.GetAccountByIdResponse;
import com.infina.corso.dto.response.GetAllAccountResponse;
import com.infina.corso.dto.response.GetCustomerAccountsForTransactionPage;
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
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @Operation(summary = "Get all accounts", description = "Retrieve a list of all accounts.")
    public ResponseEntity<List<GetAllAccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieve an account by ID.")
    public ResponseEntity<GetAccountByIdResponse> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get accounts by customer ID", description = "Retrieve a list of accounts by customer ID.")
    public ResponseEntity<List<GetAllAccountResponse>> getAccountsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
    }

    @GetMapping("/customer/transaction/{customerId}")
    @Operation(summary = "Get accounts by customer ID", description = "Retrieve a list of accounts by customer ID.")
    public ResponseEntity<List<GetCustomerAccountsForTransactionPage>> getAccountsBalanceBiggerThanZeroByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getAccountsBalanceBiggerThanZeroByCustomerId(customerId));
    }

    @PostMapping("/{customerId}")
    @Operation(summary = "Create a new account", description = "Create a new account with the given details.")
    @PreAuthorize("hasRole('ROLE_BROKER') OR hasRole('ROLE_MANAGER')")
    public GenericMessage createAccount(@PathVariable Long customerId, @RequestBody CreateAccountRequest createAccountRequest) {
        accountService.createAccount(createAccountRequest, customerId);
        return new GenericMessage(Messages.getMessageForLocale("corso.create.account.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_BROKER')")
    @Operation(summary = "Delete an account", description = "Delete an account by ID.")
    public GenericMessage deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return new GenericMessage(Messages.getMessageForLocale("corso.delete.account.success.message.successfully",
                LocaleContextHolder.getLocale()));
    }
    @PutMapping("/reactivate/{id}")
    @PreAuthorize("hasRole('ROLE_BROKER')")
    @Operation(summary = "Reactivate a deleted account", description = "Reactivate an account by ID.")
    public GenericMessage reactivateAccount(@PathVariable Long id) {
        accountService.reactivateAccount(id);
        return new GenericMessage(Messages.getMessageForLocale("corso.reactivate.account.success.message",
                LocaleContextHolder.getLocale()));
    }

    @GetMapping("/broker/{userId}")
    @PreAuthorize("hasRole('ROLE_BROKER')")
    @Operation(summary = "Get accounts by user ID", description = "Retrieve a list of accounts by user ID.")
    public ResponseEntity<List<GetAllAccountResponse>> getAllAccountsForBroker(@PathVariable int userId) {
        return ResponseEntity.ok(accountService.getAllAccountsForBroker(userId));
    }
}