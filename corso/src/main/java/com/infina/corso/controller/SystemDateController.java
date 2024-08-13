package com.infina.corso.controller;


import com.infina.corso.service.SystemDateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/system-date")
public class SystemDateController {

    private final SystemDateService systemDateService;

    public SystemDateController(SystemDateService systemDateService) {
        this.systemDateService = systemDateService;
    }

    @PostMapping("/close-day")
    public void closeDay(LocalDate date){
        systemDateService.closeDay(date);
    }

    @GetMapping("/start-close-day")
    public void startCloseDayProcess (){
        systemDateService.startCloseDay();
    }

}
