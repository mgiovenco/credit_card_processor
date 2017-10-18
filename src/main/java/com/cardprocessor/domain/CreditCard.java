package com.cardprocessor.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CreditCard {
    private String name;
    private String cardNumber;
    private BigDecimal limit;
    private BigDecimal balance;
}
