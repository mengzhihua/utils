package com.mengzhihua.utils.common.bean;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Small reflection helpers for field get/set and method invoke.
 */
public final class ReflectUtil {

    private ReflectUtil() {
    }

    public static Object getFieldValue(Object target, String fieldName) {
        if (target == null || StringUtil.isBlank(fieldName)) {
            return null;
        }
        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("cannot read field " + fieldName, ex);
        }
    }

    public static void setFieldValue(Object target, String fieldName, Object value) {
        if (target == null || StringUtil.isBlank(fieldName)) {
            return;
        }
        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("cannot write field " + fieldName, ex);
        }
    }

    public static Object invoke(Object target, String methodName, Object... args) {
        if (target == null || StringUtil.isBlank(methodName)) {
            return null;
        }
        try {
            Class<?>[] types = new Class<?>[args == null ? 0 : args.length];
            if (args != null) {
                for (int i = 0; i < args.length; i++) {
                    types[i] = args[i] == null ? Object.class : args[i].getClass();
                }
            }
            Method method = target.getClass().getMethod(methodName, types);
            method.setAccessible(true);
            return method.invoke(target, args);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalArgumentException("cannot invoke " + methodName, ex);
        }
    }

    private static Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null && current != Object.class) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}
