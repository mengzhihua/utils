package com.mengzhihua.utils.common.net;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code X-Content-Type-Options}.
 */
public final class XctoUtil {

    private XctoUtil() {
    }

    public static String parse(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        int sc = header.indexOf(';');
        return (sc < 0 ? header : header.substring(0, sc)).trim().toLowerCase(Locale.ROOT);
    }

    public static boolean nosniff(String header) {
        return "nosniff".equals(parse(header));
    }

    public static boolean known(String header) {
        return nosniff(header);
    }
}
