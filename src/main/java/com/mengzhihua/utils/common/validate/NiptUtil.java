package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Albanian NIPT / NUIS. Format {@code [A-M]dddddddd[A-Z]}.
 * Samples {@code J91402501L}, {@code K22218003V}.
 */
public final class NiptUtil {

    private NiptUtil() {
    }

    public static boolean isValid(String value) {
        return normalize(value).matches("[A-M]\\d{8}[A-Z]");
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s().-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("AL")) {
            compact = compact.substring(2);
        }
        return compact;
    }
}
