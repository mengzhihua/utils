package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Malaysian MyKad (12-digit NRIC). {@code YYMMDD-PP-###G}, last digit odd = male.
 * Sample {@code 900101-14-5671} (1990-01-01, Johor, male).
 */
public final class MyKadUtil {

    private MyKadUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{12}")) {
            return false;
        }
        int place = Integer.parseInt(digits.substring(6, 8));
        return place >= 1 && place <= 98 && validDate(digits);
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() != 12) {
            throw new IllegalArgumentException("MyKad must be 12 digits");
        }
        return (digits.charAt(11) - '0') % 2 == 0;
    }

    public static String placeCode(String value) {
        String digits = normalize(value);
        return digits.length() >= 8 ? digits.substring(6, 8) : "";
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 12) {
            throw new IllegalArgumentException("MyKad must be 12 digits");
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
