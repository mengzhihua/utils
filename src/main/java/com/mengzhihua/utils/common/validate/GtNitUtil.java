package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Guatemala NIT (Número de Identificación Tributaria).
 * Check {@code -sum(i*n for i,n in enumerate(reversed(body), 2)) % 11}, {@code 10 → K}.
 * Sample {@code 576937-K} / {@code 7108-0}.
 */
public final class GtNitUtil {

    private GtNitUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() < 2 || compact.length() > 12) {
            return false;
        }
        String body = compact.substring(0, compact.length() - 1);
        char check = compact.charAt(compact.length() - 1);
        if (!body.chars().allMatch(Character::isDigit) || (check != 'K' && !Character.isDigit(check))) {
            return false;
        }
        return checkDigit(body) == check;
    }

    public static char checkDigit(String body) {
        String digits = digitsOnly(body);
        if (digits.isEmpty() || digits.length() > 11) {
            throw new IllegalArgumentException("Guatemala NIT body must be 1 to 11 digits");
        }
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += (digits.charAt(digits.length() - 1 - i) - '0') * (i + 2);
        }
        int rem = Math.floorMod(-sum, 11);
        return rem == 10 ? 'K' : (char) ('0' + rem);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        String digits = digitsOnly(compact);
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() < 2) {
            return value == null ? "" : value.trim();
        }
        return compact.substring(0, compact.length() - 1) + '-' + compact.charAt(compact.length() - 1);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        int start = 0;
        while (start < compact.length() && compact.charAt(start) == '0') {
            start++;
        }
        return compact.substring(start);
    }

    private static String digitsOnly(String value) {
        String compact = normalize(value);
        return compact.replaceAll("\\D", "");
    }
}
