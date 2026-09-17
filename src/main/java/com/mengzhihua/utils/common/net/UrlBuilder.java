package com.mengzhihua.utils.common.net;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Fluent HTTP(S) URL builder on top of {@link UrlUtil}.
 */
public final class UrlBuilder {

    private String scheme = "https";
    private String host;
    private Integer port;
    private String path = "/";
    private final Map<String, Object> query = new LinkedHashMap<>();
    private String fragment;

    private UrlBuilder() {
    }

    public static UrlBuilder of() {
        return new UrlBuilder();
    }

    public static UrlBuilder of(String url) {
        UrlUtil.Parts parts = UrlUtil.parse(url);
        UrlBuilder builder = new UrlBuilder();
        if (parts == null) {
            return builder;
        }
        if (parts.scheme() != null) {
            builder.scheme = parts.scheme();
        }
        builder.host = parts.host();
        builder.port = parts.port();
        builder.path = StringUtil.defaultIfBlank(parts.path(), "/");
        builder.query.putAll(UrlUtil.parseQuery(parts.query() == null ? "" : parts.query()));
        builder.fragment = parts.fragment();
        return builder;
    }

    public UrlBuilder scheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public UrlBuilder host(String host) {
        this.host = host;
        return this;
    }

    public UrlBuilder port(Integer port) {
        this.port = port;
        return this;
    }

    public UrlBuilder path(String path) {
        this.path = path;
        return this;
    }

    public UrlBuilder appendPath(String segment) {
        if (StringUtil.isBlank(segment)) {
            return this;
        }
        String add = segment.startsWith("/") ? segment : "/" + segment;
        if (StringUtil.isBlank(this.path) || "/".equals(this.path)) {
            this.path = add;
        } else if (this.path.endsWith("/")) {
            this.path = this.path + add.substring(1);
        } else {
            this.path = this.path + add;
        }
        return this;
    }

    public UrlBuilder query(String key, Object value) {
        if (key != null) {
            this.query.put(key, value);
        }
        return this;
    }

    public UrlBuilder queries(Map<String, ?> params) {
        if (params != null) {
            this.query.putAll(params);
        }
        return this;
    }

    public UrlBuilder fragment(String fragment) {
        this.fragment = fragment;
        return this;
    }

    public String build() {
        if (StringUtil.isBlank(host)) {
            throw new IllegalArgumentException("host is required");
        }
        StringBuilder builder = new StringBuilder();
        builder.append(StringUtil.defaultIfBlank(scheme, "https")).append("://").append(host);
        if (port != null && port > 0 && !isDefaultPort()) {
            builder.append(':').append(port);
        }
        String resolvedPath = StringUtil.defaultIfBlank(path, "/");
        if (!resolvedPath.startsWith("/")) {
            builder.append('/');
        }
        builder.append(resolvedPath);
        String queryString = UrlUtil.buildQuery(query);
        if (StringUtil.isNotBlank(queryString)) {
            builder.append('?').append(queryString);
        }
        if (StringUtil.isNotBlank(fragment)) {
            builder.append('#').append(fragment);
        }
        return builder.toString();
    }

    private boolean isDefaultPort() {
        if (port == null) {
            return true;
        }
        return ("http".equalsIgnoreCase(scheme) && port == 80)
                || ("https".equalsIgnoreCase(scheme) && port == 443);
    }
}
