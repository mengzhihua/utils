package com.mengzhihua.utils.common.validate;


/**
 * Norwegian fødselsnummer (11 digits, two MOD-11 checks). Sample {@code 11077941012}.
 */
public final class FodselsnummerUtil {

    private static final int[] W1 = {3, 7, 6, 1, 8, 9, 4, 5, 2};
    private static final int[] W2 = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};

    private FodselsnummerUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{11}")) {
            return false;
        }
        int k1 = mod11(digits, W1, 9);
        int k2 = mod11(digits, W2, 10);
        return k1 >= 0 && k2 >= 0 && k1 == digits.charAt(9) - '0' && k2 == digits.charAt(10) - '0'
                && validDate(digits);
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("fødselsnummer body must be 9 digits");
        }
        int k1 = mod11(digits, W1, 9);
        if (k1 < 0) {
            throw new IllegalArgumentException("fødselsnummer individual number is invalid");
        }
        int k2 = mod11(digits + k1, W2, 10);
        if (k2 < 0) {
            throw new IllegalArgumentException("fødselsnummer individual number is invalid");
        }
        return digits + k1 + k2;
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        if (digits.length() < 9) {
            throw new IllegalArgumentException("fødselsnummer is too short");
        }
        return (digits.charAt(8) - '0') % 2 == 0;
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static int mod11(String digits, int[] weights, int length) {
        int sum = 0;
        for (int i = 0; i < length; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        int rem = sum % 11;
        if (rem == 0) {
            return 0;
        }
        int check = 11 - rem;
        return check == 10 ? -1 : check;
    }

    private static boolean validDate(String digits) {
        int day = Integer.parseInt(digits.substring(0, 2));
        int month = Integer.parseInt(digits.substring(2, 4));
        if (day > 40) {
            day -= 40;
        }
        if (month > 40) {
            month -= 40;
        }
        return month >= 1 && month <= 12 && day >= 1 && day <= 31;
    }
}
