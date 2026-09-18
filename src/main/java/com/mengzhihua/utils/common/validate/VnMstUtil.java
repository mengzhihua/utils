package com.mengzhihua.utils.common.validate;


/**
 * Vietnamese MST (10 or 13 digits). Weights {@code 31,29,23,19,17,13,7,5,3},
 * check {@code 10 - (sum % 11)}. Sample {@code 0100233488}.
 */
public final class VnMstUtil {

    private static final int[] WEIGHTS = {31, 29, 23, 19, 17, 13, 7, 5, 3};

    private VnMstUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{10}") && !digits.matches("\\d{13}")) {
            return false;
        }
        if ("0000000".equals(digits.substring(2, 9))) {
            return false;
        }
        if (digits.length() == 13 && "000".equals(digits.substring(10))) {
            return false;
        }
        return checkDigit(digits.substring(0, 9)) == digits.charAt(9);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() >= 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("MST body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int check = 10 - (sum % 11);
        if (check == 10) {
            throw new IllegalArgumentException("MST check digit overflow");
        }
        return (char) ('0' + check);
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 9) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
