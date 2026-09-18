package com.mengzhihua.utils.common.net;


import java.util.Locale;
import java.util.Set;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Cross-Origin-Opener-Policy}.
 */
public final class CoopUtil {

    private static final Set<String> KNOWN = Set.of(
            "unsafe-none",
            "same-origin-allow-popups",
            "same-origin",
            "noopener-allow-popups"
    );

    private CoopUtil() {
    }

    public static String parse(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        int sc = header.indexOf(';');
        String token = (sc < 0 ? header : header.substring(0, sc)).trim().toLowerCase(Locale.ROOT);
        return token;
    }

    public static boolean sameOrigin(String header) {
        return "same-origin".equals(parse(header));
    }

    public static boolean known(String header) {
        return KNOWN.contains(parse(header));
    }
}
