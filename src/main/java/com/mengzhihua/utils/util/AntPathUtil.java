package com.mengzhihua.utils.util;

import org.springframework.util.AntPathMatcher;

/**
 * Ant-style path matching ({@code /api/**}, {@code *.json}).
 */
public final class AntPathUtil {

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    private AntPathUtil() {
    }

    public static boolean match(String pattern, String path) {
        if (pattern == null || path == null) {
            return false;
        }
        return MATCHER.match(pattern, path);
    }

    public static boolean matchAny(String path, String... patterns) {
        if (patterns == null) {
            return false;
        }
        for (String pattern : patterns) {
            if (match(pattern, path)) {
                return true;
            }
        }
        return false;
    }

    public static String extractPath(String pattern, String path) {
        if (!match(pattern, path)) {
            return null;
        }
        return MATCHER.extractPathWithinPattern(pattern, path);
    }
}
