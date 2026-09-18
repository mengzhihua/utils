package com.mengzhihua.utils.common.validate;


/**
 * Lithuanian juridinio asmens kodas (9 digits, 8th is {@code 1}).
 * Weights {@code 1..8}; remainder {@code 10} unused. Sample {@code 119511515}.
 */
public final class LtJaUtil {

    private static final int[] WEIGHTS = {1, 2, 3, 4, 5, 6, 7, 8};

    private LtJaUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{7}1\\d") && checkDigit(digits.substring(0, 8)) == digits.charAt(8);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("JA body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        if (rem == 10) {
            throw new IllegalArgumentException("JA body has no valid check digit");
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
