package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.regex.Pattern;

/**
 * ISO 9362 SWIFT/BIC (Apache Commons Validator).
 */
public final class BicUtil {

    private static final Pattern BIC = Pattern.compile("^[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}([A-Z0-9]{3})?$");

    private BicUtil() {
    }

    public static boolean isValid(String bic) {
        String compact = normalize(bic);
        if (!BIC.matcher(compact).matches()) {
            return false;
        }
        char locationType = compact.charAt(6);
        return locationType != '0' && locationType != '1';
    }

    public static String normalize(String bic) {
        return bic == null ? "" : bic.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    public static String bankCode(String bic) {
        String compact = normalize(bic);
        return compact.length() >= 4 ? compact.substring(0, 4) : "";
    }

    public static String countryCode(String bic) {
        String compact = normalize(bic);
        return compact.length() >= 6 ? compact.substring(4, 6) : "";
    }
}
