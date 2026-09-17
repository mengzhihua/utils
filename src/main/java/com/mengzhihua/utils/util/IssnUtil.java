package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * ISSN-8 checksum (ISO 3297).
 */
public final class IssnUtil {

    private IssnUtil() {
    }

    public static boolean isValid(String issn) {
        String compact = normalize(issn);
        if (compact.length() != 8) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            int digit = Character.digit(compact.charAt(i), 10);
            if (digit < 0) {
                return false;
            }
            sum += digit * (8 - i);
        }
        char last = compact.charAt(7);
        int check = last == 'X' ? 10 : Character.digit(last, 10);
        if (check < 0) {
            return false;
        }
        return (sum + check) % 11 == 0;
    }

    public static String normalize(String issn) {
        return issn == null ? "" : issn.replaceAll("[^0-9Xx]", "").toUpperCase(Locale.ROOT);
    }
}
