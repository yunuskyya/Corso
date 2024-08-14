package com.infina.corso.service;

import com.infina.corso.model.SystemDate;

import java.time.LocalDate;

public interface SystemDateService {
    LocalDate getSystemDate();
    void closeDay(LocalDate date);
    void startCloseDay();
    boolean isDayClosedStarted();
    void initializeSystemDate();
    SystemDate getFullDateData();
}
