package com.mengzhihua.utils.common.net;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Strict-Transport-Security} (RFC 6797).
 */
public final class HstsUtil {

    private HstsUtil() {
    }

    public static Map<String, String> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        Map<String, String> directives = new LinkedHashMap<>();
        for (String part : header.split(";")) {
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

    public static boolean includeSubDomains(String header) {
        return parse(header).containsKey("includesubdomains");
    }

    public static boolean preload(String header) {
        return parse(header).containsKey("preload");
    }
}
