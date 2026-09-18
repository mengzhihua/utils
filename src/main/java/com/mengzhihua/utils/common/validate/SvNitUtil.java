package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * El Salvador NIT. 14 digits {@code XXXX-XXXXXX-XXX-X}.
 * Sample {@code 0614-050707-104-8}.
 */
public final class SvNitUtil {

    private static final int[] OLD_WEIGHTS = {14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    private static final int[] NEW_WEIGHTS = {2, 7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    private SvNitUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("[019]\\d{13}")) {
            return false;
        }
        return checkDigit(digits) == digits.charAt(13);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 14) {
            digits = digits.substring(0, 13);
        }
        if (!digits.matches("\\d{13}")) {
            throw new IllegalArgumentException("El Salvador NIT body must be 13 digits");
        }
        int[] weights = digits.substring(10).compareTo("100") <= 0 ? OLD_WEIGHTS : NEW_WEIGHTS;
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        int rem = weights == OLD_WEIGHTS ? (sum % 11) % 10 : Math.floorMod(-sum, 11) % 10;
        return (char) ('0' + rem);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 13) {
            digits = digits.substring(0, 13);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 14) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 4) + '-' + digits.substring(4, 10) + '-' + digits.substring(10, 13) + '-' + digits.charAt(13);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("SV")) {
            compact = compact.substring(2);
        }
        return compact.replaceAll("\\D", "");
    }
}
