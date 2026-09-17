package com.mengzhihua.utils.util;

import com.mengzhihua.utils.common.exception.BizException;
import com.mengzhihua.utils.common.api.ResultCode;

import java.util.Collection;
import java.util.Map;

/**
 * Fail-fast assertions that throw {@link BizException}.
 */
public final class AssertUtil {

    private AssertUtil() {
    }

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new BizException(ResultCode.BAD_REQUEST, message);
        }
    }

    public static void isFalse(boolean expression, String message) {
        isTrue(!expression, message);
    }

    public static void notNull(Object value, String message) {
        isTrue(value != null, message);
    }

    public static void isNull(Object value, String message) {
        isTrue(value == null, message);
    }

    public static void notBlank(String value, String message) {
        isTrue(StringUtil.isNotBlank(value), message);
    }

    public static void notEmpty(Collection<?> collection, String message) {
        isTrue(collection != null && !collection.isEmpty(), message);
    }

    public static void notEmpty(Map<?, ?> map, String message) {
        isTrue(map != null && !map.isEmpty(), message);
    }

    public static void notEmpty(Object[] array, String message) {
        isTrue(array != null && array.length > 0, message);
    }
}
