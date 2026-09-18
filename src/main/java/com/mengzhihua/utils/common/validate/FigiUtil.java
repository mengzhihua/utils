package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Bloomberg FIGI check digit (OpenFIGI / CUSIP-style from the right). Sample {@code BBG000B9XRY4} (Apple).
 */
public final class FigiUtil {

    private FigiUtil() {
    }

    public static boolean isValid(String figi) {
        String compact = normalize(figi);
        if (!compact.matches("[0-9BCDFGHJKLMNPQRSTVWXYZ]{12}")) {
            return false;
        }
        return compact.charAt(11) == checkDigit(compact.substring(0, 11));
    }

    public static char checkDigit(String body11) {
        String body = normalize(body11);
        if (body.length() != 11) {
            throw new IllegalArgumentException("FIGI body must be 11 characters");
        }
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            int value = value(body.charAt(i));
            if (value < 0) {
                throw new IllegalArgumentException("invalid FIGI character");
            }
            int fromRight = 10 - i;
            if (fromRight % 2 == 0) {
                int weighted = value * 2;
                sum += weighted / 10 + weighted % 10;
            } else {
                sum += value;
            }
        }
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }

    public static String complete(String body11) {
        String body = normalize(body11);
        return body + checkDigit(body);
    }

    public static String normalize(String figi) {
        return figi == null ? "" : figi.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static int value(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        return -1;
    }
}
