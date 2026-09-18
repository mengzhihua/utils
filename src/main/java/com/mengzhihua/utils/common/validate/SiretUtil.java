package com.mengzhihua.utils.common.validate;

/**
 * French SIRET (14-digit Luhn). Sample {@code 73282932000074}.
 */
public final class SiretUtil {

    private SiretUtil() {
    }

    public static boolean isValid(String siret) {
        String digits = normalize(siret);
        return digits.matches("\\d{14}") && CheckDigitUtil.luhn(digits) && SirenUtil.isValid(digits.substring(0, 9));
    }

    public static char checkDigit(String body13) {
        String digits = normalize(body13);
        if (!digits.matches("\\d{13}")) {
            throw new IllegalArgumentException("SIRET body must be 13 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body13) {
        return normalize(body13) + checkDigit(body13);
    }

    public static String siren(String siret) {
        String digits = normalize(siret);
        return digits.length() >= 9 ? digits.substring(0, 9) : "";
    }

    public static String normalize(String siret) {
        return siret == null ? "" : siret.replaceAll("\\D", "");
    }
}
