package com.mengzhihua.utils.common.net;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Age} (RFC 9111). First {@code delta-seconds} member.
 */
public final class HttpAgeUtil {

    private HttpAgeUtil() {
    }

    public static Long parse(String header) {
        if (StringUtil.isBlank(header)) {
            return null;
        }
        String first = header.split(",", 2)[0].trim();
        if (!first.matches("\\d+")) {
            return null;
        }
        return Long.parseLong(first);
    }

    public static boolean valid(String header) {
        return parse(header) != null;
    }

    public static boolean fresh(String header, long maxAgeSeconds) {
        Long age = parse(header);
        return age != null && age <= maxAgeSeconds;
    }
}
