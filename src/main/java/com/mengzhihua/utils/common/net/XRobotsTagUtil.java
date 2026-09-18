package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code X-Robots-Tag} token parser.
 */
public final class XRobotsTagUtil {

    private XRobotsTagUtil() {
    }

    public static List<String> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<String> tokens = new ArrayList<>();
        for (String part : header.split(",")) {
            String token = part.trim().toLowerCase(Locale.ROOT);
            if (token.isEmpty()) {
                continue;
            }
            tokens.add(token);
            int colon = token.indexOf(':');
            if (colon >= 0) {
                String directive = token.substring(colon + 1).trim();
                if (!directive.isEmpty()) {
                    tokens.add(directive);
                }
            }
        }
        return List.copyOf(tokens);
    }

    public static String first(String header) {
        List<String> tokens = parse(header);
        return tokens.isEmpty() ? "" : tokens.get(0);
    }

    public static boolean has(String header, String directive) {
        return directive != null && parse(header).contains(directive.trim().toLowerCase(Locale.ROOT));
    }

    public static boolean noindex(String header) {
        return has(header, "noindex") || has(header, "none");
    }
}
