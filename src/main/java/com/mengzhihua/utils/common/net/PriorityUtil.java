package com.mengzhihua.utils.common.net;


import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Priority} (RFC 9218). Sample {@code u=1, i}.
 */
public final class PriorityUtil {

    private PriorityUtil() {
    }

    public static Map<String, String> parse(String header) {
        Map<String, String> directives = new LinkedHashMap<>();
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        for (String part : header.split(",")) {
            String item = part.trim();
            if (item.isEmpty()) {
                continue;
            }
            int eq = item.indexOf('=');
            if (eq < 0) {
                directives.put(item.toLowerCase(Locale.ROOT), "");
            } else {
                directives.put(item.substring(0, eq).trim().toLowerCase(Locale.ROOT),
                        item.substring(eq + 1).trim());
            }
        }
        return Map.copyOf(directives);
    }

    public static Integer urgency(String header) {
        String value = parse(header).get("u");
        if (value == null || !value.matches("[0-7]")) {
            return null;
        }
        return Integer.valueOf(value);
    }

    public static boolean incremental(String header) {
        return parse(header).containsKey("i");
    }

    public static boolean isValid(String header) {
        Map<String, String> directives = parse(header);
        if (urgency(header) == null) {
            return false;
        }
        for (Map.Entry<String, String> entry : directives.entrySet()) {
            if ("u".equals(entry.getKey())) {
                continue;
            }
            if ("i".equals(entry.getKey()) && entry.getValue().isEmpty()) {
                continue;
            }
            return false;
        }
        return true;
    }
}
