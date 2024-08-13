package com.infina.corso.service.impl;

import com.infina.corso.config.ModelMapperConfig;
import com.infina.corso.dto.request.AccountRequestTransaction;
import com.infina.corso.dto.request.CreateCustomerRequest;
import com.infina.corso.dto.request.CustomerFilterRequest;
import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.dto.response.*;
import com.infina.corso.exception.AccessDeniedException;
import com.infina.corso.exception.CustomerNotFoundException;
import com.infina.corso.exception.UnauthorizedAccessException;
import com.infina.corso.model.Account;
import com.infina.corso.model.Customer;
import com.infina.corso.model.User;
import com.infina.corso.model.enums.CustomerStatus;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new CustomerNotFoundException(id));
        int currentUserId = authService.getCurrentUserId();

        if (customerInDb.getUser().getId() != currentUserId) {
            throw new UnauthorizedAccessException();
        }

        return customerRepository.findById(id)
                .map(customer -> modelMapperResponse.map(customer, CustomerGetByIdResponse.class))
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    // only manager or broker
    @Override
    public Page<CustomerByBrokerResponse> getAllCustomersByBrokerId(Long brokerId, Pageable pageable) {
//        Customer customerInDb = customerRepository.findById(brokerId)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//        int currentUserId = authService.getCurrentUserId();

        return customerRepository.findAllByUserId(brokerId, pageable)
                .map(customer -> modelMapperResponse.map(customer, CustomerByBrokerResponse.class));
    }

    // only manager or broker
    public Page<CustomerByBrokerResponseTransactionPage> getAllCustomersByBrokerIdForTransaction(Long brokerId, Pageable pageable) {
        return customerRepository.findAllByUserId(brokerId, pageable)
                .map(customer -> modelMapperResponse.map(customer, CustomerByBrokerResponseTransactionPage.class));
    }




    // Only manager or admin can use this method or the controller that calls this
    // method must have a security check
    @Override
    public Page<CustomerResponse> getAllCustomersPaged(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customer -> modelMapperResponse.map(customer, CustomerResponse.class));
    }

    public List<GetAllCustomerForEndOfDayResponse> getAllCustomersForEndOfDay() {
        List<Customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(customer -> {
                    GetAllCustomerForEndOfDayResponse response = modelMapperResponse
                            .map(customer, GetAllCustomerForEndOfDayResponse.class);
                    String customerNameSurname = customer.getName() + " " + customer.getSurname();
                    response.setCustomerNameSurname(customerNameSurname);
                    return response;
                })
                .collect(Collectors.toList());
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
    public void createCustomer(CreateCustomerRequest customerDto) {
        int currentUserId = authService.getCurrentUserId();
        User user = new User();
        user.setId(currentUserId);
        Customer customerEntity = modelMapperRequest.map(customerDto, Customer.class);
        customerEntity.setUser(user);
        customerEntity.setStatus(CustomerStatus.ACTIVE);
        customerRepository.save(customerEntity);
    }

    // only manager or broker
    @Override
    public CustomerResponse updateCustomer(Long id, CustomerUpdateRequest customerDto) {
        Customer customerInDb = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
                    int currentUserId = authService.getCurrentUserId();

        if (customerInDb.getUser().getId() != currentUserId) {
            throw new UnauthorizedAccessException();
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
              throw new CustomerNotFoundException(id);
            }
    }

    // only manager or broker
    @Override
    public void deleteCustomer(Long id) {
        Customer customerInDb = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        int currentUserId = authService.getCurrentUserId();

        if (customerInDb.getUser().getId() != currentUserId) {
            throw new AccessDeniedException();
        }

        customerRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Page<CustomerFilterResponse> filterCustomersPaged(CustomerFilterRequest filterRequest, Pageable pageable) {
        if (filterRequest.getUserId() != null) {
            if (filterRequest.getUserId() != authService.getCurrentUserId()) {
                throw new AccessDeniedException();
            }
        }

        Specification<Customer> specification = CustomerSpecification.filterByAllGivenFieldsWithAnd(filterRequest);
        Page<Customer> customers =  customerRepository.findAll(specification, pageable);
        return customers.map(customer -> {
            CustomerFilterResponse customerFilterResponse = modelMapperResponse.map(customer, CustomerFilterResponse.class);
            switch (customer.getCustomerType()) {
                case BIREYSEL:
                    customerFilterResponse.setName(customer.getName() + " " + customer.getSurname());
                    break;
                case KURUMSAL:
                    customerFilterResponse.setName(customer.getCompanyName());
                    break;
            }
            return customerFilterResponse;

        });
    }

    @Override
    @Transactional
    public List<CustomerFilterResponse> filterCustomers(CustomerFilterRequest filterRequest) {
        Specification<Customer> specification = CustomerSpecification.filterByAllGivenFieldsWithAnd(filterRequest);
        return customerRepository.findAll(specification)
                .stream()
                .map(customer -> modelMapperResponse.map(customer, CustomerFilterResponse.class))
                .toList();
    }

}
