package com.mengzhihua.utils.common.validate;


import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Cuba NI (Número de identidad). 11 digits, encodes birth date and gender.
 * Sample {@code 91021027775} (1991-02-10, F).
 */
public final class CuNiUtil {

    private CuNiUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{11}")) {
            return false;
        }
        try {
            birthDate(digits);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 11) {
            throw new IllegalArgumentException("Cuba NI must be 11 digits");
        }
        int year = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        char century = digits.charAt(6);
        if (century == '9') {
            year += 1800;
        } else if (century >= '0' && century <= '5') {
            year += 1900;
        } else {
            year += 2000;
        }
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Cuba NI has an invalid birth date", ex);
        }
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() != 11) {
            throw new IllegalArgumentException("Cuba NI must be 11 digits");
        }
        return (digits.charAt(9) - '0') % 2 != 0;
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
