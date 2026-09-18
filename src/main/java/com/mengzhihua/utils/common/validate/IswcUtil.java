package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * ISWC (ISO 15707). Weights 1–9, check = sum % 10. Sample {@code T-034.524.680-8}.
 */
public final class IswcUtil {

    private IswcUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("T\\d{10}")) {
            return false;
        }
        return checkDigit(compact.substring(1, 10)) == compact.charAt(10);
    }

    public static char checkDigit(String body9) {
        String digits = body9 == null ? "" : body9.replaceAll("\\D", "");
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("ISWC body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * (i + 1);
        }
        return (char) ('0' + (sum % 10));
    }

    public static String complete(String body9) {
        String digits = body9 == null ? "" : body9.replaceAll("\\D", "");
        if (digits.length() > 9) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("ISWC body must be 9 digits");
        }
        return "T" + digits + checkDigit(digits);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 11) {
            return compact;
        }
        return compact.charAt(0) + "-" + compact.substring(1, 4) + "." + compact.substring(4, 7)
                + "." + compact.substring(7, 10) + "-" + compact.charAt(10);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }
}
