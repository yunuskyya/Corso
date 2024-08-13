package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountAlreadyActiveException extends RuntimeException {
    public AccountAlreadyActiveException() {
        super(Messages.getMessageForLocale("error.account.already.active", LocaleContextHolder.getLocale()));
    }
}
