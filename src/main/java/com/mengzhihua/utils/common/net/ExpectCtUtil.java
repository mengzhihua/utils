package com.mengzhihua.utils.common.net;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Expect-CT}.
 * Sample {@code max-age=86400, enforce}.
 */
public final class ExpectCtUtil {

    private ExpectCtUtil() {
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
                String value = item.substring(eq + 1).trim();
                if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
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

    public static boolean enforce(String header) {
        return parse(header).containsKey("enforce");
    }

    public static String reportUri(String header) {
        return parse(header).getOrDefault("report-uri", "");
    }

    public static boolean isValid(String header) {
        Map<String, String> directives = parse(header);
        Long age = maxAge(header);
        if (age == null || age < 0) {
            return false;
        }
        for (String name : directives.keySet()) {
            if (!"max-age".equals(name) && !"enforce".equals(name) && !"report-uri".equals(name)) {
                return false;
            }
        }
        return true;
    }
}
