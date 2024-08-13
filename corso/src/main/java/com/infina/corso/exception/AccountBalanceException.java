package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AccountBalanceException extends RuntimeException {
    public AccountBalanceException() {
        super(Messages.getMessageForLocale("error.account.has.balance.cannot.delete", LocaleContextHolder.getLocale()));
    }
}
