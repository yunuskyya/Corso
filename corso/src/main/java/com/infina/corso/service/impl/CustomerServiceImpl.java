package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.CustomerRequest;
import com.infina.corso.dto.response.CustomerResponse;
import com.infina.corso.model.Customer;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::mapToGetCustomerResponse)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    @Override
    public Page<CustomerResponse> getAllCustomersPaged(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::mapToGetCustomerResponse);
    }

    @Override
    public void createCustomer(CustomerRequest customer) {
        Customer customerEntity = mapToCustomer(customer);
        customerRepository.save(customerEntity);
    }

    @Override
    public CustomerResponse updateCustomer(Long id, CustomerRequest customer) {
        Optional<Customer> foundCustomer = customerRepository.findById(id);

        if(foundCustomer.isPresent()) {
            Customer customerEntity = mapToCustomer(customer);
            customerEntity.setId(id);
            customerRepository.save(customerEntity);
            return mapToGetCustomerResponse(customerEntity);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private Customer mapToCustomer(CustomerRequest customer) {
        return modelMapperConfig.modelMapperForRequest()
                .map(customer, Customer.class);
    }

    private CustomerResponse mapToGetCustomerResponse(Customer customer) {
        return modelMapperConfig.modelMapperForResponse()
                .map(customer, CustomerResponse.class);
    }
}
