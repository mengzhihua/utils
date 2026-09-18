package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Link} header (RFC 8288).
 */
public final class LinkHeaderUtil {

    private LinkHeaderUtil() {
    }

    public static List<Link> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<Link> links = new ArrayList<>();
        for (String part : split(header, ',')) {
            String item = part.trim();
            int gt = item.indexOf('>');
            if (!item.startsWith("<") || gt < 2) {
                continue;
            }
            String uri = item.substring(1, gt);
            Map<String, String> params = new LinkedHashMap<>();
            String rest = item.substring(gt + 1);
            for (String raw : split(rest, ';')) {
                String pair = raw.trim();
                if (pair.isEmpty()) {
                    continue;
                }
                int eq = pair.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = pair.substring(0, eq).trim().toLowerCase(Locale.ROOT);
                params.put(key, unquote(pair.substring(eq + 1).trim()));
            }
            links.add(new Link(uri, Map.copyOf(params)));
        }
        return List.copyOf(links);
    }

    public static String rel(String header, String relation) {
        if (relation == null) {
            return "";
        }
        String wanted = relation.toLowerCase(Locale.ROOT);
        for (Link link : parse(header)) {
            String rel = link.rel();
            if (rel == null) {
                continue;
            }
            for (String token : rel.split("\\s+")) {
                if (wanted.equals(token.toLowerCase(Locale.ROOT))) {
                    return link.uri();
                }
            }
        }
        return "";
    }

    private static List<String> split(String text, char delimiter) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        boolean angled = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '"') {
                quoted = !quoted;
                current.append(c);
            } else if (c == '<' && !quoted) {
                angled = true;
                current.append(c);
            } else if (c == '>' && !quoted) {
                angled = false;
                current.append(c);
            } else if (c == delimiter && !quoted && !angled) {
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

    public record Link(String uri, Map<String, String> params) {
        public String rel() {
            return params.get("rel");
        }

        public String title() {
            return params.get("title");
        }
    }
}
