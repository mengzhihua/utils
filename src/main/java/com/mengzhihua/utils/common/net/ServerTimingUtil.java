package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Server-Timing} parser (RFC 8594).
 * Sample {@code miss, db;dur=53, app;dur=47.2}.
 */
public final class ServerTimingUtil {

    public record Metric(String name, Map<String, String> params) {
        public Metric {
            name = name == null ? "" : name;
            params = params == null ? Map.of() : Map.copyOf(params);
        }

        public String duration() {
            return params.getOrDefault("dur", "");
        }

        public String description() {
            return params.getOrDefault("desc", "");
        }
    }

    private ServerTimingUtil() {
    }

    public static List<Metric> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<Metric> metrics = new ArrayList<>();
        for (String part : splitComma(header)) {
            Metric metric = parseMetric(part);
            if (metric != null && !metric.name().isEmpty()) {
                metrics.add(metric);
            }
        }
        return List.copyOf(metrics);
    }

    public static String first(String header) {
        List<Metric> metrics = parse(header);
        return metrics.isEmpty() ? "" : metrics.get(0).name();
    }

    public static List<String> names(String header) {
        return parse(header).stream().map(Metric::name).toList();
    }

    public static String duration(String header, String name) {
        Metric metric = find(header, name);
        return metric == null ? "" : metric.duration();
    }

    public static boolean has(String header, String name) {
        return find(header, name) != null;
    }

    private static Metric find(String header, String name) {
        if (name == null) {
            return null;
        }
        String key = name.trim().toLowerCase(Locale.ROOT);
        for (Metric metric : parse(header)) {
            if (metric.name().equals(key)) {
                return metric;
            }
        }
        return null;
    }

    private static Metric parseMetric(String part) {
        List<String> tokens = splitSemicolon(part);
        if (tokens.isEmpty()) {
            return null;
        }
        String name = tokens.get(0).trim().toLowerCase(Locale.ROOT);
        if (name.isEmpty()) {
            return null;
        }
        Map<String, String> params = new LinkedHashMap<>();
        for (int i = 1; i < tokens.size(); i++) {
            String token = tokens.get(i).trim();
            if (token.isEmpty()) {
                continue;
            }
            int eq = token.indexOf('=');
            if (eq < 0) {
                params.put(token.toLowerCase(Locale.ROOT), "");
                continue;
            }
            String key = token.substring(0, eq).trim().toLowerCase(Locale.ROOT);
            params.put(key, unquote(token.substring(eq + 1).trim()));
        }
        return new Metric(name, params);
    }

    private static List<String> splitComma(String header) {
        return split(header, ',');
    }

    private static List<String> splitSemicolon(String header) {
        return split(header, ';');
    }

    private static List<String> split(String text, char separator) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '"') {
                quoted = !quoted;
                current.append(ch);
            } else if (ch == separator && !quoted) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        if (!current.isEmpty()) {
            parts.add(current.toString());
        }
        return parts;
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1).replace("\\\"", "\"");
        }
        return value;
    }
}
