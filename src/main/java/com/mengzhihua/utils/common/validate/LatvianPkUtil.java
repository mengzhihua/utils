package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Latvian personas kods (11 digits). Weights {@code 1,6,3,7,9,10,5,8,4,2,1};
 * valid when {@code sum % 11 == 1}. Legacy sample {@code 111111-11111} (1911-11-11).
 */
public final class LatvianPkUtil {

    private static final int[] WEIGHTS = {1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

    private LatvianPkUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{11}") || !checksumOk(digits)) {
            return false;
        }
        return modern(digits) || validDate(digits);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("personas kods body must be 10 digits");
        }
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int check = Math.floorMod(1 - sum, 11);
        if (check == 10) {
            throw new IllegalArgumentException("personas kods body has no valid check digit");
        }
        return (char) ('0' + check);
    }

    public static String complete(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        return digits + checkDigit(digits);
    }

    public static boolean modern(String value) {
        return normalize(value).startsWith("32");
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 11 || modern(digits)) {
            throw new IllegalArgumentException("legacy personas kods must be 11 digits");
        }
        int day = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        int yy = Integer.parseInt(digits.substring(4, 6));
        int century = switch (digits.charAt(6)) {
            case '0' -> 1800;
            case '1' -> 1900;
            default -> 2000;
        };
        return LocalDate.of(century + yy, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean checksumOk(String digits) {
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum % 11 == 1;
    }

    private static boolean validDate(String digits) {
        char century = digits.charAt(6);
        if (century < '0' || century > '2') {
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
