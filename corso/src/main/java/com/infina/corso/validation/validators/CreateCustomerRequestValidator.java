package com.infina.corso.validation.validators;

import com.infina.corso.dto.request.CreateCustomerRequest;
import com.infina.corso.model.Customer;
import com.infina.corso.repository.CustomerRepository;
import com.infina.corso.validation.annotations.ValidCreateCustomerRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Optional;

public class CreateCustomerRequestValidator implements ConstraintValidator<ValidCreateCustomerRequest, CreateCustomerRequest> {
    private final CustomerRepository customerRepository;

    public CreateCustomerRequestValidator(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public boolean isValid(CreateCustomerRequest createCustomerRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (createCustomerRequest.getCustomerType() == null) {
            return false;
        }

        Optional<Customer> foundCustomer = customerRepository.isUnique(createCustomerRequest.getEmail(), createCustomerRequest.getPhone(), createCustomerRequest.getTcKimlikNo(), createCustomerRequest.getVkn());
        boolean isUnique = foundCustomer.isEmpty();

        if (!isUnique) {
            return false;
        }

        return switch (createCustomerRequest.getCustomerType()) {
            case BIREYSEL -> createCustomerRequest.getName() != null && createCustomerRequest.getSurname() != null && createCustomerRequest.getTcKimlikNo() != null
            && createCustomerRequest.getTcKimlikNo().length() == 11 && !createCustomerRequest.getName().isEmpty() && !createCustomerRequest.getSurname().isEmpty();
            case KURUMSAL -> createCustomerRequest.getCompanyName() != null && createCustomerRequest.getVkn() != null
            && !createCustomerRequest.getCompanyName().isEmpty() && !createCustomerRequest.getVkn().isEmpty();
            default -> false;
        };
    }
}
