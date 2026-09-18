package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Icelandic kennitala (10 digits). Weights {@code 3,2,7,6,5,4,3,2};
 * last digit {@code 9}=1900s, {@code 0}=2000s. Sample {@code 120174-3399} (1974-01-12).
 */
public final class KennitalaUtil {

    private static final int[] WEIGHTS = {3, 2, 7, 6, 5, 4, 3, 2};

    private KennitalaUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{10}") && "90".indexOf(digits.charAt(9)) >= 0
                && validDate(digits) && checkDigit(digits.substring(0, 8)) == digits.charAt(8);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() >= 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("kennitala body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = 11 - (sum % 11);
        if (rem == 10) {
            throw new IllegalArgumentException("kennitala body has no valid check digit");
        }
        return (char) ('0' + (rem == 11 ? 0 : rem));
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits) + "9";
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            throw new IllegalArgumentException("kennitala must be 10 digits");
        }
        int day = Integer.parseInt(digits.substring(0, 2));
        if (day > 40) {
            day -= 40;
        }
        int month = Integer.parseInt(digits.substring(2, 4));
        int yy = Integer.parseInt(digits.substring(4, 6));
        int century = digits.charAt(9) == '0' ? 2000 : 1900;
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
