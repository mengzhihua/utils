package com.mengzhihua.utils.common.text;


import com.mengzhihua.utils.common.json.JsonUtil;

/**
 * Escape helpers for JS / CSV / JSON string literals.
 */
public final class EscapeUtil {

    private EscapeUtil() {
    }

    public static String js(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(text.length() + 8);
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '\\' -> builder.append("\\\\");
                case '\'' -> builder.append("\\'");
                case '"' -> builder.append("\\\"");
                case '\n' -> builder.append("\\n");
                case '\r' -> builder.append("\\r");
                case '\t' -> builder.append("\\t");
                case '/' -> builder.append("\\/");
                default -> builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String csv(String text) {
        if (text == null) {
            return "";
        }
        if (text.indexOf(',') >= 0 || text.indexOf('"') >= 0 || text.indexOf('\n') >= 0 || text.indexOf('\r') >= 0) {
            return '"' + text.replace("\"", "\"\"") + '"';
        }
        return text;
    }

    public static String json(String text) {
        try {
            return JsonUtil.mapper().writeValueAsString(text);
        } catch (tools.jackson.core.JacksonException ex) {
            throw new IllegalArgumentException("failed to escape json string", ex);
        }
    }

    public static String html(String text) {
        return HtmlUtil.escape(text);
    }

    /**
     * Percent-encoding of UTF-8 bytes (Guava {@code PercentEscaper} style, unreserved kept).
     */
    public static String percent(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : text.getBytes(java.nio.charset.StandardCharsets.UTF_8)) {
            int n = b & 0xff;
            if (unreserved(n)) {
                builder.append((char) n);
            } else {
                builder.append(String.format("%%%02X", n));
            }
        }
        return builder.toString();
    }

    public static String unpercent(String text) {
        if (text == null) {
            return "";
        }
        try {
            return java.net.URLDecoder.decode(text, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("invalid percent encoding", ex);
        }
    }

    private static boolean unreserved(int n) {
        return (n >= 'A' && n <= 'Z') || (n >= 'a' && n <= 'z') || (n >= '0' && n <= '9')
                || n == '-' || n == '_' || n == '.' || n == '~';
    }
}
