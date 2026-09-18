package com.mengzhihua.utils.common.validate;


/**
 * Australian Business Number. Sample {@code 51 824 753 556}.
 */
public final class AbnUtil {

    private static final int[] WEIGHTS = {10, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19};

    private AbnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{11}")) {
            return false;
        }
        int sum = (digits.charAt(0) - '0' - 1) * WEIGHTS[0];
        for (int i = 1; i < 11; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum % 89 == 0;
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
