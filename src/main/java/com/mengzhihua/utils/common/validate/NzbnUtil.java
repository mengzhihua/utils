package com.mengzhihua.utils.common.validate;


/**
 * New Zealand Business Number (13-digit GS1 GLN, prefix {@code 94}).
 * Sample {@code 9429000000000}.
 */
public final class NzbnUtil {

    private NzbnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("94\\d{11}") && EanUtil.isValid(digits);
    }

    public static char checkDigit(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(0, 12);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("NZBN body must be 12 digits");
        }
        return EanUtil.checkDigit(digits);
    }

    public static String complete(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(0, 12);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
