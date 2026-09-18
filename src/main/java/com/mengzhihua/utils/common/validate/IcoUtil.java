package com.mengzhihua.utils.common.validate;


/**
 * Czech / Slovak IČO (8 digits). Weights {@code 8,7,6,5,4,3,2},
 * check {@code (11 - sum % 11) % 10}. Sample {@code 25596641}.
 */
public final class IcoUtil {

    private static final int[] WEIGHTS = {8, 7, 6, 5, 4, 3, 2};

    private IcoUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{8}") && checkDigit(digits.substring(0, 7)) == digits.charAt(7);
    }

    public static char checkDigit(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("IČO body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return (char) ('0' + ((11 - (sum % 11)) % 10));
    }

    public static String complete(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
