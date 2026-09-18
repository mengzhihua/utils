package com.mengzhihua.utils.common.net;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code X-DNS-Prefetch-Control}.
 * Sample {@code on} / {@code off}.
 */
public final class DnsPrefetchUtil {

    private DnsPrefetchUtil() {
    }

    public static String parse(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        return header.trim().toLowerCase(Locale.ROOT);
    }

    public static boolean isOn(String header) {
        return "on".equals(parse(header));
    }

    public static boolean isOff(String header) {
        return "off".equals(parse(header));
    }

    public static boolean isValid(String header) {
        String value = parse(header);
        return "on".equals(value) || "off".equals(value);
    }
}
