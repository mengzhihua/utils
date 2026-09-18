package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Andorra NRT (Número de Registre Tributari). Letter + 6 digits + letter.
 * Sample {@code U-132950-X}.
 */
public final class AdNrtUtil {

    private static final String TYPES = "ACDEFGLOPU";

    private AdNrtUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("[A-Z]\\d{6}[A-Z]")) {
            return false;
        }
        char type = compact.charAt(0);
        if (TYPES.indexOf(type) < 0) {
            return false;
        }
        String mid = compact.substring(1, 7);
        if (type == 'F' && mid.compareTo("699999") > 0) {
            return false;
        }
        if ((type == 'A' || type == 'L') && !(mid.compareTo("699999") > 0 && mid.compareTo("800000") < 0)) {
            return false;
        }
        return true;
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 8) {
            return value == null ? "" : value.trim();
        }
        return compact.charAt(0) + "-" + compact.substring(1, 7) + "-" + compact.charAt(7);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }
}
