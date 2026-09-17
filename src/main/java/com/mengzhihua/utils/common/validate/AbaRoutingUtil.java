package com.mengzhihua.utils.common.validate;

/**
 * ABA routing number (US bank, 9 digits). Sample {@code 021000021} (Chase).
 */
public final class AbaRoutingUtil {

    private AbaRoutingUtil() {
    }

    public static boolean isValid(String number) {
        String digits = normalize(number);
        if (!digits.matches("\\d{9}")) {
            return false;
        }
        int sum = 3 * (d(digits, 0) + d(digits, 3) + d(digits, 6))
                + 7 * (d(digits, 1) + d(digits, 4) + d(digits, 7))
                + (d(digits, 2) + d(digits, 5) + d(digits, 8));
        return sum % 10 == 0;
    }

    public static String normalize(String number) {
        return number == null ? "" : number.replaceAll("\\D", "");
    }

    private static int d(String digits, int i) {
        return digits.charAt(i) - '0';
    }
}
