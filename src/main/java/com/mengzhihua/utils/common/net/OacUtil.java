package com.mengzhihua.utils.common.net;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Origin-Agent-Cluster} structured-field boolean.
 */
public final class OacUtil {

    private OacUtil() {
    }

    public static String parse(String header) {
        return StringUtil.isBlank(header) ? "" : header.trim();
    }

    public static boolean enabled(String header) {
        return "?1".equals(parse(header));
    }

    public static boolean disabled(String header) {
        return "?0".equals(parse(header));
    }

    public static boolean known(String header) {
        return enabled(header) || disabled(header);
    }
}
