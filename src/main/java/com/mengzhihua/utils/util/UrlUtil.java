package com.mengzhihua.utils.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * URL encode / decode and query-string builders.
 */
public final class UrlUtil {

    private UrlUtil() {
    }

    public static String encode(String value) {
        return value == null ? null : URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public static String decode(String value) {
        return value == null ? null : URLDecoder.decode(value, StandardCharsets.UTF_8);
    }

    public static String buildQuery(Map<String, ?> params) {
        if (MapUtil.isEmpty(params)) {
            return "";
        }
        StringJoiner joiner = new StringJoiner("&");
        params.forEach((key, value) -> {
            if (key != null) {
                joiner.add(encode(key) + "=" + encode(value == null ? "" : String.valueOf(value)));
            }
        });
        return joiner.toString();
    }

    public static String appendQuery(String url, Map<String, ?> params) {
        if (StringUtil.isBlank(url)) {
            return url;
        }
        String query = buildQuery(params);
        if (StringUtil.isBlank(query)) {
            return url;
        }
        return url + (url.contains("?") ? "&" : "?") + query;
    }

    public static Map<String, String> parseQuery(String urlOrQuery) {
        Map<String, String> map = new LinkedHashMap<>();
        if (StringUtil.isBlank(urlOrQuery)) {
            return map;
        }
        String query = urlOrQuery;
        int hash = query.indexOf('#');
        if (hash >= 0) {
            query = query.substring(0, hash);
        }
        int q = query.indexOf('?');
        if (q >= 0) {
            query = query.substring(q + 1);
        }
        if (query.isEmpty()) {
            return map;
        }
        for (String pair : query.split("&")) {
            if (pair.isEmpty()) {
                continue;
            }
            int eq = pair.indexOf('=');
            String key = decode(eq >= 0 ? pair.substring(0, eq) : pair);
            String value = eq >= 0 ? decode(pair.substring(eq + 1)) : "";
            map.put(key, value);
        }
        return map;
    }
}
