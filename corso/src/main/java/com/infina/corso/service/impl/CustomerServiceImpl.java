package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.dto.response.CustomerResponse;
import com.infina.corso.model.Account;
import com.infina.corso.model.Customer;
import com.infina.corso.model.enums.CustomerType;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapperResponse;
    private final ModelMapper modelMapperRequest;
    private final ModelMapperConfig modelMapperConfig;

    public CustomerServiceImpl(CustomerRepository customerRepository, @Qualifier("modelMapperForResponse") ModelMapper modelMapperResponse,
                               @Qualifier("modelMapperForRequest") ModelMapper modelMapperRequest, ModelMapperConfig modelMapperConfig) {
        this.customerRepository = customerRepository;
        this.modelMapperResponse = modelMapperResponse;
        this.modelMapperRequest = modelMapperRequest;
        this.modelMapperConfig = modelMapperConfig;
    }

    // only manager or broker
    @Override
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::mapToGetCustomerResponse)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // only manager or broker
    @Override
    public Page<CustomerResponse> getAllCustomersByBrokerId(Long brokerId, Pageable pageable) {
        return customerRepository.findAllByUserId(brokerId, pageable)
                .map(this::mapToGetCustomerResponse);
    }

    // Only manager or admin can use this method or the controller that calls this method must have a security check
    @Override
    public Page<CustomerResponse> getAllCustomersPaged(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(this::mapToGetCustomerResponse);
    }

    public AccountRequestTransaction checkAccountsForPurchasedCurrency(Account account, String currencyCode) {
        Optional<Customer> customer = customerRepository.findById(account.getCustomer().getId());
        List<Account> accountList = customer.get().getAccounts();
        AccountRequestTransaction accountRequestTransaction = new AccountRequestTransaction();
        for (Account a : accountList) {
            if (a.getCurrency().equals(currencyCode)) {
                modelMapperConfig.modelMapperForResponse().map(a, accountRequestTransaction);
            }
        }
        if (accountRequestTransaction != null) {
            return accountRequestTransaction;
        } else return accountRequestTransaction;
    }

    // only manager or broker
    @Override
    public void createCustomer(CustomerUpdateRequest customer) {
        Customer customerEntity = mapToCustomer(customer);
        customerRepository.save(customerEntity);
    }

    // only manager or broker
    @Override
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest customerDto) {
        Optional<Customer> foundCustomer = customerRepository.findById(id);

        if (foundCustomer.isPresent()) {
            Customer customerEntity = modelMapperRequest.map(customerDto, Customer.class);
            customerEntity.setId(id);

            if (customerDto.getCustomerType() == CustomerType.BIREYSEL) {
                customerEntity.setCompanyName(null);
                customerEntity.setVkn(null);
            } else {
                customerEntity.setTcKimlikNo(null);
                customerEntity.setName(null);
                customerEntity.setSurname(null);
            }

            customerRepository.save(customerEntity);
            return mapToGetCustomerResponse(customerEntity);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    // only manager or broker
    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private Customer mapToCustomer(CustomerUpdateRequest customer) {
        return modelMapperRequest
                .map(customer, Customer.class);
    }

    private CustomerResponse mapToGetCustomerResponse(Customer customer) {
        return modelMapperResponse
                .map(customer, CustomerResponse.class);
    }
}
