package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * American Soundex (Apache Commons Codec style).
 */
public final class SoundexUtil {

    private static final char[] MAP = "01230120022455012623010202".toCharArray();

    private SoundexUtil() {
    }

    public static String encode(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String letters = text.toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
        if (letters.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder(4);
        builder.append(letters.charAt(0));
        char last = map(letters.charAt(0));
        for (int i = 1; i < letters.length() && builder.length() < 4; i++) {
            char mapped = map(letters.charAt(i));
            if (mapped != '0' && mapped != last) {
                builder.append(mapped);
            }
            if (mapped != '0') {
                last = mapped;
            }
        }
        while (builder.length() < 4) {
            builder.append('0');
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
