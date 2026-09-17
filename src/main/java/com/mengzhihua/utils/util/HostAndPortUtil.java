package com.mengzhihua.utils.util;

/**
 * Guava-style {@code host:port} parser, including IPv6 in brackets.
 */
public final class HostAndPortUtil {

    private HostAndPortUtil() {
    }

    public static HostAndPort parse(String text) {
        if (StringUtil.isBlank(text)) {
            throw new IllegalArgumentException("host:port is blank");
        }
        String value = text.trim();
        String host;
        Integer port = null;
        if (value.startsWith("[")) {
            int close = value.indexOf(']');
            if (close < 0) {
                throw new IllegalArgumentException("invalid IPv6 host:port: " + text);
            }
            host = value.substring(1, close);
            if (close + 1 < value.length()) {
                if (value.charAt(close + 1) != ':') {
                    throw new IllegalArgumentException("invalid IPv6 host:port: " + text);
                }
                port = parsePort(value.substring(close + 2));
            }
        } else {
            int colon = value.lastIndexOf(':');
            if (colon >= 0 && value.indexOf(':') == colon) {
                host = value.substring(0, colon);
                String portText = value.substring(colon + 1);
                if (!portText.isEmpty()) {
                    port = parsePort(portText);
                }
            } else {
                host = value;
            }
        }
        if (StringUtil.isBlank(host)) {
            throw new IllegalArgumentException("host is blank");
        }
        return new HostAndPort(host, port);
    }

    public static String format(String host, Integer port) {
        boolean ipv6 = host != null && host.contains(":");
        String head = ipv6 ? "[" + host + "]" : host;
        return port == null ? head : head + ":" + port;
    }

    private static int parsePort(String text) {
        try {
            int port = Integer.parseInt(text);
            if (port < 0 || port > 65535) {
                throw new IllegalArgumentException("port out of range: " + text);
            }
            return port;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("invalid port: " + text, ex);
        }
    }

    public record HostAndPort(String host, Integer port) {
        public boolean hasPort() {
            return port != null;
        }

        @Override
        public String toString() {
            return format(host, port);
        }
    }
}
