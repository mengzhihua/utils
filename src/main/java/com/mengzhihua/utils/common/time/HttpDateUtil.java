package com.mengzhihua.utils.common.time;


import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * RFC 7231 IMF-fixdate (HTTP Date).
 */
public final class HttpDateUtil {

    private static final DateTimeFormatter RFC_7231 = DateTimeFormatter
            .ofPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH)
            .withZone(ZoneOffset.UTC);

    private HttpDateUtil() {
    }

    public static String format(Instant instant) {
        Instant value = instant == null ? Instant.now() : instant;
        return RFC_7231.format(value);
    }

    public static String formatEpoch() {
        return format(Instant.EPOCH);
    }

    public static Instant parse(String value) {
        if (StringUtil.isBlank(value)) {
            throw new IllegalArgumentException("HTTP date is blank");
        }
        try {
            return Instant.from(RFC_7231.parse(value.trim()));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("invalid HTTP date: " + value, ex);
        }
    }
}
