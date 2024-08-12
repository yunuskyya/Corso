package com.infina.corso.service;

import com.infina.corso.dto.request.CreateCustomerRequest;
import com.infina.corso.dto.request.CustomerFilterRequest;
import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CustomerService {
    CustomerGetByIdResponse getCustomerById(Long id);
    Page<CustomerByBrokerResponse> getAllCustomersByBrokerId(Long brokerId, Pageable pageable);
    Page<CustomerByBrokerResponseTransactionPage> getAllCustomersByBrokerIdForTransaction(Long brokerId, Pageable pageable);
    Page<CustomerResponse> getAllCustomersPaged(Pageable pageable);
    void createCustomer(CreateCustomerRequest customerDto);
    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest customerDto);
    void deleteCustomer(Long id);
    Page<CustomerFilterResponse> filterCustomersPaged(CustomerFilterRequest filterRequest, Pageable pageable);
    List<CustomerFilterResponse> filterCustomers(CustomerFilterRequest filterRequest);
}
