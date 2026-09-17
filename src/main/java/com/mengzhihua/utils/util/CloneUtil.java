package com.mengzhihua.utils.util;

/**
 * Deep clone via JSON. Requires a Jackson-friendly type with a default constructor / records.
 */
public final class CloneUtil {

    private CloneUtil() {
    }

    public static <T> T deep(T source, Class<T> type) {
        if (source == null) {
            return null;
        }
        return JsonUtil.fromJson(JsonUtil.toJson(source), type);
    }
}
