package com.mengzhihua.utils.common.validate;


/**
 * Mozambique NUIT (Número Único de Identificação Tributária). 9 digits.
 * Sample {@code 400339910} / {@code 400 005 834}.
 */
public final class MzNuitUtil {

    private static final int[] WEIGHTS = {8, 9, 4, 5, 6, 7, 8, 9};
    private static final String CHECKS = "01234567891";

    private MzNuitUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{9}")) {
            return false;
        }
        return checkDigit(digits) == digits.charAt(8);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("Mozambique NUIT body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return CHECKS.charAt(sum % 11);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 3) + ' ' + digits.substring(3, 6) + ' ' + digits.substring(6);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
