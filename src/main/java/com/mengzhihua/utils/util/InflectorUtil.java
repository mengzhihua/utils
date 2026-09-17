package com.mengzhihua.utils.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * English plural / singular (ActiveSupport Inflector style, common cases).
 */
public final class InflectorUtil {

    private static final Map<String, String> IRREGULAR = new HashMap<>();
    private static final Map<String, String> SINGULAR = new HashMap<>();

    static {
        put("child", "children");
        put("person", "people");
        put("man", "men");
        put("woman", "women");
        put("mouse", "mice");
        put("goose", "geese");
        put("foot", "feet");
        put("tooth", "teeth");
        put("ox", "oxen");
        put("leaf", "leaves");
        put("life", "lives");
        put("knife", "knives");
        put("wife", "wives");
        put("potato", "potatoes");
        put("tomato", "tomatoes");
        put("quiz", "quizzes");
    }

    private InflectorUtil() {
    }

    public static String pluralize(String word) {
        if (StringUtil.isBlank(word)) {
            return word;
        }
        String lower = word.toLowerCase(Locale.ROOT);
        if (IRREGULAR.containsKey(lower)) {
            return preserveCase(word, IRREGULAR.get(lower));
        }
        if (lower.endsWith("s") || lower.endsWith("x") || lower.endsWith("z")
                || lower.endsWith("ch") || lower.endsWith("sh")) {
            return word + "es";
        }
        if (lower.endsWith("y") && word.length() > 1 && !vowel(lower.charAt(lower.length() - 2))) {
            return word.substring(0, word.length() - 1) + "ies";
        }
        if (lower.endsWith("f")) {
            return word.substring(0, word.length() - 1) + "ves";
        }
        if (lower.endsWith("fe")) {
            return word.substring(0, word.length() - 2) + "ves";
        }
        return word + "s";
    }

    public static String singularize(String word) {
        if (StringUtil.isBlank(word)) {
            return word;
        }
        String lower = word.toLowerCase(Locale.ROOT);
        if (SINGULAR.containsKey(lower)) {
            return preserveCase(word, SINGULAR.get(lower));
        }
        if (lower.endsWith("ies") && word.length() > 3) {
            return word.substring(0, word.length() - 3) + "y";
        }
        if (lower.endsWith("ves")) {
            return word.substring(0, word.length() - 3) + "f";
        }
        if (lower.endsWith("es") && (lower.endsWith("ses") || lower.endsWith("xes")
                || lower.endsWith("zes") || lower.endsWith("ches") || lower.endsWith("shes"))) {
            return word.substring(0, word.length() - 2);
        }
        if (lower.endsWith("s") && !lower.endsWith("ss")) {
            return word.substring(0, word.length() - 1);
        }
        return word;
    }

    private static void put(String singular, String plural) {
        IRREGULAR.put(singular, plural);
        SINGULAR.put(plural, singular);
    }

    private static boolean vowel(char c) {
        return "aeiou".indexOf(c) >= 0;
    }

    private static String preserveCase(String original, String replacement) {
        if (original.equals(original.toUpperCase(Locale.ROOT))) {
            return replacement.toUpperCase(Locale.ROOT);
        }
        if (Character.isUpperCase(original.charAt(0))) {
            return Character.toUpperCase(replacement.charAt(0)) + replacement.substring(1);
        }
        return replacement;
    }
}
