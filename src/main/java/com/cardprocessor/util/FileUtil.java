package com.cardprocessor.util;

public class FileUtil {
    public static String removeCurrencyPrefix(String amount) {
        return amount.replace("$", "");
    }
}
