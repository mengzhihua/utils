package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Collection helpers for emptiness, mapping, grouping and paging in memory.
 */
public final class CollectionUtil {

    private CollectionUtil() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static <T> T getFirst(List<T> list) {
        return isEmpty(list) ? null : list.get(0);
    }

    public static <T> T getLast(List<T> list) {
        return isEmpty(list) ? null : list.get(list.size() - 1);
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? new ArrayList<>() : list;
    }

    public static <T, R> List<R> map(Collection<T> source, Function<T, R> mapper) {
        if (isEmpty(source) || mapper == null) {
            return new ArrayList<>();
        }
        return source.stream().map(mapper).collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T> List<T> filter(Collection<T> source, Predicate<T> predicate) {
        if (isEmpty(source) || predicate == null) {
            return new ArrayList<>();
        }
        return source.stream().filter(predicate).collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T, K> Map<K, T> toMap(Collection<T> source, Function<T, K> keyMapper) {
        return toMap(source, keyMapper, Function.identity());
    }

    public static <T, K, V> Map<K, V> toMap(Collection<T> source, Function<T, K> keyMapper, Function<T, V> valueMapper) {
        if (isEmpty(source) || keyMapper == null || valueMapper == null) {
            return new LinkedHashMap<>();
        }
        Map<K, V> map = new LinkedHashMap<>();
        for (T item : source) {
            if (item == null) {
                continue;
            }
            map.put(keyMapper.apply(item), valueMapper.apply(item));
        }
        return map;
    }

    public static <T, K> Map<K, List<T>> groupBy(Collection<T> source, Function<T, K> classifier) {
        if (isEmpty(source) || classifier == null) {
            return new LinkedHashMap<>();
        }
        return source.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(classifier, LinkedHashMap::new, Collectors.toList()));
    }

    public static <T> List<T> distinct(Collection<T> source) {
        if (isEmpty(source)) {
            return new ArrayList<>();
        }
        return source.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    public static <T, K> List<T> distinctBy(Collection<T> source, Function<T, K> keyMapper) {
        if (isEmpty(source) || keyMapper == null) {
            return new ArrayList<>();
        }
        Map<K, T> seen = new LinkedHashMap<>();
        for (T item : source) {
            if (item == null) {
                continue;
            }
            seen.putIfAbsent(keyMapper.apply(item), item);
        }
        return new ArrayList<>(seen.values());
    }

    public static <T> List<List<T>> partition(List<T> list, int size) {
        if (isEmpty(list)) {
            return new ArrayList<>();
        }
        if (size <= 0) {
            throw new IllegalArgumentException("partition size must be greater than 0");
        }
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            result.add(new ArrayList<>(list.subList(i, Math.min(i + size, list.size()))));
        }
        return result;
    }

    public static <T> List<T> page(List<T> list, int page, int size) {
        if (isEmpty(list) || page < 1 || size < 1) {
            return Collections.emptyList();
        }
        int from = (page - 1) * size;
        if (from >= list.size()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(list.subList(from, Math.min(from + size, list.size())));
    }
}
