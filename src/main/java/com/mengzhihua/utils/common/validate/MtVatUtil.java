package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Malta VAT. 8 digits, first not {@code 0}, weighted sum {@code % 37 == 0}.
 * Sample {@code MT 1167-9112}.
 */
public final class MtVatUtil {

    private static final int[] WEIGHTS = {3, 4, 6, 7, 8, 9, 10, 1};

    private MtVatUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("[1-9]\\d{7}")) {
            return false;
        }
        return checksum(digits) == 0;
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("[1-9]\\d{6}")) {
            throw new IllegalArgumentException("Malta VAT body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = Math.floorMod(-sum, 37);
        if (rem > 9) {
            throw new IllegalArgumentException("Malta VAT check digit overflow");
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 7) {
            digits = digits.substring(0, 7);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 8) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 4) + '-' + digits.substring(4);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("MT")) {
            compact = compact.substring(2);
        }
        return compact.replaceAll("\\D", "");
    }

    private static int checksum(String digits) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum % 37;
    }
}
