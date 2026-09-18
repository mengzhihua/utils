package com.mengzhihua.utils.common.validate;


/**
 * Croatian OIB (11 digits, ISO 7064 MOD 11,10). Sample {@code 12345678903}.
 */
public final class OibUtil {

    private OibUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{11}") && checkDigit(digits.substring(0, 10)) == digits.charAt(10);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("OIB body must be 10 digits");
        }
        return CheckDigitUtil.iso7064Mod1110CheckDigit(digits);
    }

    public static String complete(String body10) {
        String digits = normalize(body10);
        if (digits.length() == 11) {
            digits = digits.substring(0, 10);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
