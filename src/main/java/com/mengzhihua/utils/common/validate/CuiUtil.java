package com.mengzhihua.utils.common.validate;


/**
 * Romanian CUI / CIF. Body left-padded to 9 digits, weights
 * {@code 7,5,3,2,1,7,5,3,2}, check {@code sum * 10 % 11 % 10}.
 * Sample {@code 18547290}.
 */
public final class CuiUtil {

    private static final int[] WEIGHTS = {7, 5, 3, 2, 1, 7, 5, 3, 2};

    private CuiUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("[1-9]\\d{1,9}")) {
            return false;
        }
        return checkDigit(digits.substring(0, digits.length() - 1)) == digits.charAt(digits.length() - 1);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() > 9) {
            throw new IllegalArgumentException("CUI body must be at most 9 digits");
        }
        if (!digits.matches("[1-9]\\d{0,8}")) {
            throw new IllegalArgumentException("CUI body must be 1 to 9 digits");
        }
        String padded = String.format("%9s", digits).replace(' ', '0');
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (padded.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + ((sum * 10) % 11 % 10));
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (digits.length() >= 2 && isValid(digits)) {
            return digits;
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
