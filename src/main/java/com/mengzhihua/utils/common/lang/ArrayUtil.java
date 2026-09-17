package com.mengzhihua.utils.common.lang;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Array emptiness, contains and conversion helpers.
 */
public final class ArrayUtil {

    private ArrayUtil() {
    }

    public static boolean isEmpty(Object array) {
        return array == null || !array.getClass().isArray() || Array.getLength(array) == 0;
    }

    public static boolean isNotEmpty(Object array) {
        return !isEmpty(array);
    }

    public static int length(Object array) {
        return isEmpty(array) ? 0 : Array.getLength(array);
    }

    public static boolean contains(Object[] array, Object value) {
        if (array == null) {
            return false;
        }
        for (Object item : array) {
            if (Objects.equals(item, value)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T get(T[] array, int index) {
        if (array == null || index < 0 || index >= array.length) {
            return null;
        }
        return array[index];
    }

    public static <T> T first(T[] array) {
        return get(array, 0);
    }

    public static <T> T last(T[] array) {
        return array == null || array.length == 0 ? null : array[array.length - 1];
    }

    @SafeVarargs
    public static <T> T[] add(T[] array, T... items) {
        if (array == null) {
            return items;
        }
        if (items == null || items.length == 0) {
            return array;
        }
        T[] result = Arrays.copyOf(array, array.length + items.length);
        System.arraycopy(items, 0, result, array.length, items.length);
        return result;
    }

    public static <T> List<T> toList(T[] array) {
        if (array == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(array));
    }
}
