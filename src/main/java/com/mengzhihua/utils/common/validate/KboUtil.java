package com.mengzhihua.utils.common.validate;


/**
 * Belgian KBO / BCE enterprise number. Last two digits are {@code 97 - (first8 % 97)}.
 * Sample {@code 0123.456.749}.
 */
public final class KboUtil {

    private KboUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[01]\\d{9}") && checkDigits(digits.substring(0, 8)).equals(digits.substring(8));
    }

    public static String checkDigits(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 10) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("KBO body must be 8 digits");
        }
        int rem = Integer.parseInt(digits) % 97;
        return String.format("%02d", 97 - rem);
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 10) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigits(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
