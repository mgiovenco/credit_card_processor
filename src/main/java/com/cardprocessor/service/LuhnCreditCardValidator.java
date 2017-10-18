package com.cardprocessor.service;

public class LuhnCreditCardValidator implements CreditCardValidator {

    public boolean validate(String numberString) {
        String[] cardNumStringArray = numberString.split("");
        Integer[] cardNumIntegerArray = new Integer[cardNumStringArray.length];

        for (int i = 0; i < cardNumStringArray.length; i++) {
            cardNumIntegerArray[i] = Integer.parseInt(cardNumStringArray[i]);
        }

        int sum = 0;
        int length = cardNumIntegerArray.length;
        for (int i = 0; i < length; i++) {

            // get digits in reverse order
            int digit = cardNumIntegerArray[length - i - 1];

            // every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2;
            }
            sum += digit > 9 ? digit - 9 : digit;
        }
        return sum % 10 == 0;
    }

}
