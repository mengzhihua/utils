package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Uruguay RUT. 12 digits, prefix 01–22, sequence, {@code 001}, check {@code -sum % 11}.
 * Sample {@code 21-100342-001-7}.
 */
public final class UyRutUtil {

    private static final int[] WEIGHTS = {4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

    private UyRutUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{12}")) {
            return false;
        }
        int prefix = Integer.parseInt(digits.substring(0, 2));
        if (prefix < 1 || prefix > 22 || "000000".equals(digits.substring(2, 8)) || !"001".equals(digits.substring(8, 11))) {
            return false;
        }
        int check = checkValue(digits);
        return check <= 9 && check == digits.charAt(11) - '0';
    }

    public static char checkDigit(String body) {
        int check = checkValue(normalize(body));
        if (check > 9) {
            throw new IllegalArgumentException("Uruguay RUT check digit overflow");
        }
        return (char) ('0' + check);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 11) {
            digits = digits.substring(0, 11);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 12) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 2) + '-' + digits.substring(2, 8) + '-' + digits.substring(8, 11) + '-' + digits.charAt(11);
    }

    public static String normalize(String value) {
        String compact = value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("UY")) {
            compact = compact.substring(2);
        }
        return compact.replaceAll("\\D", "");
    }

    private static int checkValue(String digits) {
        if (digits.length() >= 12) {
            digits = digits.substring(0, 11);
        }
        if (!digits.matches("\\d{11}")) {
            throw new IllegalArgumentException("Uruguay RUT body must be 11 digits");
        }
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return Math.floorMod(-sum, 11);
    }
}
