package com.infina.corso.controller;

import com.infina.corso.dto.response.MoneyTransferResponse;
import com.infina.corso.dto.response.TransactionResponse;
import com.infina.corso.service.MoneyTransferService;
import com.infina.corso.service.TransactionService;
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

    public ReportController(ExcelReportService excelReportService, PdfReportService pdfReportService, TransactionService transactionService, MoneyTransferService moneyTransferService) {
        this.excelReportService = excelReportService;
        this.pdfReportService = pdfReportService;
        this.transactionService = transactionService;
        this.moneyTransferService = moneyTransferService;
    }

    @GetMapping("/export-transactions/excel")
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

    @GetMapping("/export-transactions/pdf")
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

    @GetMapping("/export-money-transfers/excel")
    public ResponseEntity<byte[]> exportMoneyTransfersToExcel() throws IOException {
        List<MoneyTransferResponse> moneyTransfers = moneyTransferService.collectAllMoneyTransfers();

        ByteArrayInputStream in = excelReportService.exportMoneyTransfersToExcel(moneyTransfers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=money_transfers.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(in.readAllBytes());
    }

    @GetMapping("/export-money-transfers/pdf")
    public ResponseEntity<byte[]> exportMoneyTransfersToPdf() {
        List<MoneyTransferResponse> moneyTransfers = moneyTransferService.collectAllMoneyTransfers();

        ByteArrayInputStream in = pdfReportService.exportMoneyTransfersToPdf(moneyTransfers);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=money_transfers.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(in.readAllBytes());
    }
}
