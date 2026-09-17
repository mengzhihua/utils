package com.mengzhihua.utils.common.bean;


import com.mengzhihua.utils.common.json.JsonUtil;

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
