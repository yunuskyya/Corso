package com.infina.corso.service;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}
