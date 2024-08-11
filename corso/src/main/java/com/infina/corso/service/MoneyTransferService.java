package com.infina.corso.service;

import com.infina.corso.dto.request.MoneyTransferRequest;
import com.infina.corso.dto.response.MoneyTransferResponse;

import java.util.List;

public interface MoneyTransferService {
    void saveMoneyTransfer(MoneyTransferRequest moneyTransfer);
    List<MoneyTransferResponse> collectAllMoneyTransfers();

}
