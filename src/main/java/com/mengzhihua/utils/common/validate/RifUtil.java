package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Map;

/**
 * Venezuelan RIF. Sample {@code V-11470283-4}.
 */
public final class RifUtil {

    private static final Map<Character, Integer> TYPES = Map.of(
            'V', 4, 'E', 8, 'J', 12, 'P', 16, 'G', 20);
    private static final int[] WEIGHTS = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final String CHECK = "00987654321";

    private RifUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        return compact.matches("[VEJPG]\\d{9}") && checkDigit(compact) == compact.charAt(9);
    }

    public static char checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() >= 10) {
            compact = compact.substring(0, 9);
        }
        if (!compact.matches("[VEJPG]\\d{8}")) {
            throw new IllegalArgumentException("RIF body must be type plus 8 digits");
        }
        int sum = TYPES.get(compact.charAt(0));
        for (int i = 0; i < 8; i++) {
            sum += (compact.charAt(i + 1) - '0') * WEIGHTS[i];
        }
        return CHECK.charAt(sum % 11);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() >= 9) {
            compact = compact.substring(0, 9);
        }
        return compact + checkDigit(compact);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
