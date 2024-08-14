package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AccountOwnershipException extends RuntimeException {
    public AccountOwnershipException() {
        super(Messages.getMessageForLocale("error.account.does.not.belong.to.customer", LocaleContextHolder.getLocale()));
    }
}
