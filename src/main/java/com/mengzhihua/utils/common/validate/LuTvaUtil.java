package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Luxembourg TVA. 8 digits, last two are {@code body % 89}.
 * Sample {@code LU 150 274 42}.
 */
public final class LuTvaUtil {

    private LuTvaUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{8}")) {
            return false;
        }
        return checkDigits(digits.substring(0, 6)).equals(digits.substring(6));
    }

    public static String checkDigits(String body6) {
        String digits = normalize(body6);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 6);
        }
        if (!digits.matches("\\d{6}")) {
            throw new IllegalArgumentException("Luxembourg TVA body must be 6 digits");
        }
        return String.format(Locale.ROOT, "%02d", Integer.parseInt(digits) % 89);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 6) {
            digits = digits.substring(0, 6);
        }
        return digits + checkDigits(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 8) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 3) + ' ' + digits.substring(3, 6) + ' ' + digits.substring(6);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s:.-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("LU")) {
            compact = compact.substring(2);
        }
        return compact.replaceAll("\\D", "");
    }
}
