package com.mengzhihua.utils.util;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Safe type conversions with default fallbacks.
 */
public final class ConvertUtil {

    private ConvertUtil() {
    }

    public static String toStr(Object value) {
        return toStr(value, null);
    }

    public static String toStr(Object value, String defaultValue) {
        return value == null ? defaultValue : Objects.toString(value, defaultValue);
    }

    public static Integer toInt(Object value) {
        return toInt(value, null);
    }

    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null || StringUtil.isBlank(value.toString())) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value.toString().trim()).intValue();
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static Long toLong(Object value) {
        return toLong(value, null);
    }

    public static Long toLong(Object value, Long defaultValue) {
        if (value == null || StringUtil.isBlank(value.toString())) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value.toString().trim()).longValue();
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static Double toDouble(Object value, Double defaultValue) {
        if (value == null || StringUtil.isBlank(value.toString())) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value.toString().trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public static Boolean toBool(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        String text = value.toString().trim();
        if (text.equalsIgnoreCase("true") || text.equals("1") || text.equalsIgnoreCase("yes") || text.equalsIgnoreCase("y")) {
            return Boolean.TRUE;
        }
        if (text.equalsIgnoreCase("false") || text.equals("0") || text.equalsIgnoreCase("no") || text.equalsIgnoreCase("n")) {
            return Boolean.FALSE;
        }
        return defaultValue;
    }
}
