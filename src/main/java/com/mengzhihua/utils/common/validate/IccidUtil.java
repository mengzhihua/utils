package com.mengzhihua.utils.common.validate;


/**
 * SIM ICCID (19–20 digits, starts with {@code 89}, Luhn). Sample {@code 89014103211118510720}.
 */
public final class IccidUtil {

    private IccidUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("89\\d{17,18}") && CheckDigitUtil.luhn(digits);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (!digits.matches("89\\d{16,17}")) {
            throw new IllegalArgumentException("ICCID body must be 18 or 19 digits starting with 89");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body) {
        return normalize(body) + checkDigit(body);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
