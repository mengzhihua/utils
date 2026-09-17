package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * Word helpers (Apache Commons Text {@code WordUtils} subset).
 */
public final class WordUtil {

    private WordUtil() {
    }

    public static String initials(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean start = true;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c) || c == '-' || c == '_') {
                start = true;
            } else if (start && Character.isLetterOrDigit(c)) {
                builder.append(Character.toUpperCase(c));
                start = false;
            } else {
                start = false;
            }
        }
        return builder.toString();
    }

    public static String capitalizeFully(String text) {
        if (text == null) {
            return null;
        }
        char[] chars = text.toLowerCase(Locale.ROOT).toCharArray();
        boolean start = true;
        for (int i = 0; i < chars.length; i++) {
            if (Character.isLetter(chars[i])) {
                if (start) {
                    chars[i] = Character.toUpperCase(chars[i]);
                    start = false;
                }
            } else {
                start = true;
            }
        }
        return new String(chars);
    }

    public static String wrap(String text, int width) {
        if (text == null || width <= 0) {
            return text;
        }
        String[] words = text.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        int line = 0;
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (line > 0 && line + 1 + word.length() > width) {
                builder.append('\n');
                line = 0;
            } else if (line > 0) {
                builder.append(' ');
                line++;
            }
            builder.append(word);
            line += word.length();
        }
        return builder.toString();
    }
}
