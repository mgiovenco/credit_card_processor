package com.cardprocessor.service;

import com.cardprocessor.domain.CreditCardActionType;
import com.cardprocessor.domain.CreditCardFileProcessingReport;
import com.cardprocessor.domain.CreditCardFileProcessingStatus;
import com.cardprocessor.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Slf4j
@Service
public class TextFileProcessingService {

    private final CreditCardService creditCardService;

    @Autowired
    public TextFileProcessingService(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    public CreditCardFileProcessingReport processFile(String fileName) throws IOException {

        CreditCardFileProcessingReport report = new CreditCardFileProcessingReport(fileName);
        report.startProcessing();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineFields = line.split(" ");
                String actionType = lineFields[0].toUpperCase();
                String name = lineFields[1];

                if (CreditCardActionType.ADD.toString().equals(actionType)) {
                    if (creditCardService.add(name, lineFields[2], new BigDecimal(FileUtil.removeCurrencyPrefix(lineFields[3])))) {
                        report.updateTransactionSummary(name, BigDecimal.ZERO, null);
                    } else {
                        report.updateTransactionSummary(name, null, "error");
                    }
                } else if (CreditCardActionType.CHARGE.toString().equals(actionType)) {
                    if (creditCardService.charge(name, new BigDecimal(FileUtil.removeCurrencyPrefix(lineFields[2])))) {
                        report.updateTransactionSummary(lineFields[1], new BigDecimal(FileUtil.removeCurrencyPrefix(lineFields[2])), null);
                    }
                } else if (CreditCardActionType.CREDIT.toString().equals(actionType)) {
                    if (creditCardService.credit(name, new BigDecimal(FileUtil.removeCurrencyPrefix(lineFields[2])).negate())) {
                        report.updateTransactionSummary(lineFields[1], new BigDecimal(FileUtil.removeCurrencyPrefix(lineFields[2])).negate(), null);
                    }
                }
            }
            report.stopProcessing(CreditCardFileProcessingStatus.COMPLETED, null);
        } catch (Exception e) {
            String message = "The following exception occurred while processing the file: " + e.getMessage();
            log.error(message);
            report.stopProcessing(CreditCardFileProcessingStatus.FAILED, message);
        }

        return report;
    }

}