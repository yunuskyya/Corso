package com.infina.corso.service.impl;

import com.infina.corso.model.SystemDate;
import com.infina.corso.repository.SystemDateRepository;
import com.infina.corso.service.SystemDateService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SystemDateServiceImpl implements SystemDateService {

    private final SystemDateRepository systemDateRepository;
    private final PdfReportService pdfReportService;

    public SystemDateServiceImpl(SystemDateRepository systemDateRepository, PdfReportService pdfReportService) {
        this.systemDateRepository = systemDateRepository;
        this.pdfReportService = pdfReportService;
    }


    //sistemi ilk defa açtığımızda bir defa tetiklenecek method
    private void initializeSystemDate() {
        SystemDate systemDate = systemDateRepository.findById(1).orElseGet(() -> {
            SystemDate newDate = new SystemDate();
            newDate.setDate(LocalDate.now());// Varsayılan tarih
            return systemDateRepository.save(newDate);
        });
    }

    public boolean isDayClosedStarted(){
        Optional<SystemDate> systemDate = systemDateRepository.findById(1);
        return systemDate.get().isDayClosedStarted();
    }

    public LocalDate getSystemDate() {
        Optional<SystemDate> systemDate = systemDateRepository.findById(1);
        return systemDate.get().getDate();
    }

    private SystemDate advanceDateByOneDay(SystemDate systemDate){
        LocalDate date = systemDate.getDate().plusDays(1);
        systemDate.setDate(date);
        return systemDate;
    }

    public void startCloseDay(){
        SystemDate systemDate = systemDateRepository.findById(1).orElseThrow(() -> new IllegalStateException("Sistem tarihi bulunamadı."));
        systemDate.setDayClosedStarted(true);
        systemDateRepository.save(systemDate);
    }



    public void closeDay(LocalDate date) {
        try {
            SystemDate systemDate = systemDateRepository.findById(1).orElseThrow(() -> new IllegalStateException("Sistem tarihi bulunamadı."));
            systemDate.setDate(date);
            systemDate.setDayClosedStarted(false);
            systemDateRepository.save(systemDate);
        } catch (Exception e) {
            // Genel bir hata yakalama
            System.out.println("Hata: " + e.getMessage());
        }
    }


    /*@Scheduled(cron = "0 0 0 * * ?")
    public void autoCloseDayAtMidnight() {
        SystemDate systemDate = systemDateRepository.findById(1).get();
        if (!systemDate.isDayClosedStarted()) {
            System.out.println("Automatically closing the day at " + LocalDateTime.now());

        }
    } */
}
