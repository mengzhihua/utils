package com.mengzhihua.utils.common.net;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Timing-Allow-Origin}.
 */
public final class TimingAllowUtil {

    private TimingAllowUtil() {
    }

    public static String parse(String header) {
        return StringUtil.isBlank(header) ? "" : header.trim();
    }

    public static boolean wildcard(String header) {
        return "*".equals(parse(header));
    }

    public static boolean origin(String header) {
        String value = parse(header);
        return value.startsWith("http://") || value.startsWith("https://");
    }

    public static boolean known(String header) {
        return wildcard(header) || origin(header);
    }
}
