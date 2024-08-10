package com.infina.corso.service.impl;

import com.infina.corso.dto.response.MoneyTransferResponse;
import com.infina.corso.dto.response.TransactionResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelReportService {

    public ByteArrayInputStream exportTransactionsToExcel(List<TransactionResponse> transactions) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("User ID");
        headerRow.createCell(1).setCellValue("Purchased Currency");
        headerRow.createCell(2).setCellValue("Sold Currency");
        headerRow.createCell(3).setCellValue("Amount");
        headerRow.createCell(4).setCellValue("Transaction Date");

        int rowNum = 1;
        for (TransactionResponse transaction : transactions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(transaction.getUser_id());
            row.createCell(1).setCellValue(transaction.getPurchasedCurrency());
            row.createCell(2).setCellValue(transaction.getSoldCurrency());
            row.createCell(3).setCellValue(transaction.getAmount());
            row.createCell(4).setCellValue(transaction.getTransactionSystemDate().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportMoneyTransfersToExcel(List<MoneyTransferResponse> moneyTransfers) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Money Transfers");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("IBAN No");
        headerRow.createCell(1).setCellValue("Amount");
        headerRow.createCell(2).setCellValue("Receiver");
        headerRow.createCell(3).setCellValue("Sender");
        headerRow.createCell(4).setCellValue("System Date");

        int rowNum = 1;
        for (MoneyTransferResponse moneyTransfer : moneyTransfers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(moneyTransfer.getIbanNo());
            row.createCell(1).setCellValue(moneyTransfer.getAmount());
            row.createCell(2).setCellValue(moneyTransfer.getReceiver());
            row.createCell(3).setCellValue(moneyTransfer.getSender());
            row.createCell(4).setCellValue(moneyTransfer.getSystemDate().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}