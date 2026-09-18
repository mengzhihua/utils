package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;

/**
 * Lithuanian asmens kodas (11 digits). Primary weights {@code 1..9,1};
 * fallback {@code 3,4,5,6,7,8,9,1,2,3}. Sample {@code 33309240064} (1933-09-24, male).
 */
public final class LithuanianAkUtil {

    private static final int[] PRIMARY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
    private static final int[] SECONDARY = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

    private LithuanianAkUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-6]\\d{10}") && validDate(digits)
                && checkDigit(digits.substring(0, 10)) == digits.charAt(10);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("asmens kodas body must be 10 digits");
        }
        int rem = remainder(digits, PRIMARY);
        if (rem == 10) {
            rem = remainder(digits, SECONDARY);
            if (rem == 10) {
                rem = 0;
            }
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        return digits + checkDigit(digits);
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() != 11) {
            throw new IllegalArgumentException("asmens kodas must be 11 digits");
        }
        return (digits.charAt(0) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() != 11) {
            throw new IllegalArgumentException("asmens kodas must be 11 digits");
        }
        int century = switch (digits.charAt(0)) {
            case '1', '2' -> 1800;
            case '3', '4' -> 1900;
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

    private static int remainder(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        return sum % 11;
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
