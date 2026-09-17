package com.mengzhihua.utils.common.bean;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Classpath presence checks.
 */
public final class ClassUtil {

    private ClassUtil() {
    }

    public static boolean isPresent(String className) {
        if (StringUtil.isBlank(className)) {
            return false;
        }
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    public static Class<?> forName(String className) {
        if (StringUtil.isBlank(className)) {
            return null;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new IllegalArgumentException("class not found: " + className, ex);
        }
    }

    public static String getSimpleName(Object obj) {
        return obj == null ? "" : obj.getClass().getSimpleName();
    }
}
