package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Swedish personnummer (10/12 digits, Luhn). Sample {@code 19811218-9876}.
 */
public final class PersonnummerUtil {

    private PersonnummerUtil() {
    }

    public static boolean isValid(String value) {
        String ten = tenDigits(value);
        return ten.matches("\\d{10}") && CheckDigitUtil.luhn(ten) && validDate(ten);
    }

    public static char checkDigit(String body9) {
        String digits = tenDigits(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("personnummer body must be 9 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body9) {
        String digits = tenDigits(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigit(digits);
    }

    public static boolean female(String value) {
        String ten = tenDigits(value);
        if (ten.length() != 10) {
            throw new IllegalArgumentException("personnummer must be 10 digits");
        }
        return (ten.charAt(8) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String compact = normalize(value);
        String ten = tenDigits(value);
        if (ten.length() != 10) {
            throw new IllegalArgumentException("personnummer must be 10 digits");
        }
        int year;
        if (compact.length() >= 12) {
            year = Integer.parseInt(compact.substring(0, 4));
        } else {
            int yy = Integer.parseInt(ten.substring(0, 2));
            int current = LocalDate.now().getYear() % 100;
            year = (yy > current ? 1900 : 2000) + yy;
        }
        int month = Integer.parseInt(ten.substring(2, 4));
        int day = Integer.parseInt(ten.substring(4, 6));
        if (day > 60) {
            day -= 60;
        }
        return LocalDate.of(year, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static String tenDigits(String value) {
        String digits = normalize(value);
        return digits.length() == 12 ? digits.substring(2) : digits;
    }

    private static boolean validDate(String ten) {
        int month = Integer.parseInt(ten.substring(2, 4));
        int day = Integer.parseInt(ten.substring(4, 6));
        if (day > 60) {
            day -= 60;
        }
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }
        int yy = Integer.parseInt(ten.substring(0, 2));
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
