package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Montenegro PIB (Poreski Identifikacioni Broj). 8 digits, weights 8..2.
 * Sample {@code 02655284}.
 */
public final class MePibUtil {

    private static final int[] WEIGHTS = {8, 7, 6, 5, 4, 3, 2};

    private MePibUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{8}")) {
            return false;
        }
        return checkDigit(digits) == digits.charAt(7);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("Montenegro PIB body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + Math.floorMod(-sum, 11) % 10);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 7) {
            digits = digits.substring(0, 7);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("ME")) {
            compact = compact.substring(2);
        }
        return compact.replaceAll("\\D", "");
    }
}
