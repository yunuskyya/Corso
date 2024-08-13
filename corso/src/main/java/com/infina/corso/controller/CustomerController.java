package com.infina.corso.controller;

import com.infina.corso.config.CurrentUser;
import com.infina.corso.dto.request.CreateCustomerRequest;
import com.infina.corso.dto.request.CustomerFilterRequest;
import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.dto.response.*;
import com.infina.corso.model.User;
import com.infina.corso.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

//@RestController
//@RequestMapping("/api/v1/user/{userId}/customer/{customerId}/account")
@RestController
@Validated
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Get a customer by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<CustomerGetByIdResponse> getCustomerById(@PathVariable Long id) {
        CustomerGetByIdResponse customerResponse = customerService.getCustomerById(id);
        return ResponseEntity.ok(customerResponse);
    }

    // Get all customers paged
    @GetMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<Page<CustomerResponse>> getAllCustomersPaged(Pageable pageable) {
        Page<CustomerResponse> customers = customerService.getAllCustomersPaged(pageable);
        return ResponseEntity.ok(customers);
    }

    // Get all customers by broker ID
    @GetMapping("/broker/{brokerId}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<Page<CustomerByBrokerResponse>> getAllCustomersByBrokerId(@PathVariable Long brokerId, Pageable pageable) {
        Page<CustomerByBrokerResponse> customers = customerService.getAllCustomersByBrokerId(brokerId, pageable);
        return ResponseEntity.ok(customers);
    }

    // Get all customers by broker ID
    @GetMapping("/transaction/broker/{brokerId}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<Page<CustomerByBrokerResponseTransactionPage>> getAllCustomersByBrokerIdTransactions(@PathVariable Long brokerId, Pageable pageable) {
        Page<CustomerByBrokerResponseTransactionPage> customers = customerService.getAllCustomersByBrokerIdForTransaction(brokerId, pageable);
        return ResponseEntity.ok(customers);
    }

    // Create a new customer
    @PostMapping
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<Void> createCustomer(@RequestBody @Validated CreateCustomerRequest customerRequest) {
        customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Update a customer
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long id, @RequestBody @Validated CustomerUpdateRequest customerRequest) {
        CustomerResponse updatedCustomer = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(updatedCustomer);
    }

    // Delete a customer
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    // Filter customers paged
    @PostMapping("/filter")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<Page<CustomerFilterResponse>> filterCustomersPaged(@RequestBody @Validated CustomerFilterRequest filterRequest, Pageable pageable /*Authentication authentication*/) {
        //CurrentUser user = (CurrentUser) authentication.getPrincipal();
        //System.out.println("User: " + user.getId());
        Page<CustomerFilterResponse> customers = customerService.filterCustomersPaged(filterRequest, pageable);
        return ResponseEntity.ok(customers);
    }

    // Filter customers
    @GetMapping("/filter/all")
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_BROKER')")
    public ResponseEntity<List<CustomerFilterResponse>> filterCustomers(@RequestBody @Validated CustomerFilterRequest filterRequest) {
        List<CustomerFilterResponse> customers = customerService.filterCustomers(filterRequest);
        return ResponseEntity.ok(customers);
    }
}