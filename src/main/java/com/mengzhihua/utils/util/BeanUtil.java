package com.mengzhihua.utils.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean copy and Map conversion helpers.
 */
public final class BeanUtil {

    private BeanUtil() {
    }

    public static void copy(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        BeanUtils.copyProperties(source, target);
    }

    public static <T> T copy(Object source, Class<T> targetClass) {
        if (source == null || targetClass == null) {
            return null;
        }
        T target = instantiate(targetClass);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    /**
     * Copies non-null properties only, useful for PATCH-style updates.
     */
    public static void copyIgnoreNull(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }
        BeanWrapper src = new BeanWrapperImpl(source);
        BeanWrapper trg = new BeanWrapperImpl(target);
        for (PropertyDescriptor descriptor : src.getPropertyDescriptors()) {
            String name = descriptor.getName();
            if ("class".equals(name) || !src.isReadableProperty(name) || !trg.isWritableProperty(name)) {
                continue;
            }
            Object value = src.getPropertyValue(name);
            if (value != null) {
                trg.setPropertyValue(name, value);
            }
        }
    }

    public static <T> List<T> copyList(Collection<?> sources, Class<T> targetClass) {
        if (CollectionUtil.isEmpty(sources) || targetClass == null) {
            return new ArrayList<>();
        }
        List<T> result = new ArrayList<>(sources.size());
        for (Object source : sources) {
            result.add(copy(source, targetClass));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return new LinkedHashMap<>();
        }
        if (bean instanceof Map<?, ?> map) {
            Map<String, Object> result = new LinkedHashMap<>();
            map.forEach((k, v) -> result.put(String.valueOf(k), v));
            return result;
        }
        String json = JsonUtil.toJson(bean);
        return JsonUtil.toMap(json);
    }

    public static <T> T toBean(Map<String, ?> map, Class<T> targetClass) {
        if (map == null || targetClass == null) {
            return null;
        }
        return JsonUtil.fromJson(JsonUtil.toJson(map), targetClass);
    }

    private static <T> T instantiate(Class<T> type) {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            throw new IllegalArgumentException("cannot instantiate " + type.getName(), ex);
        }
    }
}
