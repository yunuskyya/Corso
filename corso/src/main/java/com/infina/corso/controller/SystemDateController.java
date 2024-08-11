package com.infina.corso.controller;


import com.infina.corso.service.SystemDateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/system-date")
public class SystemDateController {

    private final SystemDateService systemDateService;

    public SystemDateController(SystemDateService systemDateService) {
        this.systemDateService = systemDateService;
    }

    @GetMapping("/close-day")
    public void closeDay(){
        systemDateService.closeDay();
    }

    @GetMapping("/start-close-day")
    public void startCloseDayProcess (){
        systemDateService.startCloseDay();
    }

}
