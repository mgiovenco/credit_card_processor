package com.cardprocessor.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Data
public class CreditCardFileProcessingReport {
    private String fileName;
    private Date processingStartDate;
    private Date processingEndDate;
    private Map<String, BigDecimal> transactionSummary;
    private CreditCardFileProcessingStatus status;
    private String message;

    public CreditCardFileProcessingReport(String fileName) {
        this.fileName = fileName;
        this.transactionSummary = new TreeMap<>();
    }

    public void startProcessing() {
        this.setProcessingStartDate(new Date());
        this.setStatus(CreditCardFileProcessingStatus.STARTED);
    }

    public void stopProcessing(CreditCardFileProcessingStatus status, String message) {
        this.setProcessingEndDate(new Date());
        this.setStatus(status);
        this.setMessage(message);
    }

    public void updateTransactionSummary(String name, BigDecimal amount, String failure) {
        BigDecimal existing = this.getTransactionSummary().get(name);

        if (existing == null) {
            if (failure != null) {
                // Null represents a user with a failure
                this.getTransactionSummary().put(name, null);
            } else {
                this.getTransactionSummary().put(name, amount);
            }

        } else {
            this.getTransactionSummary().put(name, existing.add(amount));
        }
    }

    public void outputTransactionSummaryOutput() {
        for (Map.Entry<String, BigDecimal> pair : transactionSummary.entrySet()) {
            System.out.println(pair.getKey() + ": " + (pair.getValue() == null ? "error " : "$" + pair.getValue().toPlainString()));
        }
    }

}
