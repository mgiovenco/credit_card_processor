package com.cardprocessor.service;

import com.cardprocessor.domain.CreditCardFileProcessingReport;
import com.cardprocessor.domain.CreditCardFileProcessingStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.MockitoAnnotations.initMocks;

public class TextFileProcessingServiceTest {

    @Mock
    CreditCardService creditCardService;

    TextFileProcessingService textFileProcessingService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        textFileProcessingService = new TextFileProcessingService(creditCardService);

        // Create file for testing
        File file = new File("sample.txt");
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write("Add Tom 4111111111111111 $1000\n");
        writer.write("Add Lisa 5454545454545454 $3000\n");
        writer.write("Charge Tom $500\n");
        writer.write("Credit Lisa $100\n");
        writer.close();
    }

    @Test
    public void processFile_success() throws Exception {
        Mockito.when(creditCardService.add(any(String.class), any(String.class), any(BigDecimal.class))).thenReturn(true);
        Mockito.when(creditCardService.charge(any(String.class), any(BigDecimal.class))).thenReturn(true);
        Mockito.when(creditCardService.credit(any(String.class), any(BigDecimal.class))).thenReturn(true);

        CreditCardFileProcessingReport report = textFileProcessingService.processFile("sample.txt");

        assertEquals("Report should be completed", CreditCardFileProcessingStatus.COMPLETED, report.getStatus());
        assertNotNull(report.getProcessingEndDate());
        assertEquals("sample.txt", report.getFileName());
        assertNotNull(report.getTransactionSummary());
    }

    @Test
    public void processFile_failure() throws Exception {
        Mockito.when(creditCardService.add(any(String.class), any(String.class), any(BigDecimal.class))).thenReturn(true);
        Mockito.when(creditCardService.charge(any(String.class), any(BigDecimal.class))).thenReturn(true);
        Mockito.when(creditCardService.credit(any(String.class), any(BigDecimal.class))).thenReturn(true);

        CreditCardFileProcessingReport report = textFileProcessingService.processFile("missingFile.txt");

        assertEquals("Report should have failed", CreditCardFileProcessingStatus.FAILED, report.getStatus());
        assertNotNull(report.getProcessingEndDate());
        assertEquals("missingFile.txt", report.getFileName());
    }

}