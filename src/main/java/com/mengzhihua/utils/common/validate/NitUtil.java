package com.mengzhihua.utils.common.validate;


/**
 * Colombian NIT. Weights from the right {@code 3,7,13,17,19,23,29,37,...};
 * check {@code sum % 11} ({@code 0/1} kept, else {@code 11 - rem}).
 * Sample {@code 800197268-4}.
 */
public final class NitUtil {

    private static final int[] FACTORS = {3, 7, 13, 17, 19, 23, 29, 37, 41, 43, 47, 53, 59, 67, 71};

    private NitUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{8,16}")) {
            return false;
        }
        return checkDigit(digits.substring(0, digits.length() - 1)) == digits.charAt(digits.length() - 1);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (!digits.matches("\\d{7,15}")) {
            throw new IllegalArgumentException("NIT body must be 7 to 15 digits");
        }
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += (digits.charAt(digits.length() - 1 - i) - '0') * FACTORS[i];
        }
        int rem = sum % 11;
        return (char) ('0' + (rem <= 1 ? rem : 11 - rem));
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (digits.length() >= 9 && isValid(digits)) {
            return digits;
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
