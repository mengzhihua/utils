package com.mengzhihua.utils.common.net;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.mengzhihua.utils.common.lang.MapUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

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

    public static Parts parse(String url) {
        if (StringUtil.isBlank(url)) {
            return null;
        }
        String text = url.trim();
        String scheme = "http";
        int schemeSep = text.indexOf("://");
        if (schemeSep >= 0) {
            scheme = text.substring(0, schemeSep);
            text = text.substring(schemeSep + 3);
        }
        String fragment = null;
        int hash = text.indexOf('#');
        if (hash >= 0) {
            fragment = text.substring(hash + 1);
            text = text.substring(0, hash);
        }
        String query = null;
        int q = text.indexOf('?');
        if (q >= 0) {
            query = text.substring(q + 1);
            text = text.substring(0, q);
        }
        String userInfo = null;
        String hostPort;
        String path = "";
        int slash = text.indexOf('/');
        if (slash >= 0) {
            hostPort = text.substring(0, slash);
            path = text.substring(slash);
        } else {
            hostPort = text;
        }
        int at = hostPort.lastIndexOf('@');
        if (at >= 0) {
            userInfo = hostPort.substring(0, at);
            hostPort = hostPort.substring(at + 1);
        }
        String host = hostPort;
        Integer port = null;
        if (hostPort.startsWith("[")) {
            int close = hostPort.indexOf(']');
            if (close > 0) {
                host = hostPort.substring(1, close);
                if (close + 1 < hostPort.length() && hostPort.charAt(close + 1) == ':') {
                    port = Integer.parseInt(hostPort.substring(close + 2));
                }
            }
        } else {
            int colon = hostPort.lastIndexOf(':');
            if (colon >= 0) {
                host = hostPort.substring(0, colon);
                String portText = hostPort.substring(colon + 1);
                if (!portText.isEmpty()) {
                    port = Integer.parseInt(portText);
                }
            }
        }
        return new Parts(scheme, userInfo, host, port, path, query, fragment);
    }

    public static String getProtocol(String url) {
        Parts parts = parse(url);
        return parts == null ? null : parts.scheme();
    }

    public static String getHost(String url) {
        Parts parts = parse(url);
        return parts == null ? null : parts.host();
    }

    public static Integer getPort(String url) {
        Parts parts = parse(url);
        return parts == null ? null : parts.port();
    }

    public static String getPath(String url) {
        Parts parts = parse(url);
        return parts == null ? null : parts.path();
    }

    public static String getFragment(String url) {
        Parts parts = parse(url);
        return parts == null ? null : parts.fragment();
    }

    public record Parts(String scheme, String userInfo, String host, Integer port,
                        String path, String query, String fragment) {
    }
}
