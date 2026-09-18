package com.mengzhihua.utils.common.validate;


/**
 * Paraguay RUC. Up to 9 digits, check {@code (-sum % 11) % 10}.
 * Sample {@code 80028061-0}.
 */
public final class PyRucUtil {

    private PyRucUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{2,9}") && checkDigit(digits) == digits.charAt(digits.length() - 1);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 2 && digits.length() <= 9) {
            digits = digits.substring(0, digits.length() - 1);
        }
        if (!digits.matches("\\d{1,8}")) {
            throw new IllegalArgumentException("Paraguay RUC body must be 1–8 digits");
        }
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += (i + 2) * (digits.charAt(digits.length() - 1 - i) - '0');
        }
        return (char) ('0' + (Math.floorMod(-sum, 11) % 10));
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        return digits + checkDigit(digits + "0");
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() < 2) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, digits.length() - 1) + '-' + digits.charAt(digits.length() - 1);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
