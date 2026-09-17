package com.mengzhihua.utils.common.validate;

/**
 * Brazilian CNPJ. Sample {@code 00.000.000/0001-91}.
 */
public final class CnpjUtil {

    private static final int[] WEIGHT_12 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] WEIGHT_13 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private CnpjUtil() {
    }

    public static boolean isValid(String cnpj) {
        String digits = normalize(cnpj);
        if (!digits.matches("\\d{14}") || sameDigits(digits)) {
            return false;
        }
        return checkDigit(digits.substring(0, 12)) == digits.charAt(12)
                && checkDigit(digits.substring(0, 13)) == digits.charAt(13);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        int[] weights = digits.length() == 12 ? WEIGHT_12 : WEIGHT_13;
        if (digits.length() != weights.length) {
            throw new IllegalArgumentException("CNPJ body must be 12 or 13 digits");
        }
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        int rem = sum % 11;
        return (char) ('0' + (rem < 2 ? 0 : 11 - rem));
    }

    public static String complete(String body12) {
        String digits = normalize(body12);
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("CNPJ body must be 12 digits");
        }
        char first = checkDigit(digits);
        return digits + first + checkDigit(digits + first);
    }

    public static String normalize(String cnpj) {
        return cnpj == null ? "" : cnpj.replaceAll("\\D", "");
    }

    private static boolean sameDigits(String digits) {
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }
}
