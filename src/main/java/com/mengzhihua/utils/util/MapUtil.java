package com.mengzhihua.utils.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Map builders and typed getters.
 */
public final class MapUtil {

    private MapUtil() {
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <K, V> Map<K, V> emptyIfNull(Map<K, V> map) {
        return map == null ? new LinkedHashMap<>() : map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> of(Object... keysAndValues) {
        if (keysAndValues == null || keysAndValues.length == 0) {
            return new LinkedHashMap<>();
        }
        if (keysAndValues.length % 2 != 0) {
            throw new IllegalArgumentException("keysAndValues length must be even");
        }
        Map<K, V> map = new LinkedHashMap<>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            @SuppressWarnings("unchecked")
            K key = (K) keysAndValues[i];
            @SuppressWarnings("unchecked")
            V value = (V) keysAndValues[i + 1];
            map.put(key, value);
        }
        return map;
    }

    public static <K, V> V get(Map<K, V> map, K key, V defaultValue) {
        if (map == null || !map.containsKey(key)) {
            return defaultValue;
        }
        V value = map.get(key);
        return value == null ? defaultValue : value;
    }

    public static String getStr(Map<?, ?> map, Object key) {
        return getStr(map, key, null);
    }

    public static String getStr(Map<?, ?> map, Object key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        return ConvertUtil.toStr(map.get(key), defaultValue);
    }

    public static Integer getInt(Map<?, ?> map, Object key, Integer defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        return ConvertUtil.toInt(map.get(key), defaultValue);
    }

    public static Long getLong(Map<?, ?> map, Object key, Long defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        return ConvertUtil.toLong(map.get(key), defaultValue);
    }

    public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Supplier<V> supplier) {
        if (map == null || supplier == null) {
            return null;
        }
        return map.computeIfAbsent(key, ignored -> supplier.get());
    }

    public static Map<String, Object> unmodifiable(Map<String, Object> map) {
        return map == null ? Map.of() : Collections.unmodifiableMap(map);
    }
}
