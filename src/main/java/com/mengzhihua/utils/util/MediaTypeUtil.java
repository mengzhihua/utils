package com.mengzhihua.utils.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Guava-style media type parser, e.g. {@code application/json; charset=utf-8}.
 */
public final class MediaTypeUtil {

    private MediaTypeUtil() {
    }

    public static MediaType parse(String text) {
        if (StringUtil.isBlank(text)) {
            throw new IllegalArgumentException("media type is blank");
        }
        String[] parts = text.trim().split(";");
        String[] typeParts = parts[0].trim().split("/");
        if (typeParts.length != 2 || typeParts[0].isEmpty() || typeParts[1].isEmpty()) {
            throw new IllegalArgumentException("invalid media type: " + text);
        }
        Map<String, String> parameters = new LinkedHashMap<>();
        for (int i = 1; i < parts.length; i++) {
            String param = parts[i].trim();
            if (param.isEmpty()) {
                continue;
            }
            int eq = param.indexOf('=');
            if (eq <= 0) {
                throw new IllegalArgumentException("invalid media type parameter: " + param);
            }
            String name = param.substring(0, eq).trim().toLowerCase(Locale.ROOT);
            String value = unquote(param.substring(eq + 1).trim());
            parameters.put(name, value);
        }
        return new MediaType(
                typeParts[0].toLowerCase(Locale.ROOT),
                typeParts[1].toLowerCase(Locale.ROOT),
                Collections.unmodifiableMap(parameters)
        );
    }

    public static boolean isJson(String text) {
        try {
            MediaType type = parse(text);
            return "json".equals(type.subtype()) || type.subtype().endsWith("+json");
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    public record MediaType(String type, String subtype, Map<String, String> parameters) {
        public String parameter(String name) {
            return parameters.get(name.toLowerCase(Locale.ROOT));
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(type).append('/').append(subtype);
            parameters.forEach((name, value) -> builder.append("; ").append(name).append('=').append(value));
            return builder.toString();
        }
    }
}
