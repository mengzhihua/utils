package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Guava {@code CaseFormat} conversions between camel / snake / kebab / Pascal.
 */
public final class CaseFormatUtil {

    private static final Pattern SPLIT = Pattern.compile("[\\s._-]+|(?<=[a-z0-9])(?=[A-Z])|(?<=[A-Z])(?=[A-Z][a-z])");

    public enum Style {
        LOWER_CAMEL,
        UPPER_CAMEL,
        LOWER_UNDERSCORE,
        UPPER_UNDERSCORE,
        LOWER_HYPHEN,
        UPPER_HYPHEN
    }

    private CaseFormatUtil() {
    }

    public static String to(Style style, String value) {
        List<String> words = words(value);
        if (words.isEmpty()) {
            return value == null ? "" : value;
        }
        return switch (style == null ? Style.LOWER_CAMEL : style) {
            case LOWER_CAMEL -> joinCamel(words, false);
            case UPPER_CAMEL -> joinCamel(words, true);
            case LOWER_UNDERSCORE -> String.join("_", words).toLowerCase(Locale.ROOT);
            case UPPER_UNDERSCORE -> String.join("_", words).toUpperCase(Locale.ROOT);
            case LOWER_HYPHEN -> String.join("-", words).toLowerCase(Locale.ROOT);
            case UPPER_HYPHEN -> String.join("-", words).toUpperCase(Locale.ROOT);
        };
    }

    public static String toLowerCamel(String value) {
        return to(Style.LOWER_CAMEL, value);
    }

    public static String toUpperCamel(String value) {
        return to(Style.UPPER_CAMEL, value);
    }

    public static String toLowerUnderscore(String value) {
        return to(Style.LOWER_UNDERSCORE, value);
    }

    public static String toUpperUnderscore(String value) {
        return to(Style.UPPER_UNDERSCORE, value);
    }

    public static String toLowerHyphen(String value) {
        return to(Style.LOWER_HYPHEN, value);
    }

    private static List<String> words(String value) {
        List<String> words = new ArrayList<>();
        if (StringUtil.isBlank(value)) {
            return words;
        }
        for (String part : SPLIT.split(value.trim())) {
            if (!part.isEmpty()) {
                words.add(part.toLowerCase(Locale.ROOT));
            }
        }
        return words;
    }

    private static String joinCamel(List<String> words, boolean pascal) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            if (i == 0 && !pascal) {
                builder.append(word);
            } else {
                builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
            }
        }
        return builder.toString();
    }
}
