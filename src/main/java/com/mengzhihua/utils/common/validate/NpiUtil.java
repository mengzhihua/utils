package com.mengzhihua.utils.common.validate;

/**
 * US National Provider Identifier (CMS Luhn with prefix {@code 80840}). Sample {@code 1234567893}.
 */
public final class NpiUtil {

    private NpiUtil() {
    }

    public static boolean isValid(String npi) {
        String digits = normalize(npi);
        if (!digits.matches("\\d{10}")) {
            return false;
        }
        return CheckDigitUtil.luhn("80840" + digits);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("NPI body must be 9 digits");
        }
        return CheckDigitUtil.luhnCheckDigit("80840" + digits);
    }

    public static String complete(String body9) {
        return normalize(body9) + checkDigit(body9);
    }

    public static String normalize(String npi) {
        return npi == null ? "" : npi.replaceAll("\\D", "");
    }
}
