package com.mengzhihua.utils.common.net;


import java.net.URI;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Origin} (RFC 6454).
 * Sample {@code https://example.com:8443} / {@code null}.
 */
public final class OriginUtil {

    public record Origin(String scheme, String host, int port, boolean nullOrigin) {
        public Origin {
            scheme = scheme == null ? "" : scheme;
            host = host == null ? "" : host;
        }
    }

    private OriginUtil() {
    }

    public static Origin parse(String header) {
        if (StringUtil.isBlank(header)) {
            return new Origin("", "", -1, false);
        }
        String value = header.trim();
        if ("null".equalsIgnoreCase(value)) {
            return new Origin("", "", -1, true);
        }
        try {
            URI uri = URI.create(value);
            String scheme = uri.getScheme() == null ? "" : uri.getScheme().toLowerCase(Locale.ROOT);
            String host = uri.getHost() == null ? "" : uri.getHost();
            return new Origin(scheme, host, uri.getPort(), false);
        } catch (IllegalArgumentException ex) {
            return new Origin("", "", -1, false);
        }
    }

    public static String scheme(String header) {
        return parse(header).scheme();
    }

    public static String host(String header) {
        return parse(header).host();
    }

    public static boolean isNull(String header) {
        return parse(header).nullOrigin();
    }

    public static boolean isSecure(String header) {
        return "https".equals(parse(header).scheme());
    }
}
