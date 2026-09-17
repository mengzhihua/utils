package com.mengzhihua.utils.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Enum lookup helpers.
 */
public final class EnumUtil {

    private EnumUtil() {
    }

    public static <E extends Enum<E>> E fromName(Class<E> type, String name) {
        if (type == null || StringUtil.isBlank(name)) {
            return null;
        }
        try {
            return Enum.valueOf(type, name);
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    public static <E extends Enum<E>> E fromNameIgnoreCase(Class<E> type, String name) {
        if (type == null || StringUtil.isBlank(name)) {
            return null;
        }
        return Arrays.stream(type.getEnumConstants())
                .filter(item -> item.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static <E extends Enum<E>, K> E from(Class<E> type, Function<E, K> keyMapper, K key) {
        if (type == null || keyMapper == null) {
            return null;
        }
        return Arrays.stream(type.getEnumConstants())
                .filter(item -> java.util.Objects.equals(keyMapper.apply(item), key))
                .findFirst()
                .orElse(null);
    }

    public static <E extends Enum<E>> Map<String, E> getEnumMap(Class<E> type) {
        Map<String, E> map = new LinkedHashMap<>();
        if (type == null) {
            return map;
        }
        for (E item : type.getEnumConstants()) {
            map.put(item.name(), item);
        }
        return map;
    }
}
