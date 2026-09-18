package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Korean resident registration number (주민등록번호). Weights
 * {@code 2,3,4,5,6,7,8,9,2,3,4,5}, check {@code (11 - sum % 11) % 10}.
 * Sample {@code 900101-1234568} (1990-01-01, male).
 */
public final class RrnUtil {

    private static final int[] WEIGHTS = {2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4, 5};

    private RrnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{13}") && validDate(digits)
                && checkDigit(digits.substring(0, 12)) == digits.charAt(12);
    }

    public static char checkDigit(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(0, 12);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("RRN body must be 12 digits");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + ((11 - (sum % 11)) % 10));
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
            throw new IllegalArgumentException("RRN must be 13 digits");
        }
        return (digits.charAt(6) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            throw new IllegalArgumentException("RRN must be 13 digits");
        }
        int yy = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        int century = switch (digits.charAt(6)) {
            case '1', '2', '5', '6' -> 1900;
            case '3', '4', '7', '8' -> 2000;
            default -> 1800;
        };
        return LocalDate.of(century + yy, month, day);
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
        try {
            birthDate(digits);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
