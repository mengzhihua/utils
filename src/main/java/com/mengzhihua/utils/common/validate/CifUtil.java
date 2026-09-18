package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Set;

/**
 * Spanish CIF / legal-entity NIF. Sample {@code A58818501}.
 */
public final class CifUtil {

    private static final String LETTERS = "JABCDEFGHI";
    private static final Set<Character> LETTER_CONTROL = Set.of('K', 'P', 'Q', 'S');
    private static final Set<Character> DIGIT_CONTROL = Set.of('A', 'B', 'E', 'H');

    private CifUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("[ABCDEFGHJNPQRSUVW]\\d{7}[0-9A-J]")) {
            return false;
        }
        char expected = checkChar(compact.charAt(0), compact.substring(1, 8));
        char actual = compact.charAt(8);
        char type = compact.charAt(0);
        if (LETTER_CONTROL.contains(type)) {
            return actual == expected;
        }
        if (DIGIT_CONTROL.contains(type)) {
            return actual == controlDigit(compact.substring(1, 8));
        }
        return actual == expected || actual == controlDigit(compact.substring(1, 8));
    }

    public static char checkChar(char type, String body7) {
        char digit = controlDigit(body7);
        if (LETTER_CONTROL.contains(type)) {
            return LETTERS.charAt(digit - '0');
        }
        return digit;
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (compact.length() == 9 && isValid(compact)) {
            return compact;
        }
        if (!compact.matches("[ABCDEFGHJNPQRSUVW]\\d{7}")) {
            throw new IllegalArgumentException("CIF body must be letter + 7 digits");
        }
        return compact + checkChar(compact.charAt(0), compact.substring(1));
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static char controlDigit(String body7) {
        if (!body7.matches("\\d{7}")) {
            throw new IllegalArgumentException("CIF body must be 7 digits");
        }
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            int n = body7.charAt(i) - '0';
            if (i % 2 == 0) {
                n *= 2;
                sum += n / 10 + n % 10;
            } else {
                sum += n;
            }
        }
        return (char) ('0' + (10 - (sum % 10)) % 10);
    }
}
