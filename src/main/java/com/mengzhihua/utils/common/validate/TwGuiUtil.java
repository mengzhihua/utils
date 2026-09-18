package com.mengzhihua.utils.common.validate;


/**
 * Taiwan GUI / 统一编号. Weights {@code 1,2,1,2,1,2,4,1}; digit-sum % 5 == 0.
 * Seventh digit {@code 7} also accepts {@code (sum+1)%5==0}. Sample {@code 53212539}.
 */
public final class TwGuiUtil {

    private static final int[] WEIGHTS = {1, 2, 1, 2, 1, 2, 4, 1};

    private TwGuiUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{8}")) {
            return false;
        }
        int sum = weightedDigitSum(digits);
        if (sum % 5 == 0) {
            return true;
        }
        return digits.charAt(6) == '7' && (sum + 1) % 5 == 0;
    }

    public static char checkDigit(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("GUI body must be 7 digits");
        }
        for (char d = '0'; d <= '9'; d++) {
            if (isValid(digits + d)) {
                return d;
            }
        }
        throw new IllegalArgumentException("GUI body has no valid check digit");
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

    private static int weightedDigitSum(String digits) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            int product = (digits.charAt(i) - '0') * WEIGHTS[i];
            sum += product / 10 + product % 10;
        }
        return sum;
    }
}
