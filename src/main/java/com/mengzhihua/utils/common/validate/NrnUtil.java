package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Belgian national register number (Rijksregisternummer).
 * Check {@code 97 - (n % 97)}; year 2000+ uses {@code n + 2_000_000_000}.
 * Sample {@code 93.05.18-223.61} (1993-05-18, male).
 */
public final class NrnUtil {

    private NrnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{11}") || !validDate(digits)) {
            return false;
        }
        return checksum(digits, false) || checksum(digits, true);
    }

    public static String checkDigits(String body9) {
        String digits = nine(body9);
        return String.format("%02d", 97 - (Integer.parseInt(digits) % 97));
    }

    public static String complete(String body9) {
        String digits = nine(body9);
        return digits + checkDigits(digits);
    }

    public static String complete2000(String body9) {
        String digits = nine(body9);
        long n = 2_000_000_000L + Integer.parseInt(digits);
        return digits + String.format("%02d", 97 - (int) (n % 97));
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() != 11) {
            throw new IllegalArgumentException("NRN must be 11 digits");
        }
        return Integer.parseInt(digits.substring(6, 9)) % 2 == 0;
    }

    public static boolean bornIn2000s(String value) {
        String digits = normalize(value);
        return checksum(digits, true) && !checksum(digits, false);
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 11) {
            throw new IllegalArgumentException("NRN must be 11 digits");
        }
        int yy = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        int century = bornIn2000s(value) ? 2000 : 1900;
        return LocalDate.of(century + yy, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static String nine(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 11) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("NRN body must be 9 digits");
        }
        return digits;
    }

    private static boolean checksum(String digits, boolean year2000) {
        long n = Long.parseLong(digits.substring(0, 9));
        if (year2000) {
            n += 2_000_000_000L;
        }
        return Integer.parseInt(digits.substring(9)) == 97 - (int) (n % 97);
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
