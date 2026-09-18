package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * France n° TVA. 2-char key + 9-digit SIREN; new-style keys may include letters
 * (except I/O). Monaco {@code 000} SIREN is accepted. Sample {@code Fr 40 303 265 045}.
 */
public final class FrTvaUtil {

    static final String ALPHABET = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    private FrTvaUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() != 11) {
            return false;
        }
        if (ALPHABET.indexOf(compact.charAt(0)) < 0 || ALPHABET.indexOf(compact.charAt(1)) < 0) {
            return false;
        }
        String siren = compact.substring(2);
        if (!siren.matches("\\d{9}")) {
            return false;
        }
        if (!siren.startsWith("000") && !SirenUtil.isValid(siren)) {
            return false;
        }
        if (compact.matches("\\d{11}")) {
            return numericKey(siren) == Integer.parseInt(compact.substring(0, 2));
        }
        return letterKeyMatches(compact, siren);
    }

    public static String checkDigits(String siren9) {
        String siren = digits(siren9);
        if (siren.length() >= 9) {
            siren = siren.substring(siren.length() - 9);
        }
        if (!siren.matches("\\d{9}")) {
            throw new IllegalArgumentException("French TVA SIREN must be 9 digits");
        }
        return String.format(Locale.ROOT, "%02d", numericKey(siren));
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        String siren = digits(compact);
        if (siren.length() >= 9) {
            siren = siren.substring(siren.length() - 9);
        }
        return checkDigits(siren) + siren;
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 11) {
            return value == null ? "" : value.trim();
        }
        return compact.substring(0, 2) + ' ' + compact.substring(2, 5) + ' '
                + compact.substring(5, 8) + ' ' + compact.substring(8);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("FR")) {
            compact = compact.substring(2);
        }
        return compact;
    }

    static int numericKey(String siren) {
        return (12 + 3 * (int) (Long.parseLong(siren) % 97)) % 97;
    }

    private static boolean letterKeyMatches(String compact, String siren) {
        int check;
        if (Character.isDigit(compact.charAt(0))) {
            check = ALPHABET.indexOf(compact.charAt(0)) * 24 + ALPHABET.indexOf(compact.charAt(1)) - 10;
        } else {
            check = ALPHABET.indexOf(compact.charAt(0)) * 34 + ALPHABET.indexOf(compact.charAt(1)) - 100;
        }
        long body = Long.parseLong(siren);
        return (body + 1 + check / 11) % 11 == (check % 11);
    }

    private static String digits(String value) {
        return normalize(value).replaceAll("\\D", "");
    }
}
