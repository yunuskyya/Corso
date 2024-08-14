package com.infina.corso.service;

import com.infina.corso.dto.request.MoneyTransferRequestForAddMoney;
import com.infina.corso.dto.request.MoneyTransferRequestForList;
import com.infina.corso.dto.response.MoneyTransferResponseForList;

import java.util.List;

public interface MoneyTransferService {
    void saveMoneyTransfer(MoneyTransferRequestForAddMoney moneyTransfer);
    List<MoneyTransferResponseForList> collectAllMoneyTransfers();
    List<MoneyTransferResponseForList> filterMoneyTransfers(MoneyTransferRequestForList moneyTransferRequestForList);
    List<MoneyTransferResponseForList> collectMoneyTransfersForEndOfDay();

}
