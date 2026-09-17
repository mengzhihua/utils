package com.mengzhihua.utils.util;

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
}
