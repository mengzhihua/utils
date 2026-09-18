package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Tunisia MF (Matricule Fiscal). 8 or 13 characters after compacting.
 * Samples {@code 1234567/M/A/E/001}, {@code 1282182 W}, {@code 121J}.
 */
public final class TnMfUtil {

    private static final String CONTROL = "ABCDEFGHJKLMNPQRSTVWXYZ";
    private static final String TVA = "APBDN";
    private static final String CATEGORY = "MPCNE";

    private TnMfUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() != 8 && compact.length() != 13) {
            return false;
        }
        if (!compact.substring(0, 7).matches("\\d{7}")) {
            return false;
        }
        if (CONTROL.indexOf(compact.charAt(7)) < 0) {
            return false;
        }
        if (compact.length() == 8) {
            return true;
        }
        if (TVA.indexOf(compact.charAt(8)) < 0 || CATEGORY.indexOf(compact.charAt(9)) < 0) {
            return false;
        }
        if (!compact.substring(10).matches("\\d{3}")) {
            return false;
        }
        return "000".equals(compact.substring(10)) || compact.charAt(9) == 'E';
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() == 8) {
            return compact.substring(0, 7) + '/' + compact.charAt(7);
        }
        if (compact.length() == 13) {
            return compact.substring(0, 7) + '/' + compact.charAt(7) + '/' + compact.charAt(8)
                    + '/' + compact.charAt(9) + '/' + compact.substring(10);
        }
        return value == null ? "" : value.trim();
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s/.-]", "").toUpperCase(Locale.ROOT);
        int index = 0;
        while (index < compact.length() && Character.isDigit(compact.charAt(index))) {
            index++;
        }
        if (index == 0) {
            return compact;
        }
        String serial = compact.substring(0, index);
        String rest = compact.substring(index);
        if (serial.length() < 7) {
            serial = "0".repeat(7 - serial.length()) + serial;
        }
        return serial + rest;
    }
}
