package com.mengzhihua.utils.common.validate;


/**
 * Azerbaijan VÖEN. 10 digits, weights 4,1,8,6,2,7,5,3, last 1=legal 2=natural.
 * Sample {@code 1401555071}.
 */
public final class VoenUtil {

    private static final int[] WEIGHTS = {4, 1, 8, 6, 2, 7, 5, 3};

    private VoenUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{9}[12]") && checkDigit(digits) == digits.charAt(8);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() >= 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("VÖEN body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        if (rem > 9) {
            throw new IllegalArgumentException("VÖEN check digit overflow");
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        char type = '1';
        if (digits.length() >= 10 && (digits.charAt(9) == '1' || digits.charAt(9) == '2')) {
            type = digits.charAt(9);
        } else if (digits.length() == 9 && (digits.charAt(8) == '1' || digits.charAt(8) == '2')) {
            type = digits.charAt(8);
            digits = digits.substring(0, 8);
        }
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits) + type;
    }

    public static boolean legalPerson(String value) {
        String digits = normalize(value);
        return isValid(digits) && digits.charAt(9) == '1';
    }

    public static String normalize(String value) {
        String digits = value == null ? "" : value.replaceAll("\\D", "");
        return digits.length() == 9 ? '0' + digits : digits;
    }
}
