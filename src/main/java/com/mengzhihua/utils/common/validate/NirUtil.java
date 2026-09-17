package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * French NIR / INSEE number (13 digits + key {@code 97 - n % 97}).
 * Sample {@code 255081416812535}. Corsica {@code 2A}/{@code 2B} → {@code 19}/{@code 18}.
 */
public final class NirUtil {

    private NirUtil() {
    }

    public static boolean isValid(String nir) {
        String compact = normalize(nir);
        if (compact.length() != 15) {
            return false;
        }
        String body = compact.substring(0, 13);
        if (!body.matches("[1-8]\\d{4}(?:\\d{2}|2[AB])\\d{6}")) {
            return false;
        }
        return key(body).equals(compact.substring(13));
    }

    public static String key(String body13) {
        String compact = normalize(body13);
        if (compact.length() != 13) {
            throw new IllegalArgumentException("NIR body must be 13 characters");
        }
        String numeric = departmentNumeric(compact);
        if (!numeric.matches("\\d{13}")) {
            throw new IllegalArgumentException("NIR body must be 13 characters");
        }
        int value = 97 - (int) (Long.parseLong(numeric) % 97);
        return String.format(Locale.ROOT, "%02d", value);
    }

    public static String complete(String body13) {
        String compact = normalize(body13);
        if (compact.length() != 13) {
            throw new IllegalArgumentException("NIR body must be 13 characters");
        }
        return compact + key(compact);
    }

    public static boolean female(String nir) {
        String compact = normalize(nir);
        if (compact.isEmpty()) {
            throw new IllegalArgumentException("NIR is blank");
        }
        char sex = compact.charAt(0);
        return sex == '2' || sex == '4' || sex == '6' || sex == '8';
    }

    public static String normalize(String nir) {
        return nir == null ? "" : nir.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }

    private static String departmentNumeric(String body13) {
        if (body13.length() < 7) {
            return body13;
        }
        String dept = body13.substring(5, 7);
        if ("2A".equals(dept)) {
            return body13.substring(0, 5) + "19" + body13.substring(7);
        }
        if ("2B".equals(dept)) {
            return body13.substring(0, 5) + "18" + body13.substring(7);
        }
        return body13;
    }
}
