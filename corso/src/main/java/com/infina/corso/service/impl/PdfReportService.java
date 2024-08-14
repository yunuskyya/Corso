package com.infina.corso.service.impl;

import com.infina.corso.dto.response.GetAllAccountForEndOfDayResponse;
import com.infina.corso.dto.response.GetAllCustomerForEndOfDayResponse;
import com.infina.corso.dto.response.MoneyTransferResponseForList;
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

        document.add(new Paragraph("İşlem Listesi"));

        Table table = new Table(new float[]{1, 1, 2, 2, 2, 2,2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("Müsteri");
        table.addHeaderCell("Alınan Doviz");
        table.addHeaderCell("Satılan Doviz");
        table.addHeaderCell("Miktar");
        table.addHeaderCell("Gerceklesen Kur Fiyatı");
        table.addHeaderCell("Maliyet");
        table.addHeaderCell("Tarih");

        for (TransactionResponse transaction : transactions) {
            table.addCell(transaction.getName()+" "+transaction.getSurname());
            table.addCell(transaction.getPurchasedCurrency());
            table.addCell(transaction.getSoldCurrency());
            table.addCell(String.valueOf(transaction.getAmount()));
            table.addCell(String.valueOf(transaction.getRate()));
            table.addCell(transaction.getCost().toString());
            table.addCell(transaction.getTransactionSystemDate().toString());
        }
        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportMoneyTransfersToPdf(List<MoneyTransferResponseForList> moneyTransfers) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Para Akış Listesi"));

        Table table = new Table(new float[]{1, 2, 2, 2,2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("Müsteri");
        table.addHeaderCell("Miktar");
        table.addHeaderCell("Alan Hesap");
        table.addHeaderCell("Gonderen Hesap");
        table.addHeaderCell("Tarih");

        for (MoneyTransferResponseForList moneyTransfer : moneyTransfers) {
            table.addCell(moneyTransfer.getCustomerNameSurname());
            table.addCell(String.valueOf(moneyTransfer.getAmount()));
            table.addCell(moneyTransfer.getReceiver());
            table.addCell(moneyTransfer.getSender());
            table.addCell(moneyTransfer.getSystemDate().toString());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportAccountsToPdf(List<GetAllAccountForEndOfDayResponse> accounts) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Hesaplar Listesi"));

        Table table = new Table(new float[]{1, 2, 2, 2,2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("Tarih");
        table.addHeaderCell("Döviz Tipi");
        table.addHeaderCell("Müsteri");
        table.addHeaderCell("Hesap No");
        table.addHeaderCell("Bakiye");

        for (GetAllAccountForEndOfDayResponse account : accounts) {
            table.addCell(String.valueOf(account.getDate()));
            table.addCell(account.getCurrency());
            table.addCell(account.getCustomerNameSurname());
            table.addCell(account.getAccountNumber());
            table.addCell(String.valueOf(account.getBalance()));
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    public ByteArrayInputStream exportCustomersToPdf(List<GetAllCustomerForEndOfDayResponse> customers ) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Müsteri Listesi"));

        Table table = new Table(new float[]{2, 2, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell("Müsteri Adı Soyadı");
        table.addHeaderCell("TC Kimlik No");
        table.addHeaderCell("Müsteri Tipi");
        table.addHeaderCell("Sirket Adı");
        table.addHeaderCell("VKN");

        for (GetAllCustomerForEndOfDayResponse customer : customers) {
            table.addCell(customer.getCustomerNameSurname() != null ? customer.getCustomerNameSurname() : "");
            table.addCell(customer.getTcKimlikNo() != null ? customer.getTcKimlikNo() : "");
            table.addCell(customer.getCustomerType() != null ? customer.getCustomerType().toString() : "");
            table.addCell(customer.getCompanyName() != null ? customer.getCompanyName() : "");
            table.addCell(customer.getVkn() != null ? customer.getVkn() : "");
        }


        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }



}