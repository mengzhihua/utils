package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Slovenian ID za DDV. 8 digits, last is check ({@code 11 - weighted sum % 11},
 * {@code 10 → 0}; {@code 11} invalid). Sample {@code SI 5022 3054}.
 */
public final class SiDdvUtil {

    private SiDdvUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        return compact.matches("[1-9]\\d{7}") && checkDigit(compact.substring(0, 7)) == compact.charAt(7);
    }

    public static char checkDigit(String body7) {
        String digits = body7 == null ? "" : body7.replaceAll("\\D", "");
        if (digits.length() != 7) {
            throw new IllegalArgumentException("DDV body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (8 - i) * (digits.charAt(i) - '0');
        }
        int check = 11 - sum % 11;
        if (check == 11) {
            throw new IllegalArgumentException("DDV body has no valid check digit");
        }
        return (char) ('0' + (check == 10 ? 0 : check));
    }

    public static String complete(String body7) {
        String compact = normalize(body7);
        if (compact.length() == 8 && isValid(compact)) {
            return compact;
        }
        if (!compact.matches("\\d{7}")) {
            throw new IllegalArgumentException("DDV body must be 7 digits");
        }
        return compact + checkDigit(compact);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() == 8) {
            return "SI " + compact.substring(0, 4) + ' ' + compact.substring(4);
        }
        return value == null ? "" : value.trim();
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("SI")) {
            compact = compact.substring(2);
        }
        return compact;
    }
}
