package com.mengzhihua.utils.common.validate;


/**
 * Ecuador CI (Cédula de identidad). 10 digits, province + Luhn-like fold.
 * Sample {@code 171430710-3}.
 */
public final class EcCiUtil {

    private EcCiUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{10}") && validProvince(digits.substring(0, 2))
                && digits.charAt(2) <= '6' && checksum(digits) == 0;
    }

    public static String checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() >= 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("Ecuador CI body must be 9 digits");
        }
        return String.valueOf((10 - checksum(digits) % 10) % 10);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 9) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 9) + '-' + digits.substring(9);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    static boolean validProvince(String two) {
        return two.compareTo("01") >= 0 && two.compareTo("24") <= 0
                || "30".equals(two) || "50".equals(two);
    }

    static int checksum(String digits) {
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            int product = ((i % 2 == 0) ? 2 : 1) * (digits.charAt(i) - '0');
            sum += product > 9 ? product - 9 : product;
        }
        return sum % 10;
    }
}
