package com.mengzhihua.utils.common.validate;


/**
 * Polish REGON (9 or 14 digits). 9-digit weights {@code 8,9,2,3,4,5,6,7};
 * 14-digit extra weights {@code 2,4,8,5,0,9,7,3,6,1,2,4,8}. Sample {@code 123456785}.
 */
public final class RegonUtil {

    private static final int[] WEIGHTS9 = {8, 9, 2, 3, 4, 5, 6, 7};
    private static final int[] WEIGHTS14 = {2, 4, 8, 5, 0, 9, 7, 3, 6, 1, 2, 4, 8};

    private RegonUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (digits.matches("\\d{9}")) {
            return checkDigit(digits.substring(0, 8), WEIGHTS9) == digits.charAt(8);
        }
        if (digits.matches("\\d{14}")) {
            return isValid(digits.substring(0, 9))
                    && checkDigit(digits.substring(0, 13), WEIGHTS14) == digits.charAt(13);
        }
        return false;
    }

    public static char checkDigit9(String body8) {
        return checkDigit(eightOrThirteen(body8, 8), WEIGHTS9);
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit9(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static String eightOrThirteen(String body, int length) {
        String digits = normalize(body);
        if (digits.length() == length + 1) {
            digits = digits.substring(0, length);
        }
        if (digits.length() != length) {
            throw new IllegalArgumentException("REGON body must be " + length + " digits");
        }
        return digits;
    }

    private static char checkDigit(String body, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += (body.charAt(i) - '0') * weights[i];
        }
        int rem = sum % 11;
        return (char) ('0' + (rem == 10 ? 0 : rem));
    }
}
