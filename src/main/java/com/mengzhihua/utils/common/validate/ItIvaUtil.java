package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Italy Partita IVA. 11-digit Luhn, province 001–100 / 120 / 121 / 888 / 999.
 * Sample {@code IT 00743110157}.
 */
public final class ItIvaUtil {

    private ItIvaUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{11}") || Integer.parseInt(compact.substring(0, 7)) == 0) {
            return false;
        }
        return validProvince(compact.substring(7, 10)) && CheckDigitUtil.luhn(compact);
    }

    public static char checkDigit(String body10) {
        String digits = normalize(body10);
        if (digits.length() >= 11) {
            digits = digits.substring(0, 10);
        }
        if (!digits.matches("\\d{10}")) {
            throw new IllegalArgumentException("Italian IVA body must be 10 digits");
        }
        return CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 10) {
            digits = digits.substring(0, 10);
        }
        return digits + checkDigit(digits);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 11) {
            return value == null ? "" : value.trim();
        }
        return "IT " + compact;
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s.:-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("IT")) {
            compact = compact.substring(2);
        }
        return compact;
    }

    private static boolean validProvince(String province) {
        return province.compareTo("001") >= 0 && province.compareTo("100") <= 0
                || "120".equals(province) || "121".equals(province)
                || "888".equals(province) || "999".equals(province);
    }
}
