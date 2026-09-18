package com.mengzhihua.utils.common.validate;


/**
 * Swedish organisationsnummer (10 digits, Luhn). Month field {@code >= 20}.
 * Sample {@code 556036-0793}.
 */
public final class SeOrgNrUtil {

    private SeOrgNrUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("[1-9]\\d{9}")) {
            return false;
        }
        int month = Integer.parseInt(digits.substring(2, 4));
        return month >= 20 && CheckDigitUtil.luhn(digits);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("organisationsnummer body must be 9 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
