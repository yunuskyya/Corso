package com.infina.corso.validation.annotations;

import com.infina.corso.validation.validators.CustomerUpdateRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomerUpdateRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCustomerUpdateRequest {
    String message() default "Zorunlu alanlar doldurulmalıdır. Bireysel için isim-soyisim-tc, kurumsal için şirket adı ve vkn zorunludur.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
