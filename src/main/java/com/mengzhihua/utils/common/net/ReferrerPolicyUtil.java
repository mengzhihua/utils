package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Referrer-Policy}.
 */
public final class ReferrerPolicyUtil {

    private static final Set<String> KNOWN = Set.of(
            "no-referrer",
            "no-referrer-when-downgrade",
            "origin",
            "origin-when-cross-origin",
            "same-origin",
            "strict-origin",
            "strict-origin-when-cross-origin",
            "unsafe-url"
    );

    private ReferrerPolicyUtil() {
    }

    public static List<String> parse(String header) {
        List<String> tokens = new ArrayList<>();
        if (StringUtil.isBlank(header)) {
            return tokens;
        }
        for (String part : header.split(",")) {
            String token = part.trim().toLowerCase(Locale.ROOT);
            if (!token.isEmpty()) {
                tokens.add(token);
            }
        }
        return List.copyOf(tokens);
    }

    public static boolean has(String header, String policy) {
        return policy != null && parse(header).contains(policy.toLowerCase(Locale.ROOT));
    }

    public static boolean known(String policy) {
        return policy != null && KNOWN.contains(policy.toLowerCase(Locale.ROOT));
    }
}
