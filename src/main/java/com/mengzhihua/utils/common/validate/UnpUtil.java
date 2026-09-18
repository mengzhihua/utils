package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Map;

/**
 * Belarusian UNP / УНП. Samples {@code 200988541}, {@code MA1953684}.
 */
public final class UnpUtil {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERS = "ABCEHKMOPT";
    private static final int[] WEIGHTS = {29, 23, 19, 17, 13, 7, 5, 3};
    private static final Map<Character, Character> CYRILLIC = Map.ofEntries(
            Map.entry('А', 'A'), Map.entry('В', 'B'), Map.entry('Е', 'E'),
            Map.entry('К', 'K'), Map.entry('М', 'M'), Map.entry('Н', 'H'),
            Map.entry('О', 'O'), Map.entry('Р', 'P'), Map.entry('С', 'C'),
            Map.entry('Т', 'T'));

    private UnpUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() != 9) {
            return false;
        }
        char first = compact.charAt(0);
        if ("1234567ABCEHKM".indexOf(first) < 0) {
            return false;
        }
        if (!compact.substring(2).matches("\\d{7}")) {
            return false;
        }
        if (!Character.isDigit(compact.charAt(1)) && LETTERS.indexOf(compact.charAt(1)) < 0) {
            return false;
        }
        try {
            return checkDigit(compact) == compact.charAt(8);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static char checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() >= 9) {
            compact = compact.substring(0, 8);
        }
        if (compact.length() != 8) {
            throw new IllegalArgumentException("UNP body must be 8 characters");
        }
        if (!Character.isDigit(compact.charAt(1))) {
            int index = LETTERS.indexOf(compact.charAt(1));
            if (index < 0) {
                throw new IllegalArgumentException("UNP letter pair invalid");
            }
            compact = compact.charAt(0) + String.valueOf(index) + compact.substring(2);
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            int value = ALPHABET.indexOf(compact.charAt(i));
            if (value < 0) {
                throw new IllegalArgumentException("UNP body invalid");
            }
            sum += value * WEIGHTS[i];
        }
        int rem = sum % 11;
        if (rem > 9) {
            throw new IllegalArgumentException("UNP check overflow");
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() >= 8) {
            compact = compact.substring(0, 8);
        }
        return compact + checkDigit(compact);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("UNP") || compact.startsWith("УНП")) {
            compact = compact.substring(3);
        }
        StringBuilder out = new StringBuilder(compact.length());
        for (int i = 0; i < compact.length(); i++) {
            char ch = compact.charAt(i);
            out.append(CYRILLIC.getOrDefault(ch, ch));
        }
        return out.toString();
    }
}
