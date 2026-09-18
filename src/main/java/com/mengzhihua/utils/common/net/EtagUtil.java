package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP ETag / If-None-Match (RFC 9110).
 */
public final class EtagUtil {

    private EtagUtil() {
    }

    public static Etag parse(String value) {
        if (StringUtil.isBlank(value)) {
            return null;
        }
        String text = value.trim();
        boolean weak = false;
        if (text.length() >= 2 && (text.startsWith("W/") || text.startsWith("w/"))) {
            weak = true;
            text = text.substring(2).trim();
        }
        if (text.length() >= 2 && text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"') {
            return new Etag(weak, text.substring(1, text.length() - 1));
        }
        return new Etag(weak, text);
    }

    public static String strong(String tag) {
        return "\"" + (tag == null ? "" : tag) + "\"";
    }

    public static String weak(String tag) {
        return "W/" + strong(tag);
    }

    public static boolean matches(String etag, String ifNoneMatch) {
        if (StringUtil.isBlank(ifNoneMatch)) {
            return false;
        }
        if ("*".equals(ifNoneMatch.trim())) {
            return parse(etag) != null;
        }
        Etag actual = parse(etag);
        if (actual == null) {
            return false;
        }
        for (Etag candidate : parseList(ifNoneMatch)) {
            if (actual.tag().equals(candidate.tag())) {
                return true;
            }
        }
        return false;
    }

    public static List<Etag> parseList(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<Etag> tags = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < header.length(); i++) {
            char c = header.charAt(i);
            if (c == '"') {
                quoted = !quoted;
                current.append(c);
            } else if (c == ',' && !quoted) {
                Etag tag = parse(current.toString());
                if (tag != null) {
                    tags.add(tag);
                }
                current.setLength(0);
            } else {
                current.append(c);
            }
        }
        Etag tag = parse(current.toString());
        if (tag != null) {
            tags.add(tag);
        }
        return List.copyOf(tags);
    }

    public record Etag(boolean weak, String tag) {
        public String formatted() {
            return (weak ? "W/" : "") + "\"" + tag + "\"";
        }
    }
}
