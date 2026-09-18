package com.mengzhihua.utils.common.validate;


/**
 * СНИЛС (Russian pension insurance number). Weights {@code 9..1}, mod 101.
 * Sample {@code 112-233-445 95}.
 */
public final class SnilsUtil {

    private SnilsUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{11}") && !digits.startsWith("000000000")
                && checkDigits(digits.substring(0, 9)).equals(digits.substring(9));
    }

    public static String checkDigits(String body9) {
        String digits = normalize(body9);
        if (digits.length() >= 11) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("SNILS body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * (9 - i);
        }
        if (sum < 100) {
            return String.format("%02d", sum);
        }
        if (sum == 100 || sum == 101) {
            return "00";
        }
        int rem = sum % 101;
        return rem == 100 || rem == 101 ? "00" : String.format("%02d", rem);
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 9) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigits(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
