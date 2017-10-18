package com.cardprocessor.service;

import com.cardprocessor.dao.CreditCardDao;
import com.cardprocessor.domain.CreditCard;
import com.cardprocessor.domain.CreditCardActionType;
import com.cardprocessor.domain.CreditCardTransaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Service
public class CreditCardService {

    private final CreditCardValidator creditCardValidator;
    private final CreditCardDao creditCardDao;

    @Autowired
    public CreditCardService(CreditCardValidator creditCardValidator, CreditCardDao creditCardDao) {
        this.creditCardValidator = creditCardValidator;
        this.creditCardDao = creditCardDao;
    }

    public boolean add(String name, String cardNumber, BigDecimal limit) {
        boolean isValid = creditCardValidator.validate(cardNumber);

        if (isValid) {
            CreditCard creditCard = CreditCard.builder()
                    .name(name)
                    .cardNumber(cardNumber)
                    .limit(limit)
                    .balance(BigDecimal.ZERO)
                    .build();
            creditCardDao.addCreditCard(creditCard);
        } else {
            log.warn("Card is invalid for name={}", name);
            return false;
        }

        return true;
    }

    public boolean charge(String name, BigDecimal amount) {
        CreditCard creditCard = creditCardDao.getCreditCardByName(name);

        if (creditCard != null) {
            if (creditCard.getBalance().add(amount).compareTo(creditCard.getLimit()) < 1) {
                CreditCardTransaction creditCardTransaction = CreditCardTransaction.builder()
                        .name(creditCard.getName())
                        .cardNumber(creditCard.getCardNumber())
                        .amount(amount)
                        .transactionDate(new Date())
                        .type(CreditCardActionType.CHARGE)
                        .build();

                creditCardDao.addCreditCardTransation(creditCardTransaction, creditCard);
                return true;
            } else {
                log.warn("Charges would raise balance over limit for customer name={}.  Current limit={}, balance={}, charge={}",
                        name, creditCard.getLimit(), creditCard.getBalance(), amount);
            }
        } else {
            log.warn("Card not found for name={}", name);
        }

        return false;
    }

    public boolean credit(String name, BigDecimal amount) {
        CreditCard creditCard = creditCardDao.getCreditCardByName(name);

        if (creditCard != null) {
            CreditCardTransaction creditCardTransaction = CreditCardTransaction.builder()
                    .name(creditCard.getName())
                    .cardNumber(creditCard.getCardNumber())
                    .amount(amount)
                    .transactionDate(new Date())
                    .type(CreditCardActionType.CREDIT)
                    .build();

            creditCardDao.addCreditCardTransation(creditCardTransaction, creditCard);
        } else {
            log.warn("Card not found for name={}", name);
            return false;
        }

        return true;
    }

}
