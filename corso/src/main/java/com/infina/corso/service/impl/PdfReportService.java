package com.infina.corso.service.impl;
import com.infina.corso.dto.response.TransactionResponse;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;
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

        Table table = new Table(new float[]{1, 1, 2, 2, 2, 3});
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
            table.addCell(transaction.getTransactionDate().toString());
        }

        document.add(table);
        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}