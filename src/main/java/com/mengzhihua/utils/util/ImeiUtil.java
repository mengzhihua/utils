package com.mengzhihua.utils.util;

/**
 * 15-digit IMEI Luhn check and generation.
 */
public final class ImeiUtil {

    private ImeiUtil() {
    }

    public static boolean isValid(String imei) {
        String digits = normalize(imei);
        if (digits.length() != 15) {
            return false;
        }
        for (int i = 0; i < 15; i++) {
            if (!Character.isDigit(digits.charAt(i))) {
                return false;
            }
        }
        return luhn(digits);
    }

    public static String normalize(String imei) {
        return imei == null ? "" : imei.replaceAll("[^0-9]", "");
    }

    public static char checkDigit(String body14) {
        String digits = normalize(body14);
        if (digits.length() != 14) {
            throw new IllegalArgumentException("IMEI body must be 14 digits");
        }
        for (int check = 0; check <= 9; check++) {
            if (luhn(digits + check)) {
                return (char) ('0' + check);
            }
        }
        throw new IllegalStateException("IMEI check digit not found");
    }

    public static String generate() {
        String body = RandomUtil.digits(14);
        return body + checkDigit(body);
    }

    private static boolean luhn(String digits) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = Character.digit(digits.charAt(i), 10);
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
}
