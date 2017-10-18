package com.cardprocessor.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@Data
public class CreditCardTransaction {
    private String name;
    private String cardNumber;
    private CreditCardActionType type;
    private BigDecimal amount;
    private Date transactionDate;
}
