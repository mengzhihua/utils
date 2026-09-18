package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Danish CPR. Weights {@code 4,3,2,7,6,5,4,3,2,1}, {@code sum % 11 == 0}.
 * Sample {@code 010170-0003} (1970-01-01, male).
 */
public final class CprUtil {

    private static final int[] WEIGHTS = {4, 3, 2, 7, 6, 5, 4, 3, 2, 1};

    private CprUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{10}") && checksum(digits) && validDate(digits);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("CPR body must be 9 digits");
        }
        for (int check = 0; check <= 9; check++) {
            if (checksum(digits + check)) {
                return (char) ('0' + check);
            }
        }
        throw new IllegalArgumentException("CPR body has no valid check digit");
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
            throw new IllegalArgumentException("CPR must be 10 digits");
        }
        return (digits.charAt(9) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            throw new IllegalArgumentException("CPR must be 10 digits");
        }
        int day = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int yy = Integer.parseInt(digits.substring(4, 6));
        int serialHead = digits.charAt(6) - '0';
        int century = serialHead <= 3 ? 1900 : (yy <= 36 ? 2000 : 1900);
        return LocalDate.of(century + yy, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean checksum(String digits) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum % 11 == 0;
    }

    private static boolean validDate(String digits) {
        int day = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }
        int yy = Integer.parseInt(digits.substring(4, 6));
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
