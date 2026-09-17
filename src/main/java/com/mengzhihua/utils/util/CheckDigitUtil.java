package com.mengzhihua.utils.util;

/**
 * Luhn / Verhoeff / Damm check digits (Apache Commons Validator).
 */
public final class CheckDigitUtil {

    private static final int[][] VERHOEFF_D = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {1, 2, 3, 4, 0, 6, 7, 8, 9, 5},
            {2, 3, 4, 0, 1, 7, 8, 9, 5, 6},
            {3, 4, 0, 1, 2, 8, 9, 5, 6, 7},
            {4, 0, 1, 2, 3, 9, 5, 6, 7, 8},
            {5, 9, 8, 7, 6, 0, 4, 3, 2, 1},
            {6, 5, 9, 8, 7, 1, 0, 4, 3, 2},
            {7, 6, 5, 9, 8, 2, 1, 0, 4, 3},
            {8, 7, 6, 5, 9, 3, 2, 1, 0, 4},
            {9, 8, 7, 6, 5, 4, 3, 2, 1, 0}
    };

    private static final int[][] VERHOEFF_P = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
            {1, 5, 7, 6, 2, 8, 3, 0, 9, 4},
            {5, 8, 0, 3, 7, 9, 6, 1, 4, 2},
            {8, 9, 1, 6, 0, 4, 3, 5, 2, 7},
            {9, 4, 5, 3, 1, 2, 6, 8, 7, 0},
            {4, 2, 8, 6, 5, 7, 9, 3, 0, 1},
            {2, 7, 9, 3, 8, 0, 6, 4, 1, 5},
            {7, 0, 4, 6, 9, 1, 3, 2, 5, 8}
    };

    private static final int[] VERHOEFF_INV = {0, 4, 3, 2, 1, 5, 6, 7, 8, 9};

    private static final int[][] DAMM = {
            {0, 3, 1, 7, 5, 9, 8, 6, 4, 2},
            {7, 0, 9, 2, 1, 5, 4, 8, 6, 3},
            {4, 2, 0, 6, 8, 7, 1, 3, 5, 9},
            {1, 7, 5, 0, 9, 8, 3, 4, 2, 6},
            {6, 1, 2, 3, 0, 4, 5, 9, 7, 8},
            {3, 6, 7, 4, 2, 0, 9, 5, 8, 1},
            {5, 8, 6, 9, 7, 2, 0, 1, 3, 4},
            {8, 9, 4, 5, 3, 6, 2, 0, 1, 7},
            {9, 4, 3, 8, 6, 1, 7, 2, 0, 5},
            {2, 5, 8, 1, 4, 3, 6, 7, 9, 0}
    };

    private CheckDigitUtil() {
    }

    public static boolean luhn(String digits) {
        String value = digitsOnly(digits);
        if (value.isEmpty()) {
            return false;
        }
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = value.length() - 1; i >= 0; i--) {
            int n = value.charAt(i) - '0';
            if (doubleDigit) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }

    public static char luhnCheckDigit(String body) {
        String value = digitsOnly(body);
        for (char d = '0'; d <= '9'; d++) {
            if (luhn(value + d)) {
                return d;
            }
        }
        throw new IllegalStateException("luhn check digit not found");
    }

    public static boolean verhoeff(String digits) {
        String value = digitsOnly(digits);
        if (value.isEmpty()) {
            return false;
        }
        int check = 0;
        for (int i = 0; i < value.length(); i++) {
            int digit = value.charAt(value.length() - 1 - i) - '0';
            check = VERHOEFF_D[check][VERHOEFF_P[i % 8][digit]];
        }
        return check == 0;
    }

    public static char verhoeffCheckDigit(String body) {
        String value = digitsOnly(body);
        int check = 0;
        for (int i = 0; i < value.length(); i++) {
            int digit = value.charAt(value.length() - 1 - i) - '0';
            check = VERHOEFF_D[check][VERHOEFF_P[(i + 1) % 8][digit]];
        }
        return (char) ('0' + VERHOEFF_INV[check]);
    }

    public static boolean damm(String digits) {
        String value = digitsOnly(digits);
        if (value.isEmpty()) {
            return false;
        }
        int interim = 0;
        for (int i = 0; i < value.length(); i++) {
            interim = DAMM[interim][value.charAt(i) - '0'];
        }
        return interim == 0;
    }

    public static char dammCheckDigit(String body) {
        String value = digitsOnly(body);
        int interim = 0;
        for (int i = 0; i < value.length(); i++) {
            interim = DAMM[interim][value.charAt(i) - '0'];
        }
        return (char) ('0' + interim);
    }

    private static String digitsOnly(String digits) {
        if (digits == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(digits.length());
        for (int i = 0; i < digits.length(); i++) {
            char c = digits.charAt(i);
            if (c >= '0' && c <= '9') {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
