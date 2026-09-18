package com.mengzhihua.utils.common.net;


import java.time.Instant;

import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.time.HttpDateUtil;

/**
 * HTTP {@code Retry-After} (RFC 9110). Delay-seconds or IMF-fixdate.
 */
public final class RetryAfterUtil {

    public record RetryAfter(Long seconds, Instant date) {
        public boolean delaySeconds() {
            return seconds != null;
        }
    }

    private RetryAfterUtil() {
    }

    public static RetryAfter parse(String header) {
        if (StringUtil.isBlank(header)) {
            throw new IllegalArgumentException("Retry-After is blank");
        }
        String value = header.trim();
        if (value.matches("\\d+")) {
            return new RetryAfter(Long.parseLong(value), null);
        }
        return new RetryAfter(null, HttpDateUtil.parse(value));
    }

    public static Long seconds(String header) {
        return parse(header).seconds();
    }

    public static Instant date(String header) {
        return parse(header).date();
    }

    public static boolean isDelaySeconds(String header) {
        return header != null && header.trim().matches("\\d+");
    }
}
