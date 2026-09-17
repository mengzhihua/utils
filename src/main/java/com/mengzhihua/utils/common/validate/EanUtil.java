package com.mengzhihua.utils.common.validate;

/**
 * GS1 GTIN checksum for UPC-A / EAN-8 / EAN-13 / GTIN-14.
 */
public final class EanUtil {

    private EanUtil() {
    }

    public static boolean isValid(String code) {
        String digits = normalize(code);
        int length = digits.length();
        if (length != 8 && length != 12 && length != 13 && length != 14) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(digits.charAt(i))) {
                return false;
            }
        }
        return checkDigit(digits.substring(0, length - 1)) == digits.charAt(length - 1);
    }

    public static String normalize(String code) {
        return code == null ? "" : code.replaceAll("\\D", "");
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.isEmpty()) {
            throw new IllegalArgumentException("EAN body is empty");
        }
        int sum = 0;
        int factor = 3;
        for (int i = digits.length() - 1; i >= 0; i--) {
            sum += Character.digit(digits.charAt(i), 10) * factor;
            factor = 4 - factor;
        }
        return (char) ('0' + (10 - (sum % 10)) % 10);
    }
}
