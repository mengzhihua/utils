package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * RFC 7396 JSON Merge Patch.
 */
public final class JsonMergePatchUtil {

    private JsonMergePatchUtil() {
    }

    public static String apply(String targetJson, String patchJson) {
        Object target = JsonUtil.mapper().convertValue(JsonUtil.readTree(targetJson), Object.class);
        Object patch = JsonUtil.mapper().convertValue(JsonUtil.readTree(patchJson), Object.class);
        return JsonUtil.toJson(merge(target, patch));
    }

    @SuppressWarnings("unchecked")
    private static Object merge(Object target, Object patch) {
        if (!(patch instanceof Map<?, ?> patchMap)) {
            return copyOf(patch);
        }
        Map<String, Object> result = new LinkedHashMap<>();
        if (target instanceof Map<?, ?> targetMap) {
            for (Map.Entry<?, ?> entry : targetMap.entrySet()) {
                result.put(String.valueOf(entry.getKey()), copyOf(entry.getValue()));
            }
        }
        for (Map.Entry<?, ?> entry : patchMap.entrySet()) {
            String key = String.valueOf(entry.getKey());
            Object value = entry.getValue();
            if (value == null) {
                result.remove(key);
            } else {
                result.put(key, merge(result.get(key), value));
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Object copyOf(Object value) {
        if (value instanceof Map<?, ?> map) {
            Map<String, Object> copy = new LinkedHashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                copy.put(String.valueOf(entry.getKey()), copyOf(entry.getValue()));
            }
            return copy;
        }
        if (value instanceof List<?> list) {
            List<Object> copy = new ArrayList<>(list.size());
            for (Object item : list) {
                copy.add(copyOf(item));
            }
            return copy;
        }
        return value;
    }
}
