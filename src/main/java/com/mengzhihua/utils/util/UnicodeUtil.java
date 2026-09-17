package com.mengzhihua.utils.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unicode escape / unescape ({@code \\uXXXX}).
 */
public final class UnicodeUtil {

    private static final Pattern UNICODE = Pattern.compile("\\\\u([0-9a-fA-F]{4})");

    private UnicodeUtil() {
    }

    public static String toUnicode(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(text.length() * 6);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c > 127) {
                builder.append("\\u").append(String.format(Locale.ROOT, "%04x", (int) c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String fromUnicode(String text) {
        if (text == null) {
            return null;
        }
        Matcher matcher = UNICODE.matcher(text);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            matcher.appendReplacement(builder, Character.toString((char) Integer.parseInt(matcher.group(1), 16)));
        }
        matcher.appendTail(builder);
        return builder.toString();
    }
}
