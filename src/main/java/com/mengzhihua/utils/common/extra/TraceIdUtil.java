package com.mengzhihua.utils.common.extra;


import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Request trace ids for logs / headers.
 */
public final class TraceIdUtil {

    public static final String HEADER = "X-Trace-Id";

    private static final ThreadLocal<String> HOLDER = new ThreadLocal<>();

    private TraceIdUtil() {
    }

    public static String create() {
        return IdUtil.simpleUuid();
    }

    public static void set(String traceId) {
        HOLDER.set(StringUtil.defaultIfBlank(traceId, create()));
    }

    public static String get() {
        String current = HOLDER.get();
        if (current == null) {
            current = create();
            HOLDER.set(current);
        }
        return current;
    }

    public static void clear() {
        HOLDER.remove();
    }
}
