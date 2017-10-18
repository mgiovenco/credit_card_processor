package com.cardprocessor.service;

import com.cardprocessor.dao.CreditCardDao;
import com.cardprocessor.domain.CreditCard;
import com.cardprocessor.domain.CreditCardTransaction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class CreditCardServiceTest {

    @Mock
    CreditCardValidator creditCardValidator;

    @Mock
    CreditCardDao creditCardDao;

    CreditCardService creditCardService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        creditCardService = new CreditCardService(creditCardValidator, creditCardDao);
    }

    @Test
    public void add_success_true() throws Exception {
        Mockito.when(creditCardValidator.validate(any(String.class))).thenReturn(true);

        boolean result = creditCardService.add("Mike", "4111111111111111", new BigDecimal(1000));

        verify(creditCardDao, times(1)).addCreditCard(any(CreditCard.class));

        assertEquals("Result should be true", true, result);

    }

    @Test
    public void add_success_false() throws Exception {
        Mockito.when(creditCardValidator.validate(any(String.class))).thenReturn(false);

        boolean result = creditCardService.add("Mike", "4111111111111111", new BigDecimal(1000));

        verify(creditCardDao, never()).addCreditCard(any(CreditCard.class));

        assertEquals("Result should be false", false, result);
    }

    @Test
    public void charge_success_true() throws Exception {
        CreditCard creditCard = CreditCard.builder()
                .balance(new BigDecimal(100))
                .limit(new BigDecimal(200))
                .build();

        Mockito.when(creditCardDao.getCreditCardByName(any(String.class))).thenReturn(creditCard);

        boolean result = creditCardService.charge("Mike", new BigDecimal(50));

        verify(creditCardDao, times(1)).addCreditCardTransation(any(CreditCardTransaction.class), any(CreditCard.class));

        assertEquals("Result should be true", true, result);
    }

    @Test
    public void charge_success_false() throws Exception {
        CreditCard creditCard = CreditCard.builder()
                .balance(new BigDecimal(100))
                .limit(new BigDecimal(200))
                .build();

        Mockito.when(creditCardDao.getCreditCardByName(any(String.class))).thenReturn(creditCard);

        boolean result = creditCardService.charge("Mike", new BigDecimal(500));

        verify(creditCardDao, never()).addCreditCardTransation(any(CreditCardTransaction.class), any(CreditCard.class));

        assertEquals("Result should be false", false, result);
    }

    @Test
    public void credit_success_true() throws Exception {
        CreditCard creditCard = CreditCard.builder()
                .balance(new BigDecimal(100))
                .limit(new BigDecimal(200))
                .build();

        Mockito.when(creditCardDao.getCreditCardByName(any(String.class))).thenReturn(creditCard);

        boolean result = creditCardService.credit("Mike", new BigDecimal(500));

        verify(creditCardDao, times(1)).addCreditCardTransation(any(CreditCardTransaction.class), any(CreditCard.class));

        assertEquals("Result should be true", true, result);
    }

    @Test
    public void credit_success_false() throws Exception {
        Mockito.when(creditCardDao.getCreditCardByName(any(String.class))).thenReturn(null);

        boolean result = creditCardService.credit("Mike", new BigDecimal(500));

        verify(creditCardDao, never()).addCreditCardTransation(any(CreditCardTransaction.class), any(CreditCard.class));

        assertEquals("Result should be false", false, result);
    }
}