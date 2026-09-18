package com.mengzhihua.utils.common.validate;


/**
 * Indian Aadhaar (12 digits, Verhoeff). Sample {@code 234123412346}.
 */
public final class AadhaarUtil {

    private AadhaarUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[2-9]\\d{11}") && CheckDigitUtil.verhoeff(digits);
    }

    public static char checkDigit(String body11) {
        String digits = normalize(body11);
        if (!digits.matches("[2-9]\\d{10}")) {
            throw new IllegalArgumentException("Aadhaar body must be 11 digits starting 2-9");
        }
        return CheckDigitUtil.verhoeffCheckDigit(digits);
    }

    public static String complete(String body11) {
        return normalize(body11) + checkDigit(body11);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
