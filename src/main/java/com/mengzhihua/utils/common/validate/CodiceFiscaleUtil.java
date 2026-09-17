package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;

/**
 * Italian codice fiscale. Sample {@code RSSMRA80A01H501U} (Mario Rossi, 1980-01-01).
 */
public final class CodiceFiscaleUtil {

    private static final int[] ODD_DIGIT = {1, 0, 5, 7, 9, 13, 15, 17, 19, 21};
    private static final Map<Character, Integer> MONTH = Map.ofEntries(
            Map.entry('A', 1), Map.entry('B', 2), Map.entry('C', 3), Map.entry('D', 4),
            Map.entry('E', 5), Map.entry('H', 6), Map.entry('L', 7), Map.entry('M', 8),
            Map.entry('P', 9), Map.entry('R', 10), Map.entry('S', 11), Map.entry('T', 12));

    private CodiceFiscaleUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("[A-Z]{6}\\d{2}[A-EHLMPRST]\\d{2}[A-Z]\\d{3}[A-Z]")) {
            return false;
        }
        return checkLetter(compact.substring(0, 15)) == compact.charAt(15);
    }

    public static char checkLetter(String body15) {
        String compact = normalize(body15);
        if (compact.length() != 15) {
            throw new IllegalArgumentException("codice fiscale body must be 15 characters");
        }
        int sum = 0;
        for (int i = 0; i < 15; i++) {
            char c = compact.charAt(i);
            if (i % 2 == 0) {
                sum += oddValue(c);
            } else {
                sum += evenValue(c);
            }
        }
        return (char) ('A' + (sum % 26));
    }

    public static String complete(String body15) {
        return normalize(body15) + checkLetter(body15);
    }

    public static boolean female(String value) {
        String compact = normalize(value);
        if (compact.length() < 11) {
            throw new IllegalArgumentException("codice fiscale is too short");
        }
        return Integer.parseInt(compact.substring(9, 11)) > 40;
    }

    public static LocalDate birthDate(String value) {
        String compact = normalize(value);
        if (compact.length() < 11) {
            throw new IllegalArgumentException("codice fiscale is too short");
        }
        int year = Integer.parseInt(compact.substring(6, 8));
        Integer month = MONTH.get(compact.charAt(8));
        if (month == null) {
            throw new IllegalArgumentException("invalid codice fiscale month");
        }
        int day = Integer.parseInt(compact.substring(9, 11));
        if (day > 40) {
            day -= 40;
        }
        int current = LocalDate.now().getYear() % 100;
        int century = year > current ? 1900 : 2000;
        return LocalDate.of(century + year, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }

    private static int oddValue(char c) {
        if (c >= '0' && c <= '9') {
            return ODD_DIGIT[c - '0'];
        }
        return switch (c) {
            case 'A' -> 1;
            case 'B' -> 0;
            case 'C' -> 5;
            case 'D' -> 7;
            case 'E' -> 9;
            case 'F' -> 13;
            case 'G' -> 15;
            case 'H' -> 17;
            case 'I' -> 19;
            case 'J' -> 21;
            case 'K' -> 2;
            case 'L' -> 4;
            case 'M' -> 18;
            case 'N' -> 20;
            case 'O' -> 11;
            case 'P' -> 3;
            case 'Q' -> 6;
            case 'R' -> 8;
            case 'S' -> 12;
            case 'T' -> 14;
            case 'U' -> 16;
            case 'V' -> 10;
            case 'W' -> 22;
            case 'X' -> 25;
            case 'Y' -> 24;
            case 'Z' -> 23;
            default -> throw new IllegalArgumentException("invalid codice fiscale character");
        };
    }

    private static int evenValue(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        return c - 'A';
    }
}
