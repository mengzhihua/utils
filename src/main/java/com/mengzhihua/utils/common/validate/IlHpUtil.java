package com.mengzhihua.utils.common.validate;


/**
 * Israeli company number (ח.פ.). Nine digits starting with {@code 5}, Luhn.
 * Sample {@code 516179157}.
 */
public final class IlHpUtil {

    private IlHpUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("5\\d{8}") && CheckDigitUtil.luhn(digits);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("5\\d{7}")) {
            throw new IllegalArgumentException("ח.פ. body must be 8 digits starting with 5");
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
