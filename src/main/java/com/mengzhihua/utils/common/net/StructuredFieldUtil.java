package com.mengzhihua.utils.common.net;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * RFC 8941 Structured Fields dictionary (integer / boolean / token / string).
 * Sample {@code abc=123, def=?0, title="hi"}.
 */
public final class StructuredFieldUtil {

    private static final Pattern MEMBER = Pattern.compile(
            "([a-z*][a-z0-9_.*-]*)(?:=(\\?[01]|-?\\d+|\"[^\"]*\"|[A-Za-z0-9_.:*/]+))?");

    private StructuredFieldUtil() {
    }

    public static Map<String, Object> parseDictionary(String header) {
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        Map<String, Object> values = new LinkedHashMap<>();
        for (String part : header.split(",")) {
            Matcher matcher = MEMBER.matcher(part.trim());
            if (!matcher.matches()) {
                continue;
            }
            String key = matcher.group(1).toLowerCase(Locale.ROOT);
            String raw = matcher.group(2);
            if (raw == null) {
                values.put(key, Boolean.TRUE);
            } else if ("?0".equals(raw) || "?1".equals(raw)) {
                values.put(key, "?1".equals(raw));
            } else if (raw.matches("-?\\d+")) {
                values.put(key, Long.parseLong(raw));
            } else if (raw.length() >= 2 && raw.charAt(0) == '"') {
                values.put(key, raw.substring(1, raw.length() - 1));
            } else {
                values.put(key, raw);
            }
        }
        return Collections.unmodifiableMap(values);
    }

    public static Object get(String header, String key) {
        if (key == null) {
            return null;
        }
        return parseDictionary(header).get(key.toLowerCase(Locale.ROOT));
    }

    public static boolean has(String header, String key) {
        return get(header, key) != null;
    }
}
