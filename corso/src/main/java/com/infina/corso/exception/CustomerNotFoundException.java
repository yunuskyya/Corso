package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(Long customerId) {
        super(Messages.getMessageForLocale("error.customer.not.found.with.id", LocaleContextHolder.getLocale(), customerId));
    }
}
