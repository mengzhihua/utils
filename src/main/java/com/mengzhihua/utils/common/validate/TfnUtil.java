package com.mengzhihua.utils.common.validate;


/**
 * Australian Tax File Number (9 digits). Sample {@code 123456782}.
 */
public final class TfnUtil {

    private static final int[] WEIGHTS = {1, 4, 3, 7, 5, 8, 6, 9, 10};

    private TfnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{8,9}")) {
            return false;
        }
        if (digits.length() == 8) {
            int sum = 0;
            int[] w8 = {10, 7, 8, 4, 6, 3, 5, 1};
            for (int i = 0; i < 8; i++) {
                sum += (digits.charAt(i) - '0') * w8[i];
            }
            return sum % 11 == 0;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum % 11 == 0;
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
