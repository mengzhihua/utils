package com.mengzhihua.utils.common.validate;


/**
 * Peruvian DNI (8 digits + check). Weights {@code 3,2,7,6,5,4,3,2};
 * numeric map {@code 6,7,8,9,0,1,1,2,3,4,5}. Sample {@code 713903006}.
 */
public final class PeDniUtil {

    private static final int[] WEIGHTS = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final char[] NUMERIC = {'6', '7', '8', '9', '0', '1', '1', '2', '3', '4', '5'};
    private static final char[] LETTERS = {'K', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

    private PeDniUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{8}[0-9A-JK]")) {
            return false;
        }
        int index = checkIndex(compact.substring(0, 8));
        char last = compact.charAt(8);
        return last == NUMERIC[index] || last == LETTERS[index];
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("DNI body must be 8 digits");
        }
        return NUMERIC[checkIndex(digits)];
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[^0-9A-Za-z]", "").toUpperCase();
    }

    private static int checkIndex(String digits) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int key = 11 - (sum % 11);
        return key == 11 ? 0 : key;
    }
}
