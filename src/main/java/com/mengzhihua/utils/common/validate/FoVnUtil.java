package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Faroe Islands V-number (Vinnutal). 6 digits, optional {@code FO}.
 * Sample {@code 623857}.
 */
public final class FoVnUtil {

    private FoVnUtil() {
    }

    public static boolean isValid(String value) {
        return normalize(value).matches("\\d{6}");
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String format(String value) {
        return normalize(value);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("FO")) {
            compact = compact.substring(2);
        }
        return compact.replaceAll("\\D", "");
    }
}
