package com.mengzhihua.utils.common.validate;


/**
 * Turkish VKN (Vergi Kimlik Numarası). Sample {@code 4540536920}.
 */
public final class VknUtil {

    private VknUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{10}") && checkDigit(digits.substring(0, 9)) == digits.charAt(9);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("VKN body must be 9 digits");
        }
        int sum = 0;
        for (int i = 1; i <= 9; i++) {
            int n = digits.charAt(9 - i) - '0';
            int c1 = (n + i) % 10;
            if (c1 == 0) {
                continue;
            }
            int c2 = (int) ((c1 * (1L << i)) % 9);
            sum += c2 == 0 ? 9 : c2;
        }
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
