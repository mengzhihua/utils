package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Ireland VAT. 8 or 9 characters, 7-digit weighted check against {@code WABCDEFGHIJKLMNOPQRSTUV}.
 * Samples {@code IE 6433435F}, {@code 6433435OA}, {@code 8D79739I}.
 */
public final class IeVatUtil {

    static final String ALPHABET = "WABCDEFGHIJKLMNOPQRSTUV";

    private IeVatUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() != 8 && compact.length() != 9) {
            return false;
        }
        if (!Character.isDigit(compact.charAt(0)) || !compact.substring(2, 7).matches("\\d{5}")) {
            return false;
        }
        for (int i = 7; i < compact.length(); i++) {
            if (ALPHABET.indexOf(compact.charAt(i)) < 0) {
                return false;
            }
        }
        if (compact.substring(0, 7).matches("\\d{7}")) {
            return compact.charAt(7) == checkDigit(compact.substring(0, 7) + compact.substring(8));
        }
        char second = compact.charAt(1);
        if (second == '+' || second == '*' || (second >= 'A' && second <= 'Z')) {
            return compact.charAt(7) == checkDigit(compact.substring(2, 7) + compact.charAt(0));
        }
        return false;
    }

    public static char checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() < 7) {
            compact = ("0000000" + compact).substring(compact.length());
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            if (!Character.isDigit(compact.charAt(i))) {
                throw new IllegalArgumentException("Irish VAT body must start with 7 digits");
            }
            sum += (8 - i) * (compact.charAt(i) - '0');
        }
        int extra = 0;
        if (compact.length() > 7 && !compact.substring(7).isEmpty()) {
            extra = ALPHABET.indexOf(compact.charAt(7));
            if (extra < 0) {
                throw new IllegalArgumentException("Irish VAT has an invalid extra letter");
            }
        }
        return ALPHABET.charAt((sum + 9 * extra) % 23);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.matches("\\d{7}")) {
            return compact + checkDigit(compact);
        }
        if (compact.matches("\\d{7}[A-Z]")) {
            return compact.substring(0, 7) + checkDigit(compact) + compact.substring(7);
        }
        throw new IllegalArgumentException("Irish VAT body is invalid");
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 8 && compact.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return "IE " + compact;
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("IE")) {
            compact = compact.substring(2);
        }
        return compact;
    }
}
