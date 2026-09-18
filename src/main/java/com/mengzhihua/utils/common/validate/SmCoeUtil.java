package com.mengzhihua.utils.common.validate;


import java.util.Set;

/**
 * San Marino COE (Codice operatore economico). Up to 5 digits; leading zeros dropped.
 * Sample {@code 51} / {@code 024165} → {@code 24165}.
 */
public final class SmCoeUtil {

    private static final Set<Integer> LOW = Set.of(
            2, 4, 6, 7, 8, 9, 10, 11, 13, 16, 18, 19, 20, 21, 25, 26, 30, 32, 33, 35,
            36, 37, 38, 39, 40, 42, 45, 47, 49, 51, 52, 55, 56, 57, 58, 59, 61, 62,
            64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 79, 80, 81, 84, 85,
            87, 88, 91, 92, 94, 95, 96, 97, 99);

    private SmCoeUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (digits.isEmpty() || digits.length() > 5 || !digits.matches("\\d+")) {
            return false;
        }
        return digits.length() >= 3 || LOW.contains(Integer.parseInt(digits));
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String digits = value.replaceAll("[\\s.]", "").replaceAll("\\D", "");
        return digits.replaceFirst("^0+", "");
    }
}
