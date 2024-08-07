package com.infina.corso.service;

import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.dto.response.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface CustomerService {
    CustomerResponse getCustomerById(Long id);
    Page<CustomerResponse> getAllCustomersByBrokerId(Long brokerId, Pageable pageable);
    Page<CustomerResponse> getAllCustomersPaged(Pageable pageable);
    void createCustomer(CustomerUpdateRequest customer);
    CustomerResponse updateCustomer(Long id, CustomerUpdateRequest customer);
    void deleteCustomer(Long id);

}
