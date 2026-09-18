package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Indian GSTIN. Luhn mod 36 over {@code 0-9A-Z}; 14th char is {@code Z}.
 * Sample {@code 27AAPFU0939F1ZV}.
 */
public final class GstinUtil {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private GstinUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{2}[A-Z]{5}\\d{4}[A-Z][1-9A-Z]Z[0-9A-Z]")) {
            return false;
        }
        int state = Integer.parseInt(compact.substring(0, 2));
        if (state < 1 || (state > 38 && state != 97)) {
            return false;
        }
        return PanUtil.isValid(compact.substring(2, 12)) && luhnMod36(compact);
    }

    public static char checkDigit(String body14) {
        String compact = normalize(body14);
        if (compact.length() == 15) {
            compact = compact.substring(0, 14);
        }
        if (!compact.matches("\\d{2}[A-Z]{5}\\d{4}[A-Z][1-9A-Z]Z")) {
            throw new IllegalArgumentException("GSTIN body must be 14 characters");
        }
        int checksum = luhnMod36Sum(compact + ALPHABET.charAt(0));
        return ALPHABET.charAt((ALPHABET.length() - checksum) % ALPHABET.length());
    }

    public static String complete(String body14) {
        String compact = normalize(body14);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() == 15) {
            compact = compact.substring(0, 14);
        }
        return compact + checkDigit(compact);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static boolean luhnMod36(String value) {
        return luhnMod36Sum(value) == 0;
    }

    private static int luhnMod36Sum(String value) {
        int modulus = ALPHABET.length();
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = value.length() - 1; i >= 0; i--) {
            int index = ALPHABET.indexOf(value.charAt(i));
            if (index < 0) {
                return -1;
            }
            if (doubleDigit) {
                int doubled = index * 2;
                index = doubled / modulus + doubled % modulus;
            }
            sum += index;
            doubleDigit = !doubleDigit;
        }
        return sum % modulus;
    }
}
