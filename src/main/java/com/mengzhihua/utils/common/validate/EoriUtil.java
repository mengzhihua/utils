package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * EU EORI (ISO country + 1–15 alphanumeric). French EORI is {@code FR} + SIRET.
 * Sample {@code FR73282932000074}.
 */
public final class EoriUtil {

    private EoriUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("[A-Z]{2}[A-Z0-9]{1,15}")) {
            return false;
        }
        if (compact.startsWith("FR")) {
            return SiretUtil.isValid(compact.substring(2));
        }
        return true;
    }

    public static String country(String value) {
        String compact = normalize(value);
        return compact.length() >= 2 ? compact.substring(0, 2) : "";
    }

    public static String identifier(String value) {
        String compact = normalize(value);
        return compact.length() > 2 ? compact.substring(2) : "";
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }
}
