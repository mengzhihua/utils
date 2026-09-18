package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * North Macedonia ЕДБ (Едниствен Даночен Број). 13 digits, optional {@code MK}/{@code МК}.
 * Sample {@code 4030000375897}.
 */
public final class MkEdbUtil {

    private static final int[] WEIGHTS = {7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    private MkEdbUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{13}")) {
            return false;
        }
        return checkDigit(digits) == digits.charAt(12);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 13) {
            digits = digits.substring(0, 12);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("North Macedonia EDB body must be 12 digits");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + Math.floorMod(-sum, 11) % 10);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 12) {
            digits = digits.substring(0, 12);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("MK") || compact.startsWith("МК")) {
            compact = compact.substring(2);
        }
        return compact.replaceAll("\\D", "");
    }
}
