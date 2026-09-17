package com.mengzhihua.utils.common.validate;

/**
 * UK NHS number (Mod 11). Sample {@code 943 476 5919}.
 */
public final class NhsNumberUtil {

    private NhsNumberUtil() {
    }

    public static boolean isValid(String number) {
        String digits = normalize(number);
        if (!digits.matches("\\d{10}")) {
            return false;
        }
        int check = checkDigit(digits.substring(0, 9));
        return check >= 0 && check == digits.charAt(9) - '0';
    }

    public static int checkDigit(String body9) {
        String digits = normalize(body9);
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("NHS body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * (10 - i);
        }
        int rem = sum % 11;
        int check = 11 - rem;
        if (check == 11) {
            return 0;
        }
        if (check == 10) {
            return -1;
        }
        return check;
    }

    public static String complete(String body9) {
        int check = checkDigit(body9);
        if (check < 0) {
            throw new IllegalArgumentException("NHS number has no valid check digit");
        }
        return normalize(body9) + check;
    }

    public static String normalize(String number) {
        return number == null ? "" : number.replaceAll("\\D", "");
    }
}
