package com.infina.corso.service;

import com.infina.corso.dto.request.CustomerFilterRequest;
import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.dto.response.CustomerByBrokerResponse;
import com.infina.corso.dto.response.CustomerFilterResponse;
import com.infina.corso.dto.response.CustomerGetByIdResponse;
import com.infina.corso.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface CustomerService {
    CustomerGetByIdResponse getCustomerById(Long id);
    Page<CustomerByBrokerResponse> getAllCustomersByBrokerId(Long brokerId, Pageable pageable);
    Page<CustomerResponse> getAllCustomersPaged(Pageable pageable);
    void createCustomer(CustomerUpdateRequest customerDto);
    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest customerDto);
    void deleteCustomer(Long id);
    Page<CustomerFilterResponse> filterCustomersPaged(CustomerFilterRequest filterRequest, Pageable pageable);
    List<CustomerFilterResponse> filterCustomers(CustomerFilterRequest filterRequest);
}
