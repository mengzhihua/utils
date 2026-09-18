package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Ukrainian IPN / РНОКПП (10 digits). First 5 digits are days since 1900-01-01;
 * 9th digit odd = male. Weights {@code -1,5,7,9,4,6,10,5,7}.
 * Sample {@code 2922000110} (1980-01-01, male).
 */
public final class IpnUtil {

    private static final int[] WEIGHTS = {-1, 5, 7, 9, 4, 6, 10, 5, 7};

    private IpnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{10}") && validDate(digits)
                && checkDigit(digits.substring(0, 9)) == digits.charAt(9);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("IPN body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = Math.floorMod(sum, 11);
        return (char) ('0' + (rem == 10 ? 0 : rem));
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigit(digits);
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            throw new IllegalArgumentException("IPN must be 10 digits");
        }
        return (digits.charAt(8) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            throw new IllegalArgumentException("IPN must be 10 digits");
        }
        int days = Integer.parseInt(digits.substring(0, 5));
        return LocalDate.of(1900, 1, 1).plusDays(days - 1L);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean validDate(String digits) {
        try {
            LocalDate date = birthDate(digits);
            return !date.isBefore(LocalDate.of(1900, 1, 1)) && !date.isAfter(LocalDate.of(2099, 12, 31));
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
