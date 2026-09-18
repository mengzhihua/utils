package com.mengzhihua.utils.common.validate;


/**
 * ОГРН / ОГРНИП (Russian primary state registration number).
 * Samples {@code 1022200525819}, {@code 385768585948949}.
 */
public final class OgrnUtil {

    private OgrnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (digits.matches("[1-9]\\d{12}")) {
            return checkDigit(digits.substring(0, 12)) == digits.charAt(12);
        }
        if (digits.matches("[34]\\d{14}")) {
            return checkDigit(digits.substring(0, 14)) == digits.charAt(14);
        }
        return false;
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() == 13 || digits.length() == 15) {
            digits = digits.substring(0, digits.length() - 1);
        }
        if (digits.length() == 12) {
            return (char) ('0' + (int) (Long.parseLong(digits) % 11 % 10));
        }
        if (digits.length() == 14) {
            int rem = (int) (Long.parseLong(digits) % 13);
            if (rem > 9) {
                throw new IllegalArgumentException("OGRNIP check overflow");
            }
            return (char) ('0' + rem);
        }
        throw new IllegalArgumentException("OGRN body must be 12 or 14 digits");
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 14) {
            digits = digits.substring(0, 14);
        } else if (digits.length() >= 12) {
            digits = digits.substring(0, 12);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
