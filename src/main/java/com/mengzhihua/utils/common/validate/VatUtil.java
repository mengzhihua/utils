package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * EU VAT (DE / FR / NL). Samples {@code DE136695976}, {@code FR44732829320}, {@code NL002875718B01}.
 */
public final class VatUtil {

    private VatUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.startsWith("DE")) {
            return isGerman(compact.substring(2));
        }
        if (compact.startsWith("FR")) {
            return isFrench(compact.substring(2));
        }
        if (compact.startsWith("NL")) {
            return isDutch(compact.substring(2));
        }
        return false;
    }

    public static String country(String value) {
        String compact = normalize(value);
        return compact.length() >= 2 ? compact.substring(0, 2) : "";
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }

    private static boolean isGerman(String body) {
        if (!body.matches("\\d{9}")) {
            return false;
        }
        int product = 10;
        for (int i = 0; i < 8; i++) {
            int sum = (body.charAt(i) - '0' + product) % 10;
            if (sum == 0) {
                sum = 10;
            }
            product = (sum * 2) % 11;
        }
        int check = 11 - product;
        if (check == 10) {
            check = 0;
        }
        return check == body.charAt(8) - '0';
    }

    private static boolean isFrench(String body) {
        if (!body.matches("\\d{11}")) {
            return false;
        }
        String siren = body.substring(2);
        if (!SirenUtil.isValid(siren)) {
            return false;
        }
        int key = (12 + 3 * (int) (Long.parseLong(siren) % 97)) % 97;
        return Integer.parseInt(body.substring(0, 2)) == key;
    }

    private static boolean isDutch(String body) {
        if (!body.matches("\\d{9}B\\d{2}")) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (body.charAt(i) - '0') * (9 - i);
        }
        return sum % 11 == body.charAt(8) - '0';
    }
}
