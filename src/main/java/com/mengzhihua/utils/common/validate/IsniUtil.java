package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * ISNI (ISO 27729) using ISO 7064 MOD 11-2. Sample {@code 0000 0001 2146 358X}.
 */
public final class IsniUtil {

    private IsniUtil() {
    }

    public static boolean isValid(String isni) {
        String compact = normalize(isni);
        if (!compact.matches("\\d{15}[\\dX]")) {
            return false;
        }
        return checkDigit(compact.substring(0, 15)) == compact.charAt(15);
    }

    public static char checkDigit(String body15) {
        String digits = normalize(body15);
        if (!digits.matches("\\d{15}")) {
            throw new IllegalArgumentException("ISNI body must be 15 digits");
        }
        int p = 0;
        for (int i = 0; i < 15; i++) {
            p = (p + (digits.charAt(i) - '0')) * 2;
        }
        int check = (12 - (p % 11)) % 11;
        return check == 10 ? 'X' : (char) ('0' + check);
    }

    public static String complete(String body15) {
        return normalize(body15) + checkDigit(body15);
    }

    public static String normalize(String isni) {
        return isni == null ? "" : isni.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
