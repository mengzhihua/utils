package com.mengzhihua.utils.common.validate;


/**
 * GS1 SSCC (18-digit GTIN check). Sample {@code 106141411234567897}.
 */
public final class SsccUtil {

    private SsccUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{18}") && EanUtil.checkDigit(digits.substring(0, 17)) == digits.charAt(17);
    }

    public static char checkDigit(String body17) {
        String digits = normalize(body17);
        if (!digits.matches("\\d{17}")) {
            throw new IllegalArgumentException("SSCC body must be 17 digits");
        }
        return EanUtil.checkDigit(digits);
    }

    public static String complete(String body17) {
        return normalize(body17) + checkDigit(body17);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
