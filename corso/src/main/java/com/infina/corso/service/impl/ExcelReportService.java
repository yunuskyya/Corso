package com.infina.corso.service.impl;

import com.infina.corso.dto.response.GetAllAccountForEndOfDayResponse;
import com.infina.corso.dto.response.GetAllCustomerForEndOfDayResponse;
import com.infina.corso.dto.response.MoneyTransferResponseForList;
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
        Sheet sheet = workbook.createSheet("İşlem Listesi");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Müşteri");
        headerRow.createCell(1).setCellValue("Alınan Döviz");
        headerRow.createCell(2).setCellValue("Satılan Döviz");
        headerRow.createCell(3).setCellValue("Miktar");
        headerRow.createCell(4).setCellValue("Gerçekleşen Kur Fiyatı");
        headerRow.createCell(5).setCellValue("Maliyet");
        headerRow.createCell(6).setCellValue("Tarih");

        int rowNum = 1;
        for (TransactionResponse transaction : transactions) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(transaction.getName()+ " "+ transaction.getSurname());
            row.createCell(1).setCellValue(transaction.getPurchasedCurrency());
            row.createCell(2).setCellValue(transaction.getSoldCurrency());
            row.createCell(3).setCellValue(transaction.getAmount());
            row.createCell(4).setCellValue(transaction.getRate());
            row.createCell(5).setCellValue(transaction.getCost().toString());
            row.createCell(6).setCellValue(transaction.getTransactionSystemDate().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportMoneyTransfersToExcel(List<MoneyTransferResponseForList> moneyTransfers) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Nakit Akış Listesi");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Müsteri");
        headerRow.createCell(1).setCellValue("Adet");
        headerRow.createCell(2).setCellValue("Alan Hesap");
        headerRow.createCell(3).setCellValue("Yollayan Hesap");
        headerRow.createCell(4).setCellValue("Tarih");

        int rowNum = 1;
        for (MoneyTransferResponseForList moneyTransfer : moneyTransfers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(moneyTransfer.getCustomerNameSurname());
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

    public ByteArrayInputStream exportAccountsToExcel(List<GetAllAccountForEndOfDayResponse> accounts) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Hesap Listesi");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Tarih");
        headerRow.createCell(1).setCellValue("Döviz Tipi");
        headerRow.createCell(2).setCellValue("Müşteri");
        headerRow.createCell(3).setCellValue("Hesap No");
        headerRow.createCell(4).setCellValue("Bakiye");

        int rowNum = 1;
        for (GetAllAccountForEndOfDayResponse account : accounts) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(account.getDate());
            row.createCell(1).setCellValue(account.getCurrency());
            row.createCell(2).setCellValue(account.getCustomerNameSurname());
            row.createCell(3).setCellValue(account.getAccountNumber());
            row.createCell(4).setCellValue(account.getBalance().doubleValue());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportCustomersToExcel(List<GetAllCustomerForEndOfDayResponse> customers) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Müşteri Listesi");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Müşteri Adı Soyadı");
        headerRow.createCell(1).setCellValue("TC Kimlik No");
        headerRow.createCell(2).setCellValue("Müşteri Tipi");
        headerRow.createCell(3).setCellValue("Şirket Adı");
        headerRow.createCell(4).setCellValue("VKN");

        int rowNum = 1;
        for (GetAllCustomerForEndOfDayResponse customer : customers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(customer.getCustomerNameSurname());
            row.createCell(1).setCellValue(customer.getTcKimlikNo());
            row.createCell(2).setCellValue(customer.getCustomerType().toString());
            row.createCell(3).setCellValue(customer.getCompanyName());
            row.createCell(4).setCellValue(customer.getVkn());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

}