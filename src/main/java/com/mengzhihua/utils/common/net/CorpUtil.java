package com.mengzhihua.utils.common.net;


import java.util.Locale;
import java.util.Set;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Cross-Origin-Resource-Policy}.
 */
public final class CorpUtil {

    private static final Set<String> KNOWN = Set.of("same-origin", "same-site", "cross-origin");

    private CorpUtil() {
    }

    public static String parse(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        int sc = header.indexOf(';');
        return (sc < 0 ? header : header.substring(0, sc)).trim().toLowerCase(Locale.ROOT);
    }

    public static boolean sameOrigin(String header) {
        return "same-origin".equals(parse(header));
    }

    public static boolean sameSite(String header) {
        return "same-site".equals(parse(header));
    }

    public static boolean crossOrigin(String header) {
        return "cross-origin".equals(parse(header));
    }

    public static boolean known(String header) {
        return KNOWN.contains(parse(header));
    }
}
