package com.mengzhihua.utils.common.validate;


/**
 * Moldavian IDNO. 13 digits, weights 7,3,1 repeating, {@code % 10}.
 * Sample {@code 1008600038413}.
 */
public final class IdnoUtil {

    private static final int[] WEIGHTS = {7, 3, 1, 7, 3, 1, 7, 3, 1, 7, 3, 1};

    private IdnoUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{13}") && checkDigit(digits) == digits.charAt(12);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 13) {
            digits = digits.substring(0, 12);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("IDNO body must be 12 digits");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + (sum % 10));
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 12) {
            digits = digits.substring(0, 12);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
