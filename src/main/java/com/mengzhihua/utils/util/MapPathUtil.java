package com.mengzhihua.utils.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Dot-path access and flatten / unflatten for nested maps.
 */
public final class MapPathUtil {

    private MapPathUtil() {
    }

    public static Object get(Map<String, ?> map, String path) {
        if (map == null || StringUtil.isBlank(path)) {
            return null;
        }
        Object current = map;
        for (String part : path.split("\\.")) {
            if (!(current instanceof Map<?, ?> nested)) {
                return null;
            }
            current = nested.get(part);
            if (current == null) {
                return null;
            }
        }
        return current;
    }

    public static String getStr(Map<String, ?> map, String path) {
        return ConvertUtil.toStr(get(map, path), null);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> flatten(Map<String, ?> map) {
        Map<String, Object> result = new LinkedHashMap<>();
        flatten(map, "", result);
        return result;
    }

    public static Map<String, Object> unflatten(Map<String, ?> flat) {
        Map<String, Object> root = new LinkedHashMap<>();
        if (MapUtil.isEmpty(flat)) {
            return root;
        }
        flat.forEach((path, value) -> put(root, path, value));
        return root;
    }

    @SuppressWarnings("unchecked")
    public static void put(Map<String, Object> map, String path, Object value) {
        AssertUtil.notNull(map, "map must not be null");
        AssertUtil.notBlank(path, "path must not be blank");
        String[] parts = path.split("\\.");
        Map<String, Object> current = map;
        for (int i = 0; i < parts.length - 1; i++) {
            Object next = current.get(parts[i]);
            if (!(next instanceof Map<?, ?>)) {
                next = new LinkedHashMap<String, Object>();
                current.put(parts[i], next);
            }
            current = (Map<String, Object>) next;
        }
        current.put(parts[parts.length - 1], value);
    }

    @SuppressWarnings("unchecked")
    private static void flatten(Map<String, ?> map, String prefix, Map<String, Object> result) {
        if (map == null) {
            return;
        }
        map.forEach((key, value) -> {
            String next = StringUtil.isBlank(prefix) ? key : prefix + "." + key;
            if (value instanceof Map<?, ?> nested) {
                flatten((Map<String, ?>) nested, next, result);
            } else {
                result.put(next, value);
            }
        });
    }
}
