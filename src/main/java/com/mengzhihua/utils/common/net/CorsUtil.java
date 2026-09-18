package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * CORS response headers ({@code Access-Control-Allow-*}).
 */
public final class CorsUtil {

    private CorsUtil() {
    }

    public static boolean allowsOrigin(String allowOrigin, String requestOrigin) {
        if (StringUtil.isBlank(allowOrigin)) {
            return false;
        }
        String allowed = allowOrigin.trim();
        if ("*".equals(allowed)) {
            return true;
        }
        return requestOrigin != null && allowed.equalsIgnoreCase(requestOrigin.trim());
    }

    public static List<String> methods(String allowMethods) {
        return split(allowMethods);
    }

    public static boolean allowsMethod(String allowMethods, String method) {
        if (method == null) {
            return false;
        }
        List<String> parsed = methods(allowMethods);
        if (parsed.contains("*")) {
            return true;
        }
        String wanted = method.trim().toUpperCase(Locale.ROOT);
        return parsed.stream().anyMatch(item -> item.equalsIgnoreCase(wanted));
    }

    public static List<String> headers(String allowHeaders) {
        return split(allowHeaders);
    }

    public static boolean allowsHeader(String allowHeaders, String header) {
        if (header == null) {
            return false;
        }
        List<String> parsed = headers(allowHeaders);
        if (parsed.contains("*")) {
            return true;
        }
        String wanted = header.trim().toLowerCase(Locale.ROOT);
        return parsed.stream().anyMatch(item -> item.equalsIgnoreCase(wanted));
    }

    private static List<String> split(String header) {
        List<String> values = new ArrayList<>();
        if (StringUtil.isBlank(header)) {
            return values;
        }
        for (String part : header.split(",")) {
            String item = part.trim();
            if (!item.isEmpty()) {
                if ("*".equals(item)) {
                    values.add("*");
                } else {
                    values.add(item);
                }
            }
        }
        return values;
    }
}
