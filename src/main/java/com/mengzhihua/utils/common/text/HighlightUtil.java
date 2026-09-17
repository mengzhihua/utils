package com.mengzhihua.utils.common.text;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Keyword highlight for search snippets. Callers should treat the result as HTML.
 */
public final class HighlightUtil {

    private HighlightUtil() {
    }

    public static String html(String text, String keyword) {
        if (StringUtil.isBlank(text) || StringUtil.isBlank(keyword)) {
            return HtmlUtil.escape(text);
        }
        String escaped = HtmlUtil.escape(text);
        String needle = HtmlUtil.escape(keyword);
        if (needle.isEmpty()) {
            return escaped;
        }
        StringBuilder builder = new StringBuilder();
        String lower = escaped.toLowerCase();
        String needleLower = needle.toLowerCase();
        int from = 0;
        int index;
        while ((index = lower.indexOf(needleLower, from)) >= 0) {
            builder.append(escaped, from, index);
            builder.append("<mark>").append(escaped, index, index + needle.length()).append("</mark>");
            from = index + needle.length();
        }
        builder.append(escaped.substring(from));
        return builder.toString();
    }
}
