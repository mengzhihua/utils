package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Romanian CNP (13 digits). Weights {@code 2,7,9,1,4,6,3,5,8,2,7,9};
 * check {@code sum % 11} ({@code 10→1}). Sample {@code 1800101010015} (1980-01-01, male).
 */
public final class CnpUtil {

    private static final int[] WEIGHTS = {2, 7, 9, 1, 4, 6, 3, 5, 8, 2, 7, 9};

    private CnpUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-8]\\d{12}") && validDate(digits)
                && checkDigit(digits.substring(0, 12)) == digits.charAt(12);
    }

    public static char checkDigit(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(0, 12);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("CNP body must be 12 digits");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        return (char) ('0' + (rem == 10 ? 1 : rem));
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
            throw new IllegalArgumentException("CNP must be 13 digits");
        }
        return (digits.charAt(0) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            throw new IllegalArgumentException("CNP must be 13 digits");
        }
        int sex = digits.charAt(0) - '0';
        int century = switch (sex) {
            case 1, 2 -> 1900;
            case 3, 4 -> 1800;
            default -> 2000;
        };
        int yy = Integer.parseInt(digits.substring(1, 3));
        int month = Integer.parseInt(digits.substring(3, 5));
        int day = Integer.parseInt(digits.substring(5, 7));
        return LocalDate.of(century + yy, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean validDate(String digits) {
        int month = Integer.parseInt(digits.substring(3, 5));
        int day = Integer.parseInt(digits.substring(5, 7));
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
