package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String accountNumber) {
        super(Messages.getMessageForLocale("corso.error.insufficient.funds", LocaleContextHolder.getLocale()) + " Account: " + accountNumber);
    }
}

