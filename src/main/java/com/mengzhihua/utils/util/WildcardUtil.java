package com.mengzhihua.utils.util;

import java.util.regex.Pattern;

/**
 * {@code *} / {@code ?} wildcard matching (Apache Commons IO FilenameUtils).
 */
public final class WildcardUtil {

    private WildcardUtil() {
    }

    public static boolean match(String text, String pattern) {
        if (text == null || pattern == null) {
            return false;
        }
        return Pattern.compile(toRegex(pattern)).matcher(text).matches();
    }

    public static String toRegex(String pattern) {
        StringBuilder builder = new StringBuilder("^");
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            switch (c) {
                case '*' -> builder.append(".*");
                case '?' -> builder.append('.');
                case '.', '(', ')', '[', ']', '{', '}', '^', '$', '|', '+', '\\' -> builder.append('\\').append(c);
                default -> builder.append(c);
            }
        }
        return builder.append('$').toString();
    }
}
