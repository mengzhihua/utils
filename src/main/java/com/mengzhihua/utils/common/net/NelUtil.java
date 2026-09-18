package com.mengzhihua.utils.common.net;


import java.util.Collections;
import java.util.Map;

import com.mengzhihua.utils.common.json.JsonUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code NEL} (Network Error Logging) JSON header.
 */
public final class NelUtil {

    private NelUtil() {
    }

    public static Map<String, Object> parse(String header) {
        if (StringUtil.isBlank(header) || !JsonUtil.isJson(header)) {
            return Map.of();
        }
        try {
            return Collections.unmodifiableMap(JsonUtil.toMap(header.trim()));
        } catch (IllegalArgumentException ex) {
            return Map.of();
        }
    }

    public static String reportTo(String header) {
        Object value = parse(header).get("report_to");
        return value == null ? null : String.valueOf(value);
    }

    public static Long maxAge(String header) {
        Object value = parse(header).get("max_age");
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static boolean includeSubdomains(String header) {
        Object value = parse(header).get("include_subdomains");
        return Boolean.TRUE.equals(value) || "true".equalsIgnoreCase(String.valueOf(value));
    }
}
