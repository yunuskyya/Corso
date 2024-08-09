package com.infina.corso.validation.validators;


import com.infina.corso.dto.request.CustomerUpdateRequest;
import com.infina.corso.validation.annotations.ValidCustomerUpdateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomerUpdateRequestValidator implements ConstraintValidator<ValidCustomerUpdateRequest, CustomerUpdateRequest> {

    @Override
    public void initialize(ValidCustomerUpdateRequest constraintAnnotation) {
    }

    @Override
    public boolean isValid(CustomerUpdateRequest request, ConstraintValidatorContext context) {
        if (request.getCustomerType() == null) {
            return false;
        }

        return switch (request.getCustomerType()) {
            case BIREYSEL ->
                    request.getName() != null && request.getSurname() != null && request.getTcKimlikNo() != null;
            case KURUMSAL -> request.getCompanyName() != null && request.getVkn() != null;
            default -> false;
        };
    }
}