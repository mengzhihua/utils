package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Yugoslav JMBG (13 digits). Weights {@code 7,6,5,4,3,2} repeating;
 * check {@code 11 - sum % 11} ({@code 10/11→0}). Sample {@code 0101980500005}
 * (1980-01-01, male).
 */
public final class JmbgUtil {

    private static final int[] WEIGHTS = {7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    private JmbgUtil() {
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
            throw new IllegalArgumentException("JMBG body must be 12 digits");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = 11 - (sum % 11);
        return (char) ('0' + (rem >= 10 ? 0 : rem));
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
            throw new IllegalArgumentException("JMBG must be 13 digits");
        }
        return Integer.parseInt(digits.substring(9, 12)) >= 500;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            throw new IllegalArgumentException("JMBG must be 13 digits");
        }
        int day = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int year3 = Integer.parseInt(digits.substring(4, 7));
        int year = year3 >= 800 ? 1000 + year3 : 2000 + year3;
        return LocalDate.of(year, month, day);
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
