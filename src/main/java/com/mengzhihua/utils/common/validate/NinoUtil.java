package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Set;

/**
 * UK National Insurance Number. Two prefix letters, six digits, suffix A–D.
 * Sample {@code AB123456C}.
 */
public final class NinoUtil {

    private static final Set<String> FORBIDDEN = Set.of("BG", "GB", "KN", "NK", "NT", "TN", "ZZ");

    private NinoUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("[A-CEGHJ-PR-TW-Z][A-CEGHJ-NPR-TW-Z]\\d{6}[A-D]")) {
            return false;
        }
        return !FORBIDDEN.contains(compact.substring(0, 2));
    }

    public static String prefix(String value) {
        String compact = normalize(value);
        return compact.length() >= 2 ? compact.substring(0, 2) : "";
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }
}
