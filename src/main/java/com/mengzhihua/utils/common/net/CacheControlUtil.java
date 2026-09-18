package com.mengzhihua.utils.common.net;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Cache-Control} (RFC 9111).
 */
public final class CacheControlUtil {

    private CacheControlUtil() {
    }

    public static Map<String, String> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        Map<String, String> directives = new LinkedHashMap<>();
        for (String part : header.split(",")) {
            String item = part.trim();
            if (item.isEmpty()) {
                continue;
            }
            int eq = item.indexOf('=');
            if (eq < 0) {
                directives.put(item.toLowerCase(Locale.ROOT), "");
            } else {
                String name = item.substring(0, eq).trim().toLowerCase(Locale.ROOT);
                String value = unquote(item.substring(eq + 1).trim());
                directives.put(name, value);
            }
        }
        return Collections.unmodifiableMap(directives);
    }

    public static Long maxAge(String header) {
        String value = parse(header).get("max-age");
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static boolean has(String header, String directive) {
        return directive != null && parse(header).containsKey(directive.toLowerCase(Locale.ROOT));
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
