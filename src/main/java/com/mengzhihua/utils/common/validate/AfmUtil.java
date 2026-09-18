package com.mengzhihua.utils.common.validate;


/**
 * Greek AFM (ΑΦΜ). {@code check = (sum(d[i] * 2^(8-i)) % 11) % 10}.
 * Sample {@code 090000045}.
 */
public final class AfmUtil {

    private AfmUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{9}") && checkDigit(digits.substring(0, 8)) == digits.charAt(8);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("AFM body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * (1 << (8 - i));
        }
        return (char) ('0' + ((sum % 11) % 10));
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
