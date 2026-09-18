package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * South African ID (13 digits, Luhn). Sequence {@code 0000-4999} female,
 * {@code 5000-9999} male. Sample {@code 8001015009087} (1980-01-01, male).
 */
public final class SaIdUtil {

    private SaIdUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{13}") && CheckDigitUtil.luhn(digits) && validDate(digits);
    }

    public static char checkDigit(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(0, 12);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("SA ID body must be 12 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(0, 12);
        }
        return digits + checkDigit(digits);
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            throw new IllegalArgumentException("SA ID must be 13 digits");
        }
        return Integer.parseInt(digits.substring(6, 10)) < 5000;
    }

    public static boolean citizen(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            throw new IllegalArgumentException("SA ID must be 13 digits");
        }
        return digits.charAt(10) == '0';
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            throw new IllegalArgumentException("SA ID must be 13 digits");
        }
        int yy = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        int current = LocalDate.now().getYear() % 100;
        int year = (yy > current ? 1900 : 2000) + yy;
        return LocalDate.of(year, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean validDate(String digits) {
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }
        int yy = Integer.parseInt(digits.substring(0, 2));
        try {
            LocalDate.of(1900 + yy, month, day);
            return true;
        } catch (RuntimeException ex) {
            try {
                LocalDate.of(2000 + yy, month, day);
                return true;
            } catch (RuntimeException ignored) {
                return false;
            }
        }
    }
}
