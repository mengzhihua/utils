package com.mengzhihua.utils.common.validate;


/**
 * Finnish Y-tunnus. Weights {@code 7,9,10,5,8,4,2}; remainder {@code 1} unused.
 * Sample {@code 1234567-1}.
 */
public final class YTunnusUtil {

    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2};

    private YTunnusUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{8}") && checkDigit(digits.substring(0, 7)) == digits.charAt(7);
    }

    public static char checkDigit(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("Y-tunnus body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        if (rem == 1) {
            throw new IllegalArgumentException("Y-tunnus body has no valid check digit");
        }
        return (char) ('0' + (rem == 0 ? 0 : 11 - rem));
    }

    public static String complete(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
