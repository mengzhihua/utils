package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Forwarded} header (RFC 7239).
 */
public final class ForwardedUtil {

    private ForwardedUtil() {
    }

    public static List<Element> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<Element> elements = new ArrayList<>();
        for (String part : splitElements(header)) {
            Map<String, String> params = new LinkedHashMap<>();
            for (String pair : splitParams(part)) {
                int eq = pair.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = pair.substring(0, eq).trim().toLowerCase(Locale.ROOT);
                String value = unquote(pair.substring(eq + 1).trim());
                if (!key.isEmpty()) {
                    params.put(key, value);
                }
            }
            if (!params.isEmpty()) {
                elements.add(new Element(params.get("for"), params.get("proto"), params.get("by"), Map.copyOf(params)));
            }
        }
        return List.copyOf(elements);
    }

    public static String clientIp(String header) {
        List<Element> elements = parse(header);
        return elements.isEmpty() || elements.get(0).forParam() == null ? "" : stripPort(elements.get(0).forParam());
    }

    private static List<String> splitElements(String header) {
        return splitOn(header, ',');
    }

    private static List<String> splitParams(String element) {
        return splitOn(element, ';');
    }

    private static List<String> splitOn(String text, char delimiter) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"') {
                quoted = !quoted;
                current.append(c);
            } else if (c == delimiter && !quoted) {
                String item = current.toString().trim();
                if (!item.isEmpty()) {
                    parts.add(item);
                }
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        String item = current.toString().trim();
        if (!item.isEmpty()) {
            parts.add(item);
        }
        return parts;
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }

    private static String stripPort(String value) {
        if (value.startsWith("[")) {
            int end = value.indexOf(']');
            return end > 0 ? value.substring(1, end) : value;
        }
        int colon = value.lastIndexOf(':');
        if (colon > 0 && value.indexOf(':') == colon) {
            return value.substring(0, colon);
        }
        return value;
    }

    public record Element(String forParam, String proto, String by, Map<String, String> params) {
    }
}
