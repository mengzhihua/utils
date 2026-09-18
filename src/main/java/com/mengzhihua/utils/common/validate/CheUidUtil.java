package com.mengzhihua.utils.common.validate;


/**
 * Swiss UID (CHE-123.456.789). Weights {@code 5,4,3,2,7,6,5,4};
 * remainder {@code 1} unused. Sample {@code CHE-109.322.551}.
 */
public final class CheUidUtil {

    private static final int[] WEIGHTS = {5, 4, 3, 2, 7, 6, 5, 4};

    private CheUidUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{9}") && checkDigit(digits.substring(0, 8)) == digits.charAt(8);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("UID body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        if (rem == 1) {
            throw new IllegalArgumentException("UID body has no valid check digit");
        }
        return (char) ('0' + (rem == 0 ? 0 : 11 - rem));
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
