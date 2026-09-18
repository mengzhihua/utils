package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Set;

/**
 * Mexican RFC (SAT). 12-char moral / 13-char física; check uses Annex III
 * values and weights 13..2. Sample {@code GODE561231GR8}.
 */
public final class MxRfcUtil {

    private static final Set<String> GENERIC = Set.of("XAXX010101000", "XEXX010101000");
    private static final String MAP = "0123456789ABCDEFGHIJKLMN&OPQRSTUVWXYZ ";

    private MxRfcUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (GENERIC.contains(compact)) {
            return true;
        }
        if (!compact.matches("[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{3}")) {
            return false;
        }
        return checkDigit(compact.substring(0, compact.length() - 1)) == compact.charAt(compact.length() - 1);
    }

    public static char checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() == 13) {
            compact = compact.substring(0, 12);
        }
        if (compact.length() == 11) {
            compact = " " + compact;
        }
        if (compact.length() != 12) {
            throw new IllegalArgumentException("RFC body must be 11 or 12 characters");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += valueOf(compact.charAt(i)) * (13 - i);
        }
        int rem = sum % 11;
        if (rem == 0) {
            return '0';
        }
        if (rem == 1) {
            return 'A';
        }
        return (char) ('0' + (11 - rem));
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (compact.length() >= 13 && isValid(compact)) {
            return compact;
        }
        if (compact.length() == 13) {
            compact = compact.substring(0, 12);
        }
        return compact + checkDigit(compact);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static int valueOf(char ch) {
        if (ch == 'Ñ') {
            return 38;
        }
        int index = MAP.indexOf(ch);
        return index < 0 ? 0 : index;
    }
}
