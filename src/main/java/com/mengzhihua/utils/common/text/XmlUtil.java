package com.mengzhihua.utils.common.text;


import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * XML escape / unescape for embedding text in XML documents.
 */
public final class XmlUtil {

    private XmlUtil() {
    }

    public static String escape(String xml) {
        if (xml == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(xml.length());
        for (int i = 0; i < xml.length(); i++) {
            char c = xml.charAt(i);
            switch (c) {
                case '<' -> builder.append("&lt;");
                case '>' -> builder.append("&gt;");
                case '&' -> builder.append("&amp;");
                case '"' -> builder.append("&quot;");
                case '\'' -> builder.append("&apos;");
                default -> builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String unescape(String xml) {
        if (xml == null) {
            return null;
        }
        return xml.replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&apos;", "'")
                .replace("&amp;", "&");
    }

    public static String wrap(String tag, String text) {
        AssertUtil.notBlank(tag, "tag must not be blank");
        return "<" + tag + ">" + escape(text == null ? "" : text) + "</" + tag + ">";
    }
}
