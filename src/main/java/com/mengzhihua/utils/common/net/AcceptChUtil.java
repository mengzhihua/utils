package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Accept-CH}.
 */
public final class AcceptChUtil {

    private AcceptChUtil() {
    }

    public static List<String> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<String> hints = new ArrayList<>();
        for (String part : header.split(",")) {
            String token = part.trim().toLowerCase(Locale.ROOT);
            if (!token.isEmpty()) {
                hints.add(token);
            }
        }
        return List.copyOf(hints);
    }

    public static String first(String header) {
        List<String> hints = parse(header);
        return hints.isEmpty() ? "" : hints.get(0);
    }

    public static boolean has(String header, String hint) {
        return hint != null && parse(header).contains(hint.trim().toLowerCase(Locale.ROOT));
    }
}
