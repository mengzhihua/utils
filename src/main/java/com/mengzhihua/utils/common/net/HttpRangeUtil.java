package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * RFC 7233 {@code Range: bytes=} parser. Sample {@code bytes=0-499}.
 */
public final class HttpRangeUtil {

    private HttpRangeUtil() {
    }

    public static List<ByteRange> parse(String header) {
        if (StringUtil.isBlank(header)) {
            throw new IllegalArgumentException("Range header is blank");
        }
        String value = header.trim();
        if (value.toLowerCase(Locale.ROOT).startsWith("bytes=")) {
            value = value.substring(6);
        }
        String[] parts = value.split(",");
        List<ByteRange> ranges = new ArrayList<>();
        for (String part : parts) {
            String item = part.trim();
            if (item.isEmpty()) {
                continue;
            }
            int dash = item.indexOf('-');
            if (dash < 0) {
                throw new IllegalArgumentException("invalid byte range: " + item);
            }
            String startText = item.substring(0, dash).trim();
            String endText = item.substring(dash + 1).trim();
            if (startText.isEmpty() && endText.isEmpty()) {
                throw new IllegalArgumentException("invalid byte range: " + item);
            }
            Long start = startText.isEmpty() ? null : Long.parseLong(startText);
            Long end = endText.isEmpty() ? null : Long.parseLong(endText);
            if (start != null && start < 0 || end != null && end < 0) {
                throw new IllegalArgumentException("negative byte range");
            }
            if (start != null && end != null && start > end) {
                throw new IllegalArgumentException("invalid byte range: " + item);
            }
            ranges.add(new ByteRange(start, end));
        }
        if (ranges.isEmpty()) {
            throw new IllegalArgumentException("no byte ranges");
        }
        return List.copyOf(ranges);
    }

    public record ByteRange(Long start, Long end) {
        public boolean suffix() {
            return start == null;
        }
    }
}
