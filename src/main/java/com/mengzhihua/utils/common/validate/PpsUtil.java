package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Irish PPSN. Check map {@code W=0,A=1..V=22}. Sample {@code 1234567T}.
 */
public final class PpsUtil {

    private static final String LETTERS = "WABCDEFGHIJKLMNOPQRSTUV";

    private PpsUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.matches("\\d{7}[A-W]")) {
            return letter(compact.substring(0, 7), 0) == compact.charAt(7);
        }
        if (compact.matches("\\d{7}[A-W][A-W]")) {
            return letter(compact.substring(0, 7), valueOf(compact.charAt(7))) == compact.charAt(8);
        }
        return false;
    }

    public static char letter(String body7) {
        return letter(body7, 0);
    }

    public static String complete(String body7) {
        String digits = normalize(body7);
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("PPS body must be 7 digits");
        }
        return digits + letter(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static char letter(String body7, int extra) {
        String digits = body7 == null ? "" : body7.replaceAll("\\D", "");
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("PPS body must be 7 digits");
        }
        int sum = extra * 9;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * (8 - i);
        }
        return LETTERS.charAt(sum % 23);
    }

    private static int valueOf(char letter) {
        int index = LETTERS.indexOf(letter);
        return Math.max(index, 0);
    }
}
