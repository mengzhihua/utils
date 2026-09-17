package com.mengzhihua.utils.common.id;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.NumberUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;

/**
 * Date-scoped monotonic sequences: {@code prefix + yyyyMMdd + padded seq}.
 */
public final class SeqUtil {

    private static final Map<String, AtomicLong> COUNTERS = new ConcurrentHashMap<>();
    private static final int DEFAULT_WIDTH = 6;

    private SeqUtil() {
    }

    public static String next() {
        return next("S");
    }

    public static String next(String prefix) {
        return next(prefix, DEFAULT_WIDTH);
    }

    public static String next(String prefix, int width) {
        if (width <= 0 || width > 18) {
            throw new IllegalArgumentException("width must be 1-18");
        }
        String head = StringUtil.defaultIfBlank(prefix, "S");
        String day = DateTimeUtil.format(DateTimeUtil.today(), "yyyyMMdd");
        String key = head + ":" + day;
        long n = COUNTERS.computeIfAbsent(key, k -> new AtomicLong()).incrementAndGet();
        return head + day + NumberUtil.padZero(n, width);
    }

    public static long current(String prefix) {
        String head = StringUtil.defaultIfBlank(prefix, "S");
        String day = DateTimeUtil.format(DateTimeUtil.today(), "yyyyMMdd");
        AtomicLong counter = COUNTERS.get(head + ":" + day);
        return counter == null ? 0L : counter.get();
    }
}
