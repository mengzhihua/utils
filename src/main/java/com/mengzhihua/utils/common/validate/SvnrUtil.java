package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Austrian SVNR (Sozialversicherungsnummer). Check is the 4th digit:
 * weights {@code 3,7,9} on the serial and {@code 5,8,4,2,1,6} on {@code DDMMYY},
 * {@code check = sum % 11} (10 is invalid). Sample {@code 1237010180} (1980-01-01).
 */
public final class SvnrUtil {

    private static final int[] WEIGHTS = {3, 7, 9, 0, 5, 8, 4, 2, 1, 6};

    private SvnrUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{10}") || !validDate(digits)) {
            return false;
        }
        int check = weighted(digits);
        return check != 10 && check == digits.charAt(3) - '0';
    }

    public static char checkDigit(String body9) {
        String serialAndDate = nine(body9);
        String candidate = serialAndDate.substring(0, 3) + "0" + serialAndDate.substring(3);
        int check = weighted(candidate);
        if (check == 10) {
            throw new IllegalArgumentException("SVNR body has no valid check digit");
        }
        return (char) ('0' + check);
    }

    public static String complete(String body9) {
        String serialAndDate = nine(body9);
        return serialAndDate.substring(0, 3) + checkDigit(serialAndDate) + serialAndDate.substring(3);
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            throw new IllegalArgumentException("SVNR must be 10 digits");
        }
        int day = Integer.parseInt(digits.substring(4, 6));
        int month = Integer.parseInt(digits.substring(6, 8));
        int yy = Integer.parseInt(digits.substring(8, 10));
        int current = LocalDate.now().getYear() % 100;
        int year = (yy > current ? 1900 : 2000) + yy;
        return LocalDate.of(year, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static String nine(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            return digits.substring(0, 3) + digits.substring(4);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("SVNR body must be 9 digits (serial + DDMMYY)");
        }
        return digits;
    }

    private static int weighted(String digits) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            if (i == 3) {
                continue;
            }
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum % 11;
    }

    private static boolean validDate(String digits) {
        int day = Integer.parseInt(digits.substring(4, 6));
        int month = Integer.parseInt(digits.substring(6, 8));
        if (month < 1 || month > 12 || day < 1 || day > 31) {
            return false;
        }
        int yy = Integer.parseInt(digits.substring(8, 10));
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
