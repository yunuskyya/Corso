package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String code) {
        super(Messages.getMessageForLocale("error.currency.not.found", LocaleContextHolder.getLocale()));
    }
}
