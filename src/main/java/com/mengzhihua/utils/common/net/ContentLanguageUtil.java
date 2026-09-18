package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Content-Language} tag list (RFC 9110).
 * Sample {@code zh-CN, en}.
 */
public final class ContentLanguageUtil {

    private ContentLanguageUtil() {
    }

    public static List<String> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<String> tags = new ArrayList<>();
        for (String part : header.split(",")) {
            String tag = part.trim();
            if (!tag.isEmpty()) {
                tags.add(tag);
            }
        }
        return List.copyOf(tags);
    }

    public static String first(String header) {
        List<String> tags = parse(header);
        return tags.isEmpty() ? "" : tags.get(0);
    }

    public static boolean has(String header, String language) {
        if (language == null) {
            return false;
        }
        String wanted = language.trim().toLowerCase(Locale.ROOT);
        for (String tag : parse(header)) {
            if (tag.toLowerCase(Locale.ROOT).equals(wanted)
                    || tag.toLowerCase(Locale.ROOT).startsWith(wanted + "-")) {
                return true;
            }
        }
        return false;
    }
}
