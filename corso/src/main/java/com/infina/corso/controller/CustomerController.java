package com.infina.corso.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/{userId}/customer/{customerId}/account")
public class CustomerController {

    @GetMapping
    public String getCustomer(@PathVariable("userId") Long userId) {
        return "ID'si " + userId + " olan broker'ın tüm Customers";
    }

    @GetMapping("/{id}") // /api/v1/user/customer/1
    public String getCustomerById(@PathVariable Long id, @PathVariable("userId") Long userId){
        return "ID'si " + userId + " olan broker'ın ID'si " + id + " olan Customer";
    }

    // /api/v1/customer?brokerId=1
}
