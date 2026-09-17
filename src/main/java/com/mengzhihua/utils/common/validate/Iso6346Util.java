package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * ISO 6346 freight container number (owner + category + serial + check digit).
 * Sample {@code CSQU3054383} is valid.
 */
public final class Iso6346Util {

    private Iso6346Util() {
    }

    public static boolean isValid(String code) {
        String compact = normalize(code);
        if (!compact.matches("[A-Z]{3}[UJZ]\\d{7}")) {
            return false;
        }
        return compact.charAt(10) == checkDigit(compact.substring(0, 10));
    }

    public static char checkDigit(String body10) {
        String body = normalize(body10);
        if (body.length() != 10) {
            throw new IllegalArgumentException("ISO 6346 body must be 10 characters");
        }
        int sum = 0;
        int weight = 1;
        for (int i = 0; i < 10; i++) {
            sum += value(body.charAt(i)) * weight;
            weight *= 2;
        }
        int check = sum % 11;
        return (char) ('0' + (check == 10 ? 0 : check));
    }

    public static String complete(String body10) {
        String body = normalize(body10);
        return body + checkDigit(body);
    }

    public static String normalize(String code) {
        return code == null ? "" : code.trim().toUpperCase(Locale.ROOT).replace(" ", "").replace("-", "");
    }

    private static int value(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        int n = c - 'A' + 10;
        return n + n / 11;
    }
}
