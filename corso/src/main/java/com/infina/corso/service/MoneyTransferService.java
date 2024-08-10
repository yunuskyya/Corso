package com.infina.corso.service;

import com.infina.corso.dto.request.MoneyTransferRequest;

public interface MoneyTransferService {
    void saveMoneyTransfer(MoneyTransferRequest moneyTransfer);
}
