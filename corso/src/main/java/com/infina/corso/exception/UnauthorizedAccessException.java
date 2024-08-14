package com.infina.corso.exception;
import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException() {
        super(Messages.getMessageForLocale("corso.error.unauthorized.access", LocaleContextHolder.getLocale()));
    }
}