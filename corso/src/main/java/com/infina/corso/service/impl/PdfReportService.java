package com.infina.corso.service.impl;

import com.infina.corso.dto.response.MoneyTransferResponse;
import com.infina.corso.dto.response.TransactionResponse;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfReportService {

    public ByteArrayInputStream exportTransactionsToPdf(List<TransactionResponse> transactions) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Transactions"));

        Table table = new Table(new float[]{1, 1, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("User ID");
        table.addHeaderCell("Purchased Currency");
        table.addHeaderCell("Sold Currency");
        table.addHeaderCell("Amount");
        table.addHeaderCell("Transaction Date");

        for (TransactionResponse transaction : transactions) {
            table.addCell(String.valueOf(transaction.getUser_id()));
            table.addCell(transaction.getPurchasedCurrency());
            table.addCell(transaction.getSoldCurrency());
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(transaction.getTransactionSystemDate().toString());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportMoneyTransfersToPdf(List<MoneyTransferResponse> moneyTransfers) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Money Transfers"));

        Table table = new Table(new float[]{1, 2, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("IBAN No");
        table.addHeaderCell("Amount");
        table.addHeaderCell("Receiver");
        table.addHeaderCell("Sender");
        table.addHeaderCell("System Date");

        for (MoneyTransferResponse moneyTransfer : moneyTransfers) {
            table.addCell(moneyTransfer.getIbanNo());
            table.addCell(String.valueOf(moneyTransfer.getAmount()));
            table.addCell(moneyTransfer.getReceiver());
            table.addCell(moneyTransfer.getSender());
            table.addCell(moneyTransfer.getSystemDate().toString());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }


}