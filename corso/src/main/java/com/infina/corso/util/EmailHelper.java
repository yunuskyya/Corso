package com.infina.corso.util;

import com.infina.corso.model.User;
import com.infina.corso.service.MailService;
import org.springframework.stereotype.Component;

@Component
public class EmailHelper {

    private final MailService mailService;

    public EmailHelper(MailService mailService) {
        this.mailService = mailService;
    }

}
