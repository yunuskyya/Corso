package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // BadRequest uygun bir HTTP statüsü olabilir
public class DuplicateIbanException extends RuntimeException {

    public DuplicateIbanException() {
        super(Messages.getMessageForLocale("corso.error.iban.duplicate", LocaleContextHolder.getLocale()));
    }
}
