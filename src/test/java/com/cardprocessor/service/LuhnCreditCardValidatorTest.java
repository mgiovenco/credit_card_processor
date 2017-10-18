package com.cardprocessor.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LuhnCreditCardValidatorTest {

    private CreditCardValidator creditCardValidator;

    @Before
    public void setUp() throws Exception {
        creditCardValidator = new LuhnCreditCardValidator();
    }

    @Test
    public void validate() throws Exception {
        assertTrue(creditCardValidator.validate("4111111111111111"));
        assertTrue(creditCardValidator.validate("5454545454545454"));
        assertFalse(creditCardValidator.validate("1234567890123456"));
    }

}