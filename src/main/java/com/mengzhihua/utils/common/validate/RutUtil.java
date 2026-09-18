package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Chilean RUT. Weights 2–7 repeating from the right; check {@code 11 - sum % 11}
 * ({@code 11→0}, {@code 10→K}). Sample {@code 12.345.678-5}.
 */
public final class RutUtil {

    private RutUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{7,8}[0-9K]")) {
            return false;
        }
        return checkDigit(compact.substring(0, compact.length() - 1)) == compact.charAt(compact.length() - 1);
    }

    public static char checkDigit(String body) {
        String digits = body == null ? "" : body.replaceAll("\\D", "");
        if (!digits.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("RUT body must be 7 or 8 digits");
        }
        int sum = 0;
        int weight = 2;
        for (int i = digits.length() - 1; i >= 0; i--) {
            sum += (digits.charAt(i) - '0') * weight;
            weight = weight == 7 ? 2 : weight + 1;
        }
        int check = 11 - (sum % 11);
        if (check == 11) {
            return '0';
        }
        if (check == 10) {
            return 'K';
        }
        return (char) ('0' + check);
    }

    public static String complete(String body) {
        String digits = body == null ? "" : body.replaceAll("\\D", "");
        if (digits.length() >= 8 && digits.matches("\\d{8}[0-9K]?")) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[.\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
