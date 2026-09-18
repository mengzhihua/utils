package com.mengzhihua.utils.common.validate;


/**
 * Bulgarian EIK / BULSTAT (9 digits). Primary weights {@code 1..8};
 * fallback {@code 3..10}. Sample {@code 831641791}.
 */
public final class EikUtil {

    private static final int[] PRIMARY = {1, 2, 3, 4, 5, 6, 7, 8};
    private static final int[] SECONDARY = {3, 4, 5, 6, 7, 8, 9, 10};

    private EikUtil() {
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
            throw new IllegalArgumentException("EIK body must be 8 digits");
        }
        int rem = remainder(digits, PRIMARY);
        if (rem == 10) {
            rem = remainder(digits, SECONDARY);
            if (rem == 10) {
                rem = 0;
            }
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

    private static int remainder(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        return sum % 11;
    }
}
