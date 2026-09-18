package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Senegal NINEA. 7 (or 9) digits, optional 3-char COFI.
 * Sample {@code 306 7221} / {@code 30672212G2}.
 */
public final class SnNineaUtil {

    private static final int[] WEIGHTS = {1, 2, 1, 2, 1, 2, 1, 2, 1};

    private SnNineaUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        String body = body(compact);
        String cofi = cofi(compact);
        if (!body.matches("\\d{7}|\\d{9}")) {
            return false;
        }
        if (!cofi.isEmpty() && !cofi.matches("[012][ABCDEFGHJKLMNPQRSTUVWZ]\\d")) {
            return false;
        }
        return checksum(body) == 0;
    }

    public static char checkDigit(String body6) {
        String digits = digitsOnly(body6);
        if (digits.length() >= 7) {
            digits = digits.substring(0, 6);
        }
        if (!digits.matches("\\d{6}")) {
            throw new IllegalArgumentException("Senegal NINEA body must be 6 digits");
        }
        int rem = Math.floorMod(-checksum(digits + "0"), 10);
        return (char) ('0' + rem);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        String cofi = cofi(compact);
        String digits = body(compact);
        if (digits.length() >= 6) {
            digits = digits.substring(0, 6);
        }
        return digits + checkDigit(digits) + cofi;
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() <= 9) {
            return compact;
        }
        return compact.substring(0, compact.length() - 3) + ' ' + compact.substring(compact.length() - 3);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[\\s/,\\-]", "").toUpperCase(Locale.ROOT);
    }

    private static String body(String compact) {
        if (compact.length() > 9) {
            return compact.substring(0, compact.length() - 3);
        }
        return compact;
    }

    private static String cofi(String compact) {
        return compact.length() > 9 ? compact.substring(compact.length() - 3) : "";
    }

    private static int checksum(String number) {
        String padded = "000000000".substring(0, Math.max(0, 9 - number.length())) + number;
        if (padded.length() > 9) {
            padded = padded.substring(padded.length() - 9);
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (padded.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum % 10;
    }

    private static String digitsOnly(String value) {
        return normalize(value).replaceAll("\\D", "");
    }
}
