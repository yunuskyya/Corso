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
import com.infina.corso.model.enums.CustomerType;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.infina.corso.specifications.CustomerSpecification.*;

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
    public CustomerGetByIdResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(customer -> modelMapperResponse.map(customer, CustomerGetByIdResponse.class))
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    // only manager or broker
    @Override
    public Page<CustomerByBrokerResponse> getAllCustomersByBrokerId(Long brokerId, Pageable pageable) {
        return customerRepository.findAllByUserId(brokerId, pageable)
                .map(customer -> modelMapperResponse.map(customer, CustomerByBrokerResponse.class));
    }

    // Only manager or admin can use this method or the controller that calls this method must have a security check
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
        if (accountRequestTransaction != null) {
            return accountRequestTransaction;
        } else return accountRequestTransaction;
    }

    // only manager or broker
    @Override
    public void createCustomer(CustomerUpdateRequest customerDto) {
        Customer customerEntity = modelMapperRequest.map(customerDto, Customer.class);
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
            return modelMapperResponse.map(customerEntity, CustomerResponse.class);
        } else {
            throw new RuntimeException("Customer not found");
        }
    }

    // only manager or broker
    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public Page<CustomerFilterResponse> filterCustomers(CustomerFilterRequest filterRequest, Pageable pageable) {
        Specification<Customer> filters = Specification.where(StringUtils.hasText(filterRequest.getName())? null : likeName(filterRequest.getName()))
                .or(StringUtils.hasText(filterRequest.getName())? null : likeSurname(filterRequest.getName()))
                .or(StringUtils.hasText(filterRequest.getName())? null : likeCompanyName(filterRequest.getName()))
                .and(StringUtils.hasText(filterRequest.getTcKimlikNo())? null : hasTcKimlikNo(filterRequest.getTcKimlikNo()));
        return null;
    }

}
