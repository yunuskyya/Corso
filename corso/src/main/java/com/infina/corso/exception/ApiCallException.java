package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class ApiCallException extends RuntimeException {
    public ApiCallException() {
        super(Messages.getMessageForLocale("error.api.call.failed", LocaleContextHolder.getLocale()));
    }
}
