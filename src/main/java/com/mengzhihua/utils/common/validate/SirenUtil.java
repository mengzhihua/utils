package com.mengzhihua.utils.common.validate;

/**
 * French SIREN (9-digit Luhn). Sample {@code 732829320} (La Poste).
 */
public final class SirenUtil {

    private SirenUtil() {
    }

    public static boolean isValid(String siren) {
        String digits = normalize(siren);
        return digits.matches("\\d{9}") && CheckDigitUtil.luhn(digits);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("SIREN body must be 8 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body8) {
        return normalize(body8) + checkDigit(body8);
    }

    public static String normalize(String siren) {
        return siren == null ? "" : siren.replaceAll("\\D", "");
    }
}
