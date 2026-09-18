package com.mengzhihua.utils.common.validate;


/**
 * Liechtenstein PEID (Personenidentifikationsnummer). 4–12 digits after stripping leading zeros.
 * Sample {@code 00001234567} → {@code 1234567}.
 */
public final class LiPeidUtil {

    private LiPeidUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{4,12}");
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String digits = value.replaceAll("[\\s.]", "").replaceAll("\\D", "");
        return digits.replaceFirst("^0+", "");
    }
}
