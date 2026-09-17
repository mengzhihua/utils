package com.mengzhihua.utils.common.lang;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mengzhihua.utils.common.text.AccentUtil;

/**
 * String helpers covering blank checks, case conversion, masking and joining.
 */
public final class StringUtil {

    public static final String EMPTY = "";

    private StringUtil() {
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.isEmpty();
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(CharSequence cs) {
        if (cs == null || cs.isEmpty()) {
            return true;
        }
        for (int i = 0; i < cs.length(); i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    public static String trim(String str) {
        return str == null ? null : str.trim();
    }

    public static String trimToEmpty(String str) {
        return str == null ? EMPTY : str.trim();
    }

    public static String defaultIfBlank(String str, String defaultValue) {
        return isBlank(str) ? defaultValue : str;
    }

    public static String defaultIfEmpty(String str, String defaultValue) {
        return isEmpty(str) ? defaultValue : str;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }

    public static boolean equalsIgnoreCase(String a, String b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.equalsIgnoreCase(b);
    }

    public static boolean contains(String str, String search) {
        return str != null && search != null && str.contains(search);
    }

    public static String substring(String str, int start, int end) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        int from = Math.max(0, start);
        int to = Math.min(length, end);
        if (from >= to) {
            return EMPTY;
        }
        return str.substring(from, to);
    }

    public static String removePrefix(String str, String prefix) {
        if (isEmpty(str) || isEmpty(prefix) || !str.startsWith(prefix)) {
            return str;
        }
        return str.substring(prefix.length());
    }

    public static String removeSuffix(String str, String suffix) {
        if (isEmpty(str) || isEmpty(suffix) || !str.endsWith(suffix)) {
            return str;
        }
        return str.substring(0, str.length() - suffix.length());
    }

    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase(Locale.ROOT);
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase(Locale.ROOT);
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * Converts {@code userName} / {@code UserName} to {@code user_name}.
     */
    public static String camelToSnake(String str) {
        if (isBlank(str)) {
            return str;
        }
        StringBuilder builder = new StringBuilder(str.length() + 8);
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    builder.append('_');
                }
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    /**
     * Converts {@code user_name} to {@code userName}.
     */
    public static String snakeToCamel(String str) {
        if (isBlank(str) || !str.contains("_")) {
            return str;
        }
        String[] parts = str.toLowerCase(Locale.ROOT).split("_");
        StringBuilder builder = new StringBuilder(str.length());
        builder.append(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            if (!parts[i].isEmpty()) {
                builder.append(capitalize(parts[i]));
            }
        }
        return builder.toString();
    }

    public static String repeat(String str, int times) {
        if (str == null || times <= 0) {
            return EMPTY;
        }
        return str.repeat(times);
    }

    public static String join(CharSequence delimiter, Iterable<?> items) {
        if (items == null) {
            return EMPTY;
        }
        String sep = delimiter == null ? EMPTY : delimiter.toString();
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Object item : items) {
            if (!first) {
                builder.append(sep);
            }
            builder.append(item == null ? EMPTY : item);
            first = false;
        }
        return builder.toString();
    }

    public static List<String> split(String str, String delimiter) {
        if (isEmpty(str)) {
            return new ArrayList<>();
        }
        String sep = delimiter == null ? "," : delimiter;
        return Arrays.stream(str.split(Pattern.quote(sep), -1))
                .map(String::trim)
                .filter(StringUtil::isNotEmpty)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static String mask(String str, int keepPrefix, int keepSuffix, char maskChar) {
        if (isEmpty(str)) {
            return str;
        }
        if (keepPrefix < 0 || keepSuffix < 0 || keepPrefix + keepSuffix >= str.length()) {
            return str;
        }
        String prefix = str.substring(0, keepPrefix);
        String suffix = str.substring(str.length() - keepSuffix);
        return prefix + String.valueOf(maskChar).repeat(str.length() - keepPrefix - keepSuffix) + suffix;
    }

    public static String maskPhone(String phone) {
        return mask(phone, 3, 4, '*');
    }

    public static String maskEmail(String email) {
        if (isBlank(email) || !email.contains("@")) {
            return email;
        }
        int at = email.indexOf('@');
        String name = email.substring(0, at);
        String domain = email.substring(at);
        if (name.length() <= 1) {
            return "*" + domain;
        }
        return mask(name, 1, 0, '*') + domain;
    }

    public static String maskIdCard(String idCard) {
        return mask(idCard, 3, 4, '*');
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * Converts {@code userName} / {@code user_name} to {@code user-name}.
     */
    public static String toKebab(String str) {
        if (isBlank(str)) {
            return str;
        }
        return camelToSnake(str.contains("_") ? snakeToCamel(str) : str).replace('_', '-');
    }

    public static String toPascal(String str) {
        return capitalize(snakeToCamel(isBlank(str) ? str : str.replace('-', '_')));
    }

    public static String format(String template, Map<String, ?> params) {
        if (template == null || params == null) {
            return template;
        }
        String result = template;
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            if (entry.getKey() == null) {
                continue;
            }
            String value = entry.getValue() == null ? EMPTY : String.valueOf(entry.getValue());
            result = result.replace("{" + entry.getKey() + "}", value);
            result = result.replace("${" + entry.getKey() + "}", value);
        }
        return result;
    }

    public static String toHalfWidth(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == 12288) {
                chars[i] = ' ';
            } else if (c >= 65281 && c <= 65374) {
                chars[i] = (char) (c - 65248);
            }
        }
        return new String(chars);
    }

