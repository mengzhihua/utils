package com.mengzhihua.utils.common.validate;


/**
 * Dutch BSN (elfproef). Weights {@code 9,8,7,6,5,4,3,2,-1};
 * sum must be a non-zero multiple of 11. Sample {@code 111222333}.
 */
public final class BsnUtil {

    private static final int[] WEIGHTS = {9, 8, 7, 6, 5, 4, 3, 2, -1};

    private BsnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (digits.length() == 8) {
            digits = "0" + digits;
        }
        if (!digits.matches("[1-9]\\d{8}") && !digits.matches("0[1-9]\\d{7}")) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        return sum != 0 && sum % 11 == 0;
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9 && isValid(digits)) {
            return digits;
        }
        if (digits.length() != 8 || !digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("BSN body must be 8 digits");
        }
        for (int check = 0; check <= 9; check++) {
            String candidate = digits + check;
            if (isValid(candidate)) {
                return candidate;
            }
        }
        throw new IllegalArgumentException("BSN body has no valid check digit");
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
