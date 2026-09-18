package com.mengzhihua.utils.common.validate;


/**
 * Hungarian adószám. Weights {@code 9,7,3,1,9,7,3} on the first 7 digits.
 * Sample {@code 18154111-2-41}.
 */
public final class AdoszamUtil {

    private static final int[] WEIGHTS = {9, 7, 3, 1, 9, 7, 3};

    private AdoszamUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{8}(?:\\d{3})?")) {
            return false;
        }
        return checkDigit(digits.substring(0, 7)) == digits.charAt(7);
    }

    public static char checkDigit(String body7) {
        String digits = normalize(body7);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("adószám body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }

    public static String complete(String body7) {
        String digits = normalize(body7);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 7);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
