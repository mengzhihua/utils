package com.mengzhihua.utils.common.json;


import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Minimal INI parser ({@code [section]} / {@code key=value}).
 */
public final class IniUtil {

    private IniUtil() {
    }

    public static Map<String, Map<String, String>> parse(String text) {
        Map<String, Map<String, String>> result = new LinkedHashMap<>();
        String section = "";
        result.put(section, new LinkedHashMap<>());
        if (text == null) {
            return result;
        }
        for (String raw : text.split("\\R")) {
            String line = raw.trim();
            if (line.isEmpty() || line.startsWith("#") || line.startsWith(";")) {
                continue;
            }
            if (line.startsWith("[") && line.endsWith("]")) {
                section = line.substring(1, line.length() - 1).trim();
                result.computeIfAbsent(section, key -> new LinkedHashMap<>());
                continue;
            }
            int eq = line.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            String key = line.substring(0, eq).trim();
            String value = unquote(line.substring(eq + 1).trim());
            result.computeIfAbsent(section, keyName -> new LinkedHashMap<>()).put(key, value);
        }
        return result;
    }

    public static String get(String text, String section, String key) {
        Map<String, String> map = parse(text).get(section == null ? "" : section);
        return map == null ? null : map.get(key);
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    public static String format(Map<String, Map<String, String>> data) {
        StringBuilder builder = new StringBuilder();
        if (data == null) {
            return "";
        }
        data.forEach((section, values) -> {
            if (!section.isEmpty()) {
                if (!builder.isEmpty()) {
                    builder.append('\n');
                }
                builder.append('[').append(section).append("]\n");
            }
            values.forEach((key, value) -> builder.append(key).append('=').append(value).append('\n'));
        });
        return builder.toString();
    }

    public static String normalizeSection(String section) {
        return section == null ? "" : section.toLowerCase(Locale.ROOT);
    }
}
