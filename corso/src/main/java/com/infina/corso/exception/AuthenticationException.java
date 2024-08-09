package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NETWORK_AUTHENTICATION_REQUIRED)
public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super(Messages.getMessageForLocale("corso.error.authentication", LocaleContextHolder.getLocale()));
    }
}
