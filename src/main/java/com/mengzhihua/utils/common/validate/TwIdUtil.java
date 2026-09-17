package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Taiwan National ID (first letter + 9 digits, weighted checksum).
 * Sample {@code A123456789} is valid.
 */
public final class TwIdUtil {

    private static final int[] LETTER = {
            10, 11, 12, 13, 14, 15, 16, 17, 34, 18, 19, 20, 21,
            22, 35, 23, 24, 25, 26, 27, 28, 29, 32, 30, 31, 33
    };

    private TwIdUtil() {
    }

    public static boolean isValid(String id) {
        String compact = normalize(id);
        if (!compact.matches("[A-Z][12]\\d{8}")) {
            return false;
        }
        int n = LETTER[compact.charAt(0) - 'A'];
        int sum = n / 10 + (n % 10) * 9;
        int[] weights = {8, 7, 6, 5, 4, 3, 2, 1, 1};
        for (int i = 0; i < 9; i++) {
            sum += Character.digit(compact.charAt(i + 1), 10) * weights[i];
        }
        return sum % 10 == 0;
    }

    public static String normalize(String id) {
        return id == null ? "" : id.trim().toUpperCase(Locale.ROOT).replace(" ", "");
    }
}
