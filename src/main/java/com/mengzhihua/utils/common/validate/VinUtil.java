package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * ISO 3779 VIN check digit (ISO 3779 / NHTSA).
 */
public final class VinUtil {

    private static final int[] WEIGHTS = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};

    private VinUtil() {
    }

    public static boolean isValid(String vin) {
        String compact = normalize(vin);
        if (compact.length() != 17) {
            return false;
        }
        if (compact.indexOf('I') >= 0 || compact.indexOf('O') >= 0 || compact.indexOf('Q') >= 0) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int value = transliterate(compact.charAt(i));
            if (value < 0) {
                return false;
            }
            sum += value * WEIGHTS[i];
        }
        int remainder = sum % 11;
        char check = remainder == 10 ? 'X' : (char) ('0' + remainder);
        return compact.charAt(8) == check;
    }

    public static String normalize(String vin) {
        return vin == null ? "" : vin.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static int transliterate(char c) {
        return switch (c) {
            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> c - '0';
            case 'A', 'J' -> 1;
            case 'B', 'K', 'S' -> 2;
            case 'C', 'L', 'T' -> 3;
            case 'D', 'M', 'U' -> 4;
            case 'E', 'N', 'V' -> 5;
            case 'F', 'W' -> 6;
            case 'G', 'P', 'X' -> 7;
            case 'H', 'Y' -> 8;
            case 'R', 'Z' -> 9;
            default -> -1;
        };
    }
}
