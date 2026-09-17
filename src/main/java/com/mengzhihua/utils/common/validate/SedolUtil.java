package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * SEDOL check digit (Apache Commons Validator / LSE).
 */
public final class SedolUtil {

    private static final int[] WEIGHTS = {1, 3, 1, 3, 1, 3};

    private SedolUtil() {
    }

    public static boolean isValid(String sedol) {
        String compact = normalize(sedol);
        if (compact.length() != 7) {
            return false;
        }
        for (int i = 0; i < 7; i++) {
            if (value(compact.charAt(i)) < 0) {
                return false;
            }
        }
        return checkDigit(compact.substring(0, 6)) == compact.charAt(6);
    }

    public static char checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() != 6) {
            throw new IllegalArgumentException("SEDOL body must be 6 characters");
        }
        int sum = 0;
        for (int i = 0; i < 6; i++) {
            int charValue = value(compact.charAt(i));
            if (charValue < 0) {
                throw new IllegalArgumentException("invalid SEDOL character");
            }
            sum += charValue * WEIGHTS[i];
        }
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }

    public static String normalize(String sedol) {
        return sedol == null ? "" : sedol.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static int value(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        return -1;
    }
}
