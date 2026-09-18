package com.mengzhihua.utils.common.net;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code X-XSS-Protection}.
 * Sample {@code 1; mode=block}.
 */
public final class XssProtectionUtil {

    private XssProtectionUtil() {
    }

    public static boolean isValid(String header) {
        return parseEnabled(header) != null;
    }

    public static boolean enabled(String header) {
        return Boolean.TRUE.equals(parseEnabled(header));
    }

    public static boolean modeBlock(String header) {
        return isValid(header) && header.toLowerCase(Locale.ROOT).contains("mode=block");
    }

    public static String report(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        for (String part : header.split(";")) {
            String token = part.trim();
            int eq = token.indexOf('=');
            if (eq > 0 && "report".equalsIgnoreCase(token.substring(0, eq).trim())) {
                return token.substring(eq + 1).trim();
            }
        }
        return "";
    }

    private static Boolean parseEnabled(String header) {
        if (StringUtil.isBlank(header)) {
            return null;
        }
        String[] parts = header.split(";");
        String first = parts[0].trim();
        if (!"0".equals(first) && !"1".equals(first)) {
            return null;
        }
        for (int i = 1; i < parts.length; i++) {
            String token = parts[i].trim();
            if (token.isEmpty()) {
                continue;
            }
            int eq = token.indexOf('=');
            if (eq <= 0) {
                return null;
            }
            String name = token.substring(0, eq).trim().toLowerCase(Locale.ROOT);
            String value = token.substring(eq + 1).trim();
            if ("mode".equals(name)) {
                if (!"block".equalsIgnoreCase(value)) {
                    return null;
                }
            } else if (!"report".equals(name) || value.isEmpty()) {
                return null;
            }
        }
        return "1".equals(first);
    }
}
