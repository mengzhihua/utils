package com.mengzhihua.utils.common.text;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Refined Soundex (Apache Commons Codec). {@code testing} → {@code T6036084}.
 */
public final class RefinedSoundexUtil {

    private static final char[] MAP = "01360240043788015936020505".toCharArray();

    private RefinedSoundexUtil() {
    }

    public static String encode(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String letters = text.toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
        if (letters.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append(letters.charAt(0));
        char last = '*';
        for (int i = 0; i < letters.length(); i++) {
            char current = map(letters.charAt(i));
            if (current == last) {
                continue;
            }
            builder.append(current);
            last = current;
        }
        return builder.toString();
    }

    public static boolean similar(String left, String right) {
        String a = encode(left);
        return !a.isEmpty() && a.equals(encode(right));
    }

    private static char map(char c) {
        int index = c - 'A';
        return index >= 0 && index < MAP.length ? MAP[index] : '0';
    }
}
