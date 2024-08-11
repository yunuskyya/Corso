package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.CustomerFilterRequest;
import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.dto.response.CustomerByBrokerResponse;
import com.infina.corso.dto.response.CustomerFilterResponse;
import com.infina.corso.dto.response.CustomerGetByIdResponse;
import com.infina.corso.dto.response.CustomerResponse;
import com.infina.corso.model.Account;
import com.infina.corso.model.Customer;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.CustomerType;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.service.AuthService;
import com.infina.corso.service.CustomerService;
import com.infina.corso.specifications.CustomerSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapperResponse;
    private final ModelMapper modelMapperRequest;
    private final ModelMapperConfig modelMapperConfig;
    private final AuthService authService;

    public CustomerServiceImpl(CustomerRepository customerRepository,
            @Qualifier("modelMapperForResponse") ModelMapper modelMapperResponse,
            @Qualifier("modelMapperForRequest") ModelMapper modelMapperRequest, ModelMapperConfig modelMapperConfig,
            AuthService authService) {
        this.customerRepository = customerRepository;
        this.modelMapperResponse = modelMapperResponse;
        this.modelMapperRequest = modelMapperRequest;
        this.modelMapperConfig = modelMapperConfig;
        this.authService = authService;
    }

    // only manager or broker
    @Override
    public CustomerGetByIdResponse getCustomerById(Long id) {
        Customer customerInDb = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        int currentUserId = authService.getCurrentUserId();

        if (customerInDb.getUser().getId() != currentUserId) {
            throw new RuntimeException("You are not authorized to see this customer");
        }

        return customerRepository.findById(id)
                .map(customer -> modelMapperResponse.map(customer, CustomerGetByIdResponse.class))
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // only manager or broker
    @Override
    public Page<CustomerByBrokerResponse> getAllCustomersByBrokerId(Long brokerId, Pageable pageable) {
        Customer customerInDb = customerRepository.findById(brokerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        int currentUserId = authService.getCurrentUserId();

        return customerRepository.findAllByUserId(brokerId, pageable)
                .map(customer -> modelMapperResponse.map(customer, CustomerByBrokerResponse.class));
    }

    // Only manager or admin can use this method or the controller that calls this
    // method must have a security check
    @Override
    public Page<CustomerResponse> getAllCustomersPaged(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customer -> modelMapperResponse.map(customer, CustomerResponse.class));
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
        return accountRequestTransaction; // Her türlü aynı kodu return ediyordu
    }

    // only manager or broker
    @Override
    public void createCustomer(CustomerUpdateRequest customerDto) {
        int currentUserId = authService.getCurrentUserId();
        User user = new User();
        user.setId(currentUserId);
        Customer customerEntity = modelMapperRequest.map(customerDto, Customer.class);
        customerEntity.setUser(user);
        customerRepository.save(customerEntity);
    }

    // only manager or broker
    @Override
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest customerDto) {
        Customer customerInDb = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        int currentUserId = authService.getCurrentUserId();

        if (customerInDb.getUser().getId() != currentUserId) {
            throw new RuntimeException("You are not authorized to see this customer");
        }
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
            return modelMapperResponse.map(customerEntity, CustomerResponse.class);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    // only manager or broker
    @Override
    public void deleteCustomer(Long id) {
        Customer customerInDb = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        int currentUserId = authService.getCurrentUserId();

        if (customerInDb.getUser().getId() != currentUserId) {
            throw new RuntimeException("You are not authorized to see this customer");
        }

        customerRepository.deleteById(id);
    }

    @Override
    public Page<CustomerFilterResponse> filterCustomersPaged(CustomerFilterRequest filterRequest, Pageable pageable) {
        Specification<Customer> specification = CustomerSpecification.filterByAllGivenFieldsWithAnd(filterRequest);
        return customerRepository.findAll(specification, pageable)
                .map(customer -> modelMapperResponse.map(customer, CustomerFilterResponse.class));
    }

    @Override
    public List<CustomerFilterResponse> filterCustomers(CustomerFilterRequest filterRequest) {
        Specification<Customer> specification = CustomerSpecification.filterByAllGivenFieldsWithAnd(filterRequest);
        return customerRepository.findAll(specification)
                .stream()
                .map(customer -> modelMapperResponse.map(customer, CustomerFilterResponse.class))
                .toList();
    }

}
