package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SystemDateNotFoundException extends RuntimeException {

    public SystemDateNotFoundException() {
        super(Messages.getMessageForLocale("corso.error.system.date.not.found", LocaleContextHolder.getLocale()));
    }
}
