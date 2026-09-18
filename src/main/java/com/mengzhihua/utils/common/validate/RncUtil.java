package com.mengzhihua.utils.common.validate;


import java.util.Set;

/**
 * Dominican Republic RNC (9 digits). Sample {@code 1-01-85004-3}.
 */
public final class RncUtil {

    private static final int[] WEIGHTS = {7, 9, 8, 6, 5, 4, 3, 2};
    private static final Set<String> WHITELIST = Set.of(
            "101581601", "101582245", "101595422", "101595785", "10233317",
            "131188691", "401007374", "501341601", "501378067", "501620371");

    private RncUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (WHITELIST.contains(digits)) {
            return true;
        }
        return digits.matches("\\d{9}") && checkDigit(digits.substring(0, 8)) == digits.charAt(8);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() >= 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("RNC body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + ((10 - (sum % 11)) % 9 + 1));
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return digits.charAt(0) + "-" + digits.substring(1, 3) + "-" + digits.substring(3, 8) + "-" + digits.charAt(8);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
