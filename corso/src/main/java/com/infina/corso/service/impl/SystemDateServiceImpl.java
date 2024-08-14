package com.infina.corso.service.impl;

import com.infina.corso.exception.SystemDateNotFoundException;
import com.infina.corso.model.SystemDate;
import com.infina.corso.repository.SystemDateRepository;
import com.infina.corso.service.SystemDateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    @Override
    public void initializeSystemDate() {
        SystemDate systemDate = systemDateRepository.findById(1).orElseGet(() -> {
            SystemDate newDate = new SystemDate();
            newDate.setId(1);
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

    @Override
    public SystemDate getFullDateData() {
        return systemDateRepository.findById(1).orElseThrow(() -> new IllegalStateException("Sistem tarihi bulunamadı."));
    }

    private SystemDate advanceDateByOneDay(SystemDate systemDate){
        LocalDate date = systemDate.getDate().plusDays(1);
        systemDate.setDate(date);
        return systemDate;
    }

    public void startCloseDay(){
        SystemDate systemDate = systemDateRepository.findById(1).orElseThrow(() -> new  SystemDateNotFoundException());
        systemDate.setDayClosedStarted(true);
        systemDateRepository.save(systemDate);
    }



    public void closeDay(LocalDate date) {
        try {
            SystemDate systemDate = systemDateRepository.findById(1).orElseThrow(() -> new SystemDateNotFoundException());
            systemDate.setDate(date);
            systemDate.setDayClosedStarted(false);
            systemDateRepository.save(systemDate);
        } catch (SystemDateNotFoundException e) {
            // Genel bir hata yakalama
            System.out.println("Hata: " + e.getMessage());
        }
    }



   /* public void closeDay(LocalDate date) {
        try {
            SystemDate systemDate = systemDateRepository.findById(1).orElseThrow(() -> new IllegalStateException("Sistem tarihi bulunamadı."));
            // Tarih farkını kontrol et
            LocalDate today = LocalDate.now();
            LocalDate systemDateValue = systemDate.getDate();
            if (today.isBefore(systemDateValue) || today.isAfter(systemDateValue.plusDays(1))) {
                System.out.println("Hata: Sistem tarihi ile gerçek tarih arasında en fazla 1 gün fark olabilir.");
                return;  // Metodun devamını çalıştırmadan sonlandır
            }
            // Eğer gün kapatılmadıysa, kapat ve bir gün ilerlet
            if (!systemDate.isDayClosed() && isDayClosedStarted()) {
                systemDate.setDayClosed(true);
                systemDate.setDayClosedStarted(false);
                systemDateRepository.save(advanceDateByOneDay(systemDate));
                System.out.println("Gün başarıyla kapatıldı ve tarih bir gün ileri alındı.");
            } else {
                System.out.println("Hata: Gün zaten kapatılmış veya gün kapatma işlemi başlatılmadan gün kapatılmaya çalışılıyor.");
            }
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
