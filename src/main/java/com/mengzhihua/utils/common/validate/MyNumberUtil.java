package com.mengzhihua.utils.common.validate;


/**
 * Japanese Individual Number (マイナンバー). Left-to-right weights
 * {@code 7,6,5,4,3,2,7,6,5,4,3}; check {@code 0} if {@code sum % 11 <= 1}
 * else {@code 11 - (sum % 11)}. Sample {@code 123456789019}.
 */
public final class MyNumberUtil {

    private static final int[] WEIGHTS = {7, 6, 5, 4, 3, 2, 7, 6, 5, 4, 3};

    private MyNumberUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{12}") && checkDigit(digits.substring(0, 11)) == digits.charAt(11);
    }

    public static char checkDigit(String body11) {
        String digits = normalize(body11);
        if (digits.length() == 12) {
            digits = digits.substring(0, 11);
        }
        if (!digits.matches("\\d{11}")) {
            throw new IllegalArgumentException("My Number body must be 11 digits");
        }
        int sum = 0;
        for (int i = 0; i < 11; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        return (char) ('0' + (rem <= 1 ? 0 : 11 - rem));
    }

    public static String complete(String body11) {
        String digits = normalize(body11);
        if (digits.length() == 12) {
            digits = digits.substring(0, 11);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
