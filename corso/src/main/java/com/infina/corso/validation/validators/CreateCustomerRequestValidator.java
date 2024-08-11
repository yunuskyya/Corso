package com.infina.corso.validation.validators;

import com.infina.corso.dto.request.CreateCustomerRequest;
import com.infina.corso.validation.annotations.ValidCreateCustomerRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateCustomerRequestValidator implements ConstraintValidator<ValidCreateCustomerRequest, CreateCustomerRequest> {
    @Override
    public boolean isValid(CreateCustomerRequest createCustomerRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (createCustomerRequest.getCustomerType() == null) {
            return false;
        }

        return switch (createCustomerRequest.getCustomerType()) {
            case BIREYSEL -> createCustomerRequest.getName() != null && createCustomerRequest.getSurname() != null && createCustomerRequest.getTcKimlikNo() != null;
            case KURUMSAL -> createCustomerRequest.getCompanyName() != null && createCustomerRequest.getVkn() != null;
            default -> false;
        };
    }
}
