package com.infina.corso.controller;


import com.infina.corso.model.SystemDate;
import com.infina.corso.service.SystemDateService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("api/v1/system-date")
@CrossOrigin(origins = "*")
public class SystemDateController {

    private final SystemDateService systemDateService;

    public SystemDateController(SystemDateService systemDateService) {
        this.systemDateService = systemDateService;
    }

    @PostMapping("/close-day")
    public void closeDay(@RequestBody Map<String, String> requestBody) {
        String dateString = requestBody.get("date");
        LocalDate date = LocalDate.parse(dateString); // String'i LocalDate'e dönüştür
        systemDateService.closeDay(date);
    }

    @GetMapping("/start-close-day")
    public void startCloseDayProcess (){
        systemDateService.startCloseDay();
    }

    @GetMapping
    public SystemDate getSystemDate(){
        return systemDateService.getFullDateData();
    }

    @GetMapping("/is-day-close-started")
    public boolean isDayCloseStarted(){
        return systemDateService.isDayClosedStarted();
    }

}
