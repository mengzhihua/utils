package com.mengzhihua.utils.common.validate;


/**
 * Swiss AHV / AVS 13 (EAN-13, prefix {@code 756}). Sample {@code 756.1234.5678.97}.
 */
public final class AhvUtil {

    private AhvUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("756\\d{10}") && EanUtil.isValid(digits);
    }

    public static char checkDigit(String body12) {
        String digits = normalize(body12);
        if (!digits.matches("756\\d{9}")) {
            throw new IllegalArgumentException("AHV body must be 12 digits starting with 756");
        }
        return EanUtil.checkDigit(digits);
    }

    public static String complete(String body12) {
        return normalize(body12) + checkDigit(body12);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
