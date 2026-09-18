package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Set;

/**
 * UK / Isle of Man VAT. 9 or 12 digits, or {@code GD}/{@code HA} department codes.
 * Sample {@code GB 980 7806 84}.
 */
public final class GbVatUtil {

    private static final int[] WEIGHTS = {8, 7, 6, 5, 4, 3, 2, 10, 1};
    private static final Set<Integer> RESTART = Set.of(0, 42, 55);

    private GbVatUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() == 5) {
            return government(compact.substring(0, 2), compact.substring(2));
        }
        if (compact.length() == 11 && (compact.startsWith("GD8888") || compact.startsWith("HA8888"))) {
            String body = compact.substring(6, 9);
            if (!government(compact.substring(0, 2), body) || !body.matches("\\d{3}")
                    || !compact.substring(9).matches("\\d{2}")) {
                return false;
            }
            return Integer.parseInt(body) % 97 == Integer.parseInt(compact.substring(9));
        }
        if ((compact.length() == 9 || compact.length() == 12) && compact.matches("\\d+")) {
            int checksum = checksum(compact.substring(0, 9));
            if (Integer.parseInt(compact.substring(0, 3)) >= 100) {
                return RESTART.contains(checksum);
            }
            return checksum == 0;
        }
        return false;
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() == 5) {
            return compact;
        }
        if (compact.length() == 12) {
            return compact.substring(0, 3) + ' ' + compact.substring(3, 7) + ' '
                    + compact.substring(7, 9) + ' ' + compact.substring(9);
        }
        if (compact.length() == 9) {
            return compact.substring(0, 3) + ' ' + compact.substring(3, 7) + ' ' + compact.substring(7);
        }
        return value == null ? "" : value.trim();
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("GB") || compact.startsWith("XI")) {
            compact = compact.substring(2);
        }
        return compact;
    }

    static int checksum(String nine) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += WEIGHTS[i] * (nine.charAt(i) - '0');
        }
        return sum % 97;
    }

    private static boolean government(String prefix, String body) {
        if (!body.matches("\\d{3}")) {
            return false;
        }
        int value = Integer.parseInt(body);
        if ("GD".equals(prefix)) {
            return value < 500;
        }
        return "HA".equals(prefix) && value >= 500;
    }
}
