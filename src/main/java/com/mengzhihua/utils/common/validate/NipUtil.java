package com.mengzhihua.utils.common.validate;


/**
 * Polish NIP (10 digits). Sample {@code 1234563218}.
 */
public final class NipUtil {

    private static final int[] WEIGHTS = {6, 5, 7, 2, 3, 4, 5, 6, 7};

    private NipUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{10}")) {
            return false;
        }
        return checkDigit(digits.substring(0, 9)) == digits.charAt(9);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("NIP body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        if (rem == 10) {
            throw new IllegalArgumentException("NIP body has no valid check digit");
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body9) {
        return normalize(body9) + checkDigit(body9);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
