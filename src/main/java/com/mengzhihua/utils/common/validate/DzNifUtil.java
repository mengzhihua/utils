package com.mengzhihua.utils.common.validate;


/**
 * Algeria NIF (Numéro d'Identification Fiscale). 15 or 20 digits.
 * Sample {@code 416001000000007} / {@code 41201600000606600001}.
 */
public final class DzNifUtil {

    private DzNifUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{15}|\\d{20}");
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
        return value.replaceAll("\\D", "");
    }
}
