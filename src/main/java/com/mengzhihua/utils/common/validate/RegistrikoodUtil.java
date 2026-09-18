package com.mengzhihua.utils.common.validate;


/**
 * Estonian registrikood. First digit {@code 1/7/8/9}; same weights as isikukood.
 * Sample {@code 12345678}.
 */
public final class RegistrikoodUtil {

    private static final int[] PRIMARY = {1, 2, 3, 4, 5, 6, 7};
    private static final int[] SECONDARY = {3, 4, 5, 6, 7, 8, 9};

    private RegistrikoodUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1789]\\d{7}") && checkDigit(digits.substring(0, 7)) == digits.charAt(7);
    }

    public static char checkDigit(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("registrikood body must be 7 digits");
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

    private static int remainder(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        return sum % 11;
    }
}
