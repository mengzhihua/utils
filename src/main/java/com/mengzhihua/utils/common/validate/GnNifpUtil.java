package com.mengzhihua.utils.common.validate;


/**
 * Guinea NIFp (Numéro d'Identification Fiscale Permanent). 9-digit Luhn.
 * Sample {@code 693-770-885}.
 */
public final class GnNifpUtil {

    private GnNifpUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{9}") && CheckDigitUtil.luhn(digits);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() >= 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("Guinea NIFp body must be 8 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 3) + '-' + digits.substring(3, 6) + '-' + digits.substring(6);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
