package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Czech / Slovak rodné číslo. 9 digits (pre-1954) or 10 digits divisible by 11.
 * Female month {@code +50}. Sample {@code 680101/0007} (1968-01-01, male).
 */
public final class RodneCisloUtil {

    private RodneCisloUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{9,10}") || !validDate(digits)) {
            return false;
        }
        if (digits.length() == 9) {
            return true;
        }
        long number = Long.parseLong(digits);
        if (number % 11 == 0) {
            return true;
        }
        return Long.parseLong(digits.substring(0, 9)) % 11 == 10 && digits.charAt(9) == '0';
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("rodné číslo body must be 9 digits");
        }
        int rem = (int) (Long.parseLong(digits) % 11);
        char check = rem == 10 ? '0' : (char) ('0' + rem);
        return digits + check;
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() < 6) {
            throw new IllegalArgumentException("rodné číslo too short");
        }
        int month = Integer.parseInt(digits.substring(2, 4));
        return month >= 50;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() < 6) {
            throw new IllegalArgumentException("rodné číslo too short");
        }
        int yy = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        if (month >= 70) {
            month -= 70;
        } else if (month >= 50) {
            month -= 50;
        } else if (month >= 20) {
            month -= 20;
        }
        int century;
        if (digits.length() == 9) {
            century = 1900;
        } else {
            century = yy >= 54 ? 1900 : 2000;
        }
        return LocalDate.of(century + yy, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean validDate(String digits) {
        try {
            birthDate(digits);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
