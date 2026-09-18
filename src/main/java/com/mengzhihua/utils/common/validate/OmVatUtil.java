package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Oman VATIN. {@code OM} + 9 digits + check {@code 0-9}/{@code X}.
 * Sample {@code OM1100006083}.
 */
public final class OmVatUtil {

    private static final int[] WEIGHTS = {1, 6, 3, 7, 9};

    private OmVatUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("OM\\d{9}[0-9X]")) {
            return false;
        }
        return checkDigit(compact).equals(compact.substring(11));
    }

    public static String checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() >= 12) {
            compact = compact.substring(0, 11);
        }
        if (!compact.matches("OM\\d{9}")) {
            throw new IllegalArgumentException("Oman VAT body must be OM + 9 digits");
        }
        int sum = 1;
        for (int i = 0; i < 5; i++) {
            sum += (compact.charAt(6 + i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        return rem == 10 ? "X" : String.valueOf(rem);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() >= 11) {
            compact = compact.substring(0, 11);
        }
        return compact + checkDigit(compact);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 12) {
            return value == null ? "" : value.trim();
        }
        return compact.substring(0, 2) + ' ' + compact.substring(2, 6) + ' '
                + compact.substring(6, 10) + ' ' + compact.substring(10);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
