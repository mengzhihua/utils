package com.mengzhihua.utils.common.lang;


import com.mengzhihua.utils.common.bean.ConvertUtil;

/**
 * Boolean parsing and logic helpers.
 */
public final class BooleanUtil {

    private BooleanUtil() {
    }

    public static boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    public static boolean isFalse(Boolean value) {
        return Boolean.FALSE.equals(value);
    }

    public static boolean isTrue(Object value) {
        return isTrue(ConvertUtil.toBool(value, false));
    }

    public static Boolean negate(Boolean value) {
        return value == null ? null : !value;
    }

    public static boolean and(boolean... values) {
        if (values == null || values.length == 0) {
            return false;
        }
        for (boolean value : values) {
            if (!value) {
                return false;
            }
        }
        return true;
    }

    public static boolean or(boolean... values) {
        if (values == null || values.length == 0) {
            return false;
        }
        for (boolean value : values) {
            if (value) {
                return true;
            }
        }
        return false;
    }

    public static String toString(boolean value, String trueText, String falseText) {
        return value ? trueText : falseText;
    }
}
