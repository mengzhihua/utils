package com.mengzhihua.utils.common.validate;


import java.util.Set;

/**
 * Peruvian RUC (11 digits). Prefix {@code 10}/{@code 15}/{@code 17}/{@code 20}.
 * Sample {@code 20512333797}.
 */
public final class PeRucUtil {

    private static final int[] WEIGHTS = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
    private static final Set<String> PREFIXES = Set.of("10", "15", "17", "20");

    private PeRucUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{11}")
                && PREFIXES.contains(digits.substring(0, 2))
                && checkDigit(digits.substring(0, 10)) == digits.charAt(10);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (digits.length() >= 11) {
            digits = digits.substring(0, 10);
        }
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("RUC body must be 10 digits");
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + ((11 - (sum % 11)) % 10));
    }

    public static String complete(String body10) {
        String digits = normalize(body10);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 10) {
            digits = digits.substring(0, 10);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
