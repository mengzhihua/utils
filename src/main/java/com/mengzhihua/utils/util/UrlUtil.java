package com.mengzhihua.utils.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
}
