package com.infina.corso.service;

import com.infina.corso.dto.request.MoneyTransferRequestForAddMoney;
import com.infina.corso.dto.response.MoneyTransferResponse;

import java.util.List;

public interface MoneyTransferService {
    void saveMoneyTransfer(MoneyTransferRequestForAddMoney moneyTransfer);
    List<MoneyTransferResponse> collectAllMoneyTransfers();

}
