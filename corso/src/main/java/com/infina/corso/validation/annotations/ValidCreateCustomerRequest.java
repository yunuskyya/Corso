package com.infina.corso.validation.annotations;

import com.infina.corso.validation.validators.CreateCustomerRequestValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CreateCustomerRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreateCustomerRequest {
    String message() default "Bireysel için isim-soyisim-tc, kurumsal için şirket adı ve vkn zorunludur. Email-telefon-tc-vkn alanları benzersiz olmalıdır.";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
