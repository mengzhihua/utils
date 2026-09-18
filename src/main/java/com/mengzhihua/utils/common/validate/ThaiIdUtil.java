package com.mengzhihua.utils.common.validate;


/**
 * Thai national ID (13 digits). Weights {@code 13..2},
 * check {@code (11 - sum % 11) % 10}. Sample {@code 1234567890121}.
 */
public final class ThaiIdUtil {

    private ThaiIdUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-8]\\d{12}") && checkDigit(digits.substring(0, 12)) == digits.charAt(12);
    }

    public static char checkDigit(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(0, 12);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("Thai ID body must be 12 digits");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * (13 - i);
        }
        return (char) ('0' + ((11 - (sum % 11)) % 10));
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