    public static String toFullWidth(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == ' ') {
                chars[i] = 12288;
            } else if (c >= 33 && c <= 126) {
                chars[i] = (char) (c + 65248);
            }
        }
        return new String(chars);
    }

    public static String pad(String str, int length, char padChar) {
        String value = str == null ? "" : str;
        if (value.length() >= length) {
            return value;
        }
        return String.valueOf(padChar).repeat(length - value.length()) + value;
    }

    public static String brief(String str, int max) {
        if (str == null || max < 0 || str.length() <= max) {
            return str;
        }
        if (max <= 1) {
            return str.substring(0, max);
        }
        return str.substring(0, max - 1) + "…";
    }

    public static String commonPrefix(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        int n = Math.min(a.length(), b.length());
        int i = 0;
        while (i < n && a.charAt(i) == b.charAt(i)) {
            i++;
        }
        return a.substring(0, i);
    }

    public static String commonSuffix(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        int n = Math.min(a.length(), b.length());
        int i = 0;
        while (i < n && a.charAt(a.length() - 1 - i) == b.charAt(b.length() - 1 - i)) {
            i++;
        }
        return a.substring(a.length() - i);
    }

    public static String difference(String left, String right) {
        String prefix = commonPrefix(left, right);
        String value = right == null ? "" : right;
        return value.substring(prefix.length());
    }

    public static String swapCase(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isUpperCase(c)) {
                chars[i] = Character.toLowerCase(c);
            } else if (Character.isLowerCase(c)) {
                chars[i] = Character.toUpperCase(c);
            }
        }
        return new String(chars);
    }

    public static String rotate(String str, int distance) {
        if (isEmpty(str) || str.length() == 1) {
            return str;
        }
        int len = str.length();
        int shift = Math.floorMod(distance, len);
        return str.substring(len - shift) + str.substring(0, len - shift);
    }

    public static int countMatches(String str, String search) {
        if (isEmpty(str) || isEmpty(search)) {
            return 0;
        }
        int n = 0;
        int from = 0;
        while (true) {
            int index = str.indexOf(search, from);
            if (index < 0) {
                return n;
            }
            n++;
            from = index + search.length();
        }
    }

    public static String stripAccents(String str) {
        return AccentUtil.strip(str);
    }

    public static String padEnd(String str, int length, char padChar) {
        String value = str == null ? EMPTY : str;
        if (value.length() >= length) {
            return value;
        }
        return value + String.valueOf(padChar).repeat(length - value.length());
    }

    public static String abbreviate(String str, int maxWidth) {
        if (str == null) {
            return null;
        }
        if (maxWidth < 4) {
            throw new IllegalArgumentException("maxWidth must be at least 4");
        }
        if (str.length() <= maxWidth) {
            return str;
        }
        return str.substring(0, maxWidth - 3) + "...";
    }

    public static String normalizeSpace(String str) {
        if (str == null) {
            return null;
        }
        return str.trim().replaceAll("\\s+", " ");
    }

    public static String wrap(String str, String prefix, String suffix) {
        if (str == null) {
            return null;
        }
        return defaultIfEmpty(prefix, EMPTY) + str + defaultIfEmpty(suffix, EMPTY);
    }

    public static String unWrap(String str, String prefix, String suffix) {
        if (isEmpty(str) || isEmpty(prefix) || isEmpty(suffix)) {
            return str;
        }
        if (str.startsWith(prefix) && str.endsWith(suffix) && str.length() >= prefix.length() + suffix.length()) {
            return str.substring(prefix.length(), str.length() - suffix.length());
        }
        return str;
    }

    public static String subBetween(String str, String before, String after) {
        if (isEmpty(str) || isEmpty(before) || isEmpty(after)) {
            return null;
        }
        int start = str.indexOf(before);
        if (start < 0) {
            return null;
        }
        start += before.length();
        int end = str.indexOf(after, start);
        if (end < 0) {
            return null;
        }
        return str.substring(start, end);
    }

    public static List<String> subBetweenAll(String str, String before, String after) {
        List<String> list = new ArrayList<>();
        if (isEmpty(str) || isEmpty(before) || isEmpty(after)) {
            return list;
        }
        int from = 0;
        while (true) {
            int start = str.indexOf(before, from);
            if (start < 0) {
                return list;
            }
            start += before.length();
            int end = str.indexOf(after, start);
            if (end < 0) {
                return list;
            }
            list.add(str.substring(start, end));
            from = end + after.length();
        }
    }
}
