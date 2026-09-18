package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Polish PESEL. Sample {@code 44051401359} (1944-05-14). 10th digit odd=male, even=female.
 */
public final class PeselUtil {

    private static final int[] WEIGHTS = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

    private PeselUtil() {
    }

    public static boolean isValid(String pesel) {
        String digits = normalize(pesel);
        if (!digits.matches("\\d{11}")) {
            return false;
        }
        return checkDigit(digits.substring(0, 10)) == digits.charAt(10);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("PESEL body must be 10 digits");
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + (10 - (sum % 10)) % 10);
    }

    public static String complete(String body10) {
        return normalize(body10) + checkDigit(body10);
    }

    public static LocalDate birthDate(String pesel) {
        String digits = normalize(pesel);
        if (!digits.matches("\\d{11}")) {
            throw new IllegalArgumentException("PESEL must be 11 digits");
        }
        int year = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int day = Integer.parseInt(digits.substring(4, 6));
        int century;
        if (month >= 1 && month <= 12) {
            century = 1900;
        } else if (month >= 21 && month <= 32) {
            century = 2000;
            month -= 20;
        } else if (month >= 41 && month <= 52) {
            century = 2100;
            month -= 40;
        } else if (month >= 61 && month <= 72) {
            century = 2200;
            month -= 60;
        } else if (month >= 81 && month <= 92) {
            century = 1800;
            month -= 80;
        } else {
            throw new IllegalArgumentException("invalid PESEL month");
        }
        return LocalDate.of(century + year, month, day);
    }

    public static boolean female(String pesel) {
        String digits = normalize(pesel);
        if (!digits.matches("\\d{11}")) {
            throw new IllegalArgumentException("PESEL must be 11 digits");
        }
        return (digits.charAt(9) - '0') % 2 == 0;
    }

    public static String normalize(String pesel) {
        return pesel == null ? "" : pesel.replaceAll("\\D", "");
    }
}
