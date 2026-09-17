package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * Roman numerals, 1–3999.
 */
public final class RomanUtil {

    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    private RomanUtil() {
    }

    public static String toRoman(int number) {
        if (number < 1 || number > 3999) {
            throw new IllegalArgumentException("roman range is 1-3999");
        }
        StringBuilder builder = new StringBuilder();
        int remaining = number;
        for (int i = 0; i < VALUES.length; i++) {
            while (remaining >= VALUES[i]) {
                builder.append(SYMBOLS[i]);
                remaining -= VALUES[i];
            }
        }
        return builder.toString();
    }

    public static int fromRoman(String roman) {
        if (StringUtil.isBlank(roman)) {
            throw new IllegalArgumentException("roman is blank");
        }
        String text = roman.trim().toUpperCase(Locale.ROOT);
        int i = 0;
        int value = 0;
        for (int k = 0; k < SYMBOLS.length; k++) {
            String symbol = SYMBOLS[k];
            while (text.startsWith(symbol, i)) {
                value += VALUES[k];
                i += symbol.length();
            }
        }
        if (i != text.length() || !toRoman(value).equals(text)) {
            throw new IllegalArgumentException("invalid roman: " + roman);
        }
        return value;
    }

    public static boolean isValid(String roman) {
        try {
            fromRoman(roman);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }
}
