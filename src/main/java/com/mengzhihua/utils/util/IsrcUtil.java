package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * ISO 3901 ISRC (International Standard Recording Code).
 */
public final class IsrcUtil {

    private IsrcUtil() {
    }

    public static boolean isValid(String isrc) {
        String compact = normalize(isrc);
        return compact.matches("[A-Z]{2}[A-Z0-9]{3}\\d{7}");
    }

    public static String format(String isrc) {
        String compact = normalize(isrc);
        if (compact.length() != 12) {
            return compact;
        }
        return compact.substring(0, 2) + "-" + compact.substring(2, 5) + "-"
                + compact.substring(5, 7) + "-" + compact.substring(7);
    }

    public static String country(String isrc) {
        String compact = normalize(isrc);
        return compact.length() >= 2 ? compact.substring(0, 2) : "";
    }

    public static String normalize(String isrc) {
        return isrc == null ? "" : isrc.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
