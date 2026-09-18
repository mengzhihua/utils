package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Cyprus Αριθμός Εγγραφής Φ.Π.Α. 8 digits + letter; prefix {@code 12} invalid.
 * Sample {@code CY-10259033P}.
 */
public final class CyVatUtil {

    private static final int[] EVEN = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21};

    private CyVatUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{8}[A-Z]")) {
            return false;
        }
        if (compact.startsWith("12")) {
            return false;
        }
        return checkDigit(compact).charAt(0) == compact.charAt(8);
    }

    public static String checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() >= 9) {
            compact = compact.substring(0, 8);
        }
        if (!compact.matches("\\d{8}")) {
            throw new IllegalArgumentException("Cyprus VAT body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            int n = compact.charAt(i) - '0';
            sum += (i % 2 == 0) ? EVEN[n] : n;
        }
        return String.valueOf((char) ('A' + (sum % 26)));
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() >= 8) {
            compact = compact.substring(0, 8);
        }
        return compact + checkDigit(compact);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return "CY-" + compact;
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("CY")) {
            compact = compact.substring(2);
        }
        return compact;
    }
}
