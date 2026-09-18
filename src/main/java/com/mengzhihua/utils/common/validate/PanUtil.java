package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Indian PAN. 4th character is entity type ({@code P} person). Sample {@code ABCPE1234F}.
 */
public final class PanUtil {

    private PanUtil() {
    }

    public static boolean isValid(String value) {
        return normalize(value).matches("[A-Z]{3}[PCHFATBLJG][A-Z]\\d{4}[A-Z]");
    }

    public static char entityType(String value) {
        String compact = normalize(value);
        if (compact.length() < 4) {
            throw new IllegalArgumentException("PAN is too short");
        }
        return compact.charAt(3);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
