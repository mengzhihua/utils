package com.mengzhihua.utils.common.validate;


/**
 * Indonesian NPWP (15-digit). Luhn over the first 9 digits.
 * Sample {@code 01.312.166.0-091.000}.
 */
public final class NpwpUtil {

    private NpwpUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{15}") && CheckDigitUtil.luhn(digits.substring(0, 9));
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() >= 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("NPWP body must be 8 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 9) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
