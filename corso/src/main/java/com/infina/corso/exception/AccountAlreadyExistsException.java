package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(String accountNumber) {
        super(Messages.getMessageForLocale("corso.account.alreadyexists.error.message",
                LocaleContextHolder.getLocale(), accountNumber));
    }
}
