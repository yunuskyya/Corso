package com.infina.corso.exception;

import com.infina.corso.shared.Messages;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super(Messages.getMessageForLocale("corso.user.notfound.error.message",
                LocaleContextHolder.getLocale()));
    }

    public UserNotFoundException(String email) {
        super(Messages.getMessageForLocale("corso.user.notfound.error.message",
                LocaleContextHolder.getLocale(), email));
    }
}

