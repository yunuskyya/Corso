package com.infina.corso.controller;

import com.infina.corso.dto.response.GetAllAccountForEndOfDayResponse;
import com.infina.corso.dto.response.GetAllCustomerForEndOfDayResponse;
import com.infina.corso.dto.response.MoneyTransferResponseForList;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.repository.SystemDateRepository;
import com.infina.corso.service.MoneyTransferService;
import com.infina.corso.service.TransactionService;
import com.infina.corso.service.impl.AccountServiceImp;
import com.infina.corso.service.impl.CustomerServiceImpl;
import com.infina.corso.service.impl.ExcelReportService;
import com.infina.corso.service.impl.PdfReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {


    private final ExcelReportService excelReportService;
    private final PdfReportService pdfReportService;
    private final TransactionService transactionService;
    private final MoneyTransferService moneyTransferService;
    private final SystemDateRepository systemDateRepository;
    private final AccountServiceImp accountServiceImp;
    private final CustomerServiceImpl customerServiceImpl;


    public ReportController(ExcelReportService excelReportService, PdfReportService pdfReportService, TransactionService transactionService, MoneyTransferService moneyTransferService, SystemDateRepository systemDateRepository, AccountServiceImp accountServiceImp, CustomerServiceImpl customerServiceImpl) {
        this.excelReportService = excelReportService;
        this.pdfReportService = pdfReportService;
        this.transactionService = transactionService;
        this.moneyTransferService = moneyTransferService;
        this.systemDateRepository = systemDateRepository;
        this.accountServiceImp = accountServiceImp;
        this.customerServiceImpl = customerServiceImpl;
    }



    @GetMapping("/export-transactions/excel/{userId}")
    public ResponseEntity<byte[]> exportToExcel(@RequestParam(required = false) Integer userId) throws IOException {
        List<TransactionResponse> transactions = (userId != null)
                ? transactionService.collectTransactionsForSelectedUser(userId)
                : transactionService.collectAllTransactions();

        ByteArrayInputStream in = excelReportService.exportTransactionsToExcel(transactions);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transactions.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }

    @GetMapping("/export-transactions/pdf/{userId}")
    public ResponseEntity<byte[]> exportToPdf(@RequestParam(required = false) Integer userId) {
        List<TransactionResponse> transactions = (userId != null)
                ? transactionService.collectTransactionsForSelectedUser(userId)
                : transactionService.collectAllTransactions();

        ByteArrayInputStream in = pdfReportService.exportTransactionsToPdf(transactions);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transactions.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }
    //************************************* GUN SONU ÇIKTILARI *********************************************************************
    //*********TRANSACTION*********************
    // GÜN SONU TRANSACTION PDF
    @GetMapping("/export-transactions/pdf")
    public ResponseEntity<byte[]> exportToPdf() {
        List<TransactionResponse> transactions = transactionService.collectAllTransactionForDayClose();
        ByteArrayInputStream in = pdfReportService.exportTransactionsToPdf(transactions);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transactions.pdf");
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }

    // GÜN SONU TRANSACTION EXCEL

    @GetMapping("/export-transactions/excel")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<TransactionResponse> transactions = transactionService.collectAllTransactionForDayClose();
        ByteArrayInputStream in = excelReportService.exportTransactionsToExcel(transactions);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=transactions.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }

    //*************NAKİT AKIŞ*********************

    // GUN SONU NAKIT AKIS EXCEL
    @GetMapping("/export-money-transfers/excel")
    public ResponseEntity<byte[]> exportMoneyTransfersToExcel() throws IOException {
        List<MoneyTransferResponseForList> moneyTransfers = moneyTransferService.collectMoneyTransfersForEndOfDay();

        ByteArrayInputStream in = excelReportService.exportMoneyTransfersToExcel(moneyTransfers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=money_transfers.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }
    // GUN SONU NAKIT AKIS PDF
    @GetMapping("/export-money-transfers/pdf")
    public ResponseEntity<byte[]> exportMoneyTransfersToPdf() {
        List<MoneyTransferResponseForList> moneyTransfers = moneyTransferService.collectMoneyTransfersForEndOfDay();

        ByteArrayInputStream in = pdfReportService.exportMoneyTransfersToPdf(moneyTransfers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=money_transfers.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }

    //*************ACCOUNT*********************

    //GUN SONU ACCOUNT PDF
    @GetMapping("/export-accounts/pdf")
    public ResponseEntity<byte[]> exportAccountsToPdf() {
        List< GetAllAccountForEndOfDayResponse> accounts = accountServiceImp.getAllAccountsforEndOfDay();
        ByteArrayInputStream in = pdfReportService.exportAccountsToPdf(accounts);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=accounts.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }

    //GUN SONU ACCOUNT EXCEL
    @GetMapping("/export-accounts/excel")
    public ResponseEntity<byte[]> exportAccountsToExcel() throws IOException {
        List< GetAllAccountForEndOfDayResponse> accounts = accountServiceImp.getAllAccountsforEndOfDay();
        ByteArrayInputStream in = excelReportService.exportAccountsToExcel(accounts);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=accounts.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }

    //*************CUSTOMERS******************************

    //GUN SONU CUSTOMER PDF
    @GetMapping("/export-customers/pdf")
    public ResponseEntity<byte[]> exportCustomersToPdf() {
        List< GetAllCustomerForEndOfDayResponse> customers = customerServiceImpl.getAllCustomersForEndOfDay();
        ByteArrayInputStream in = pdfReportService.exportCustomersToPdf(customers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }

    //GUN SONU CUSTOMER EXCEL
    @GetMapping("/export-customers/excel")
    public ResponseEntity<byte[]> exportCustomersToExcel() throws IOException {
        List< GetAllCustomerForEndOfDayResponse> customers = customerServiceImpl.getAllCustomersForEndOfDay();
        ByteArrayInputStream in = excelReportService.exportCustomersToExcel(customers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }

}
