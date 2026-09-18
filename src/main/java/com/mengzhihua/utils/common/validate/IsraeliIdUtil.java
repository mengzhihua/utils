package com.mengzhihua.utils.common.validate;


/**
 * Israeli Teudat Zehut (9 digits, Luhn). Sample {@code 123456782}.
 */
public final class IsraeliIdUtil {

    private IsraeliIdUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{9}") && !"000000000".equals(digits) && CheckDigitUtil.luhn(digits);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("Israeli ID body must be 8 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
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
