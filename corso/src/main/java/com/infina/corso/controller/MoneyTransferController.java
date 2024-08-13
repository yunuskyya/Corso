package com.infina.corso.controller;

import com.infina.corso.dto.request.MoneyTransferRequestForAddMoney;
import com.infina.corso.dto.request.MoneyTransferRequestForList;
import com.infina.corso.dto.response.MoneyTransferResponseForList;
import com.infina.corso.service.MoneyTransferService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/money-transfer")
public class MoneyTransferController {

    private final MoneyTransferService moneyTransferService;

    public MoneyTransferController(MoneyTransferService moneyTransferService) {
        this.moneyTransferService = moneyTransferService;
    }

    @PostMapping
    public void saveTransfer(@RequestBody MoneyTransferRequestForAddMoney transfer) {
        moneyTransferService.saveMoneyTransfer(transfer);
    }

    @PostMapping("/get-filtered")
    public List<MoneyTransferResponseForList> getMoneyTransfers(@RequestBody MoneyTransferRequestForList moneyTransferRequestForList) {
        return moneyTransferService.filterMoneyTransfers(moneyTransferRequestForList);
    }

    @GetMapping("/get-all")
    public List<MoneyTransferResponseForList> getAll(){
        return moneyTransferService.collectAllMoneyTransfers();
    }

}
