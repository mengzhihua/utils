package com.mengzhihua.utils.common.validate;


/**
 * Danish CVR. Weights {@code 2,7,6,5,4,3,2} on the first 7 digits.
 * Sample {@code 35408002}.
 */
public final class CvrUtil {

    private static final int[] WEIGHTS = {2, 7, 6, 5, 4, 3, 2};

    private CvrUtil() {
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
            throw new IllegalArgumentException("CVR body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        if (rem == 1) {
            throw new IllegalArgumentException("CVR body has no valid check digit");
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
