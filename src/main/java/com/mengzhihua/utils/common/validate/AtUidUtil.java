package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Austrian UID (Umsatzsteuer-Identifikationsnummer). {@code U} + 8 digits,
 * last is the BMF check digit. Sample {@code AT U13585627}.
 */
public final class AtUidUtil {

    private AtUidUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        return compact.matches("U\\d{8}") && checkDigit(compact.substring(1, 8)) == compact.charAt(8);
    }

    /**
     * Check digit for the 7 numeric digits after {@code U} (BMF construction rule).
     */
    public static char checkDigit(String body7) {
        String digits = body7 == null ? "" : body7.replaceAll("\\D", "");
        if (digits.length() != 7) {
            throw new IllegalArgumentException("UID body must be 7 digits");
        }
        int r = s(digits.charAt(1)) + s(digits.charAt(3)) + s(digits.charAt(5));
        int sum = r + (digits.charAt(0) - '0') + (digits.charAt(2) - '0')
                + (digits.charAt(4) - '0') + (digits.charAt(6) - '0') + 4;
        return (char) ('0' + (10 - sum % 10) % 10);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (compact.startsWith("U")) {
            compact = compact.substring(1);
        }
        if (compact.length() == 8 && isValid("U" + compact)) {
            return "U" + compact;
        }
        if (!compact.matches("\\d{7}")) {
            throw new IllegalArgumentException("UID body must be 7 digits");
        }
        return "U" + compact + checkDigit(compact);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.matches("U\\d{8}")) {
            return "AT " + compact;
        }
        return value == null ? "" : value.trim();
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s./-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("AT")) {
            compact = compact.substring(2);
        }
        return compact;
    }

    private static int s(char digit) {
        int value = digit - '0';
        return value / 5 + (value * 2) % 10;
    }
}
