package com.mengzhihua.utils.common.validate;


/**
 * Canadian SIN (9-digit Luhn). Sample {@code 046454286}.
 */
public final class SinUtil {

    private SinUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{9}") && !digits.matches("0{9}") && CheckDigitUtil.luhn(digits);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("SIN body must be 8 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body8) {
        return normalize(body8) + checkDigit(body8);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
