package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Bulgarian EGN (10 digits). Weights {@code 2,4,8,5,10,9,7,3,6};
 * month {@code +20} = 1800s, {@code +40} = 2000s. Sample {@code 8001010008} (1980-01-01, female).
 */
public final class EgnUtil {

    private static final int[] WEIGHTS = {2, 4, 8, 5, 10, 9, 7, 3, 6};

    private EgnUtil() {
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
            throw new IllegalArgumentException("EGN body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
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
            throw new IllegalArgumentException("EGN must be 10 digits");
        }
        return (digits.charAt(8) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            throw new IllegalArgumentException("EGN must be 10 digits");
        }
        int yy = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        int century;
        if (month > 40) {
            century = 2000;
            month -= 40;
        } else if (month > 20) {
            century = 1800;
            month -= 20;
        } else {
            century = 1900;
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
