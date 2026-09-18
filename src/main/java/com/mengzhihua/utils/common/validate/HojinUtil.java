package com.mengzhihua.utils.common.validate;


/**
 * Japanese corporate number (法人番号). Check digit is the first of 13;
 * weights 1/2 from the right of the 12-digit body. Sample {@code 8700110005901}.
 */
public final class HojinUtil {

    private HojinUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-9]\\d{12}") && checkDigit(digits.substring(1)) == digits.charAt(0);
    }

    public static char checkDigit(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(1);
        }
        if (!digits.matches("\\d{12}")) {
            throw new IllegalArgumentException("hojin body must be 12 digits");
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int n = 12 - i;
            int weight = n % 2 == 0 ? 2 : 1;
            sum += (digits.charAt(i) - '0') * weight;
        }
        int rem = sum % 9;
        return (char) ('0' + (9 - rem));
    }

    public static String complete(String body12) {
        String digits = normalize(body12);
        if (digits.length() == 13) {
            digits = digits.substring(1);
        }
        return checkDigit(digits) + digits;
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
