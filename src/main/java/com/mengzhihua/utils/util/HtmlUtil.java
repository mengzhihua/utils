package com.mengzhihua.utils.util;

import java.util.regex.Pattern;

/**
 * HTML escape and tag stripping for XSS-safe display.
 */
public final class HtmlUtil {

    private static final Pattern TAGS = Pattern.compile("<[^>]+>");

    private HtmlUtil() {
    }

    public static String escape(String html) {
        if (html == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(html.length());
        for (int i = 0; i < html.length(); i++) {
            char c = html.charAt(i);
            switch (c) {
                case '<' -> builder.append("&lt;");
                case '>' -> builder.append("&gt;");
                case '&' -> builder.append("&amp;");
                case '"' -> builder.append("&quot;");
                case '\'' -> builder.append("&#39;");
                default -> builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String unescape(String html) {
        if (html == null) {
            return null;
        }
        return html.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&amp;", "&");
    }

    public static String stripTags(String html) {
        if (html == null) {
            return null;
        }
        return TAGS.matcher(html).replaceAll("");
    }
}
