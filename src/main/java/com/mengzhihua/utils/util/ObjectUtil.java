package com.mengzhihua.utils.util;

/**
 * Null-safe emptiness checks for common Java types.
 */
public final class ObjectUtil {

    private ObjectUtil() {
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof CharSequence sequence) {
            return sequence.isEmpty();
        }
        if (obj instanceof java.util.Collection<?> collection) {
            return collection.isEmpty();
        }
        if (obj instanceof java.util.Map<?, ?> map) {
            return map.isEmpty();
        }
        if (obj instanceof java.util.Optional<?> optional) {
            return optional.isEmpty();
        }
        if (obj.getClass().isArray()) {
            return java.lang.reflect.Array.getLength(obj) == 0;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    @SafeVarargs
    public static <T> T firstNonNull(T... values) {
        if (values == null) {
            return null;
        }
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    public static boolean equals(Object a, Object b) {
        return java.util.Objects.equals(a, b);
    }

    public static int hash(Object... values) {
        return java.util.Objects.hash(values);
    }
}
