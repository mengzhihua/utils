package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * CUSIP check digit (Apache Commons Validator / ANSI X9.6).
 */
public final class CusipUtil {

    private CusipUtil() {
    }

    public static boolean isValid(String cusip) {
        String compact = normalize(cusip);
        if (compact.length() != 9) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (value(compact.charAt(i)) < 0) {
                return false;
            }
        }
        return checkDigit(compact.substring(0, 8)) == compact.charAt(8);
    }

    public static char checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() != 8) {
            throw new IllegalArgumentException("CUSIP body must be 8 characters");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            int charValue = value(compact.charAt(i));
            if (charValue < 0) {
                throw new IllegalArgumentException("invalid CUSIP character");
            }
            int rightPos = 9 - i;
            int weighted = charValue * (rightPos % 2 == 0 ? 2 : 1);
            sum += weighted / 10 + weighted % 10;
        }
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }

    public static String normalize(String cusip) {
        return cusip == null ? "" : cusip.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static int value(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        return switch (c) {
            case '*' -> 36;
            case '@' -> 37;
            case '#' -> 38;
            default -> -1;
        };
    }
}
