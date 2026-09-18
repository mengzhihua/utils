package com.mengzhihua.utils.common.net;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code X-Frame-Options}.
 */
public final class XFrameOptionsUtil {

    public record FrameOptions(String directive, String allowFrom) {
        public boolean deny() {
            return "DENY".equals(directive);
        }

        public boolean sameOrigin() {
            return "SAMEORIGIN".equals(directive);
        }
    }

    private XFrameOptionsUtil() {
    }

    public static FrameOptions parse(String header) {
        if (StringUtil.isBlank(header)) {
            throw new IllegalArgumentException("X-Frame-Options is blank");
        }
        String value = header.trim();
        String upper = value.toUpperCase(Locale.ROOT);
        if ("DENY".equals(upper) || "SAMEORIGIN".equals(upper)) {
            return new FrameOptions(upper, "");
        }
        if (upper.startsWith("ALLOW-FROM ")) {
            return new FrameOptions("ALLOW-FROM", value.substring(11).trim());
        }
        throw new IllegalArgumentException("invalid X-Frame-Options: " + header);
    }

    public static boolean deny(String header) {
        return parse(header).deny();
    }

    public static boolean sameOrigin(String header) {
        return parse(header).sameOrigin();
    }
}
