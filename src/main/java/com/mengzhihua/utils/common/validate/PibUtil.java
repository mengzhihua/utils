package com.mengzhihua.utils.common.validate;


/**
 * Serbian PIB (ISO 7064 MOD 11,10). Sample {@code 101134702}.
 */
public final class PibUtil {

    private PibUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-9]\\d{8}") && checkDigit(digits.substring(0, 8)) == digits.charAt(8);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("[1-9]\\d{7}")) {
            throw new IllegalArgumentException("PIB body must be 8 digits starting 1-9");
        }
        return CheckDigitUtil.iso7064Mod1110CheckDigit(digits);
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
