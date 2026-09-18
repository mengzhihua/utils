package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Kenya KRA PIN. Format {@code [AP]\\d{9}[A-Z]} only.
 * Sample {@code P051365947M}.
 */
public final class KePinUtil {

    private KePinUtil() {
    }

    public static boolean isValid(String value) {
        return normalize(value).matches("[AP]\\d{9}[A-Z]");
    }

    public static boolean individual(String value) {
        String compact = normalize(value);
        return isValid(compact) && compact.charAt(0) == 'A';
    }

    public static String format(String value) {
        return normalize(value);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
