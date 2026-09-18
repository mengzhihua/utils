package com.mengzhihua.utils.common.validate;


/**
 * Argentine CUIT / CUIL. Weights {@code 5,4,3,2,7,6,5,4,3,2};
 * check {@code 11 - sum % 11} ({@code 11→0}, {@code 10→9}).
 * Sample {@code 20-12345678-6}.
 */
public final class CuitUtil {

    private static final int[] WEIGHTS = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    private CuitUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{11}") && checkDigit(digits.substring(0, 10)) == digits.charAt(10);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("CUIT body must be 10 digits");
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = 11 - (sum % 11);
        if (rem == 11) {
            return '0';
        }
        if (rem == 10) {
            return '9';
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
