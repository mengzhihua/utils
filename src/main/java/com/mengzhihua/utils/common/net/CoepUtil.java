package com.mengzhihua.utils.common.net;


import java.util.Locale;
import java.util.Set;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Cross-Origin-Embedder-Policy}.
 */
public final class CoepUtil {

    private static final Set<String> KNOWN = Set.of("unsafe-none", "require-corp", "credentialless");

    private CoepUtil() {
    }

    public static String parse(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        int sc = header.indexOf(';');
        return (sc < 0 ? header : header.substring(0, sc)).trim().toLowerCase(Locale.ROOT);
    }

    public static boolean requireCorp(String header) {
        return "require-corp".equals(parse(header));
    }

    public static boolean known(String header) {
        return KNOWN.contains(parse(header));
    }
}
