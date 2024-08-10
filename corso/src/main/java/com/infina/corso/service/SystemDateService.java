package com.infina.corso.service;

import com.infina.corso.model.SystemDate;

import java.time.LocalDate;

public interface SystemDateService {
    LocalDate getSystemDate();
    void closeDay();
    boolean isDayClosed();
    boolean isDayClosedStarted();
}
