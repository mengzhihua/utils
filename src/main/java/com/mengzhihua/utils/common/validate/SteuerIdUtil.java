package com.mengzhihua.utils.common.validate;


/**
 * German Steueridentifikationsnummer (11 digits, ISO 7064 MOD 11,10).
 * Sample {@code 86095742719}.
 */
public final class SteuerIdUtil {

    private SteuerIdUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("[1-9]\\d{10}")) {
            return false;
        }
        if (!hasOneDuplicate(digits.substring(0, 10))) {
            return false;
        }
        return checkDigit(digits.substring(0, 10)) == digits.charAt(10);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("Steuer-IdNr body must be 10 digits");
        }
        return CheckDigitUtil.iso7064Mod1110CheckDigit(digits);
    }

    public static String complete(String body10) {
        return normalize(body10) + checkDigit(body10);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean hasOneDuplicate(String body10) {
        int[] counts = new int[10];
        for (int i = 0; i < 10; i++) {
            counts[body10.charAt(i) - '0']++;
        }
        int missing = 0;
        int twice = 0;
        for (int count : counts) {
            if (count == 0) {
                missing++;
            } else if (count == 2) {
                twice++;
            } else if (count != 1) {
                return false;
            }
        }
        return missing == 1 && twice == 1;
    }
}
