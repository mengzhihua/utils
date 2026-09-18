package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Czech DIČ (Daňové identifikační číslo). 8-digit legal, 9-digit special, or RČ.
 * Sample {@code CZ 25123891}.
 */
public final class CzDicUtil {

    private CzDicUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{8,10}")) {
            return false;
        }
        if (compact.length() == 8) {
            return compact.charAt(0) != '9' && checkDigitLegal(compact.substring(0, 7)) == compact.charAt(7);
        }
        if (compact.length() == 9 && compact.charAt(0) == '6') {
            return checkDigitSpecial(compact.substring(1, 8)) == compact.charAt(8);
        }
        return RodneCisloUtil.isValid(compact);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() >= 7 && compact.charAt(0) != '9') {
            compact = compact.substring(0, 7);
            return compact + checkDigitLegal(compact);
        }
        throw new IllegalArgumentException("Czech DIČ body is invalid");
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() < 8 || compact.length() > 10) {
            return value == null ? "" : value.trim();
        }
        return "CZ " + compact;
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s/]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("CZ")) {
            compact = compact.substring(2);
        }
        return compact;
    }

    static char checkDigitLegal(String seven) {
        int check = 11;
        for (int i = 0; i < 7; i++) {
            check -= (8 - i) * (seven.charAt(i) - '0');
        }
        check = Math.floorMod(check, 11);
        return (char) ('0' + ((check == 0 ? 1 : check) % 10));
    }

    static char checkDigitSpecial(String seven) {
        int check = 0;
        for (int i = 0; i < 7; i++) {
            check += (8 - i) * (seven.charAt(i) - '0');
        }
        check %= 11;
        return (char) ('0' + Math.floorMod(8 - Math.floorMod(10 - check, 11), 10));
    }
}
