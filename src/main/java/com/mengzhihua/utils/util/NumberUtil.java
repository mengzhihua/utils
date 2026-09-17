package com.mengzhihua.utils.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Numeric helpers built on {@link BigDecimal} to avoid floating-point drift.
 */
public final class NumberUtil {

    private NumberUtil() {
    }

    public static boolean isNumber(String text) {
        if (StringUtil.isBlank(text)) {
            return false;
        }
        try {
            new BigDecimal(text.trim());
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal decimal) {
            return decimal;
        }
        if (value instanceof Number number) {
            return new BigDecimal(number.toString());
        }
        String text = value.toString().trim();
        if (text.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(text);
    }

    public static BigDecimal add(Object a, Object b) {
        return toBigDecimal(a).add(toBigDecimal(b));
    }

    public static BigDecimal subtract(Object a, Object b) {
        return toBigDecimal(a).subtract(toBigDecimal(b));
    }

    public static BigDecimal multiply(Object a, Object b) {
        return toBigDecimal(a).multiply(toBigDecimal(b));
    }

    public static BigDecimal divide(Object a, Object b, int scale) {
        return divide(a, b, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal divide(Object a, Object b, int scale, RoundingMode roundingMode) {
        BigDecimal divisor = toBigDecimal(b);
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("division by zero");
        }
        return toBigDecimal(a).divide(divisor, scale, roundingMode);
    }

    public static String formatMoney(Object amount) {
        return toBigDecimal(amount).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    public static String format(Object amount, int scale) {
        return toBigDecimal(amount).setScale(scale, RoundingMode.HALF_UP).toPlainString();
    }

    public static boolean gt(Object a, Object b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) > 0;
    }

    public static boolean gte(Object a, Object b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) >= 0;
    }

    public static boolean lt(Object a, Object b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) < 0;
    }

    public static boolean eq(Object a, Object b) {
        return toBigDecimal(a).compareTo(toBigDecimal(b)) == 0;
    }

    public static int toInt(Object value, int defaultValue) {
        try {
            return toBigDecimal(value).intValue();
        } catch (RuntimeException ex) {
            return defaultValue;
        }
    }

    public static long toLong(Object value, long defaultValue) {
        try {
            return toBigDecimal(value).longValue();
        } catch (RuntimeException ex) {
            return defaultValue;
        }
    }

    public static String percent(Object value, int scale) {
        return toBigDecimal(value).multiply(new BigDecimal("100"))
                .setScale(scale, RoundingMode.HALF_UP)
                .toPlainString() + "%";
    }

    public static String padZero(long value, int width) {
        return String.format(Locale.ROOT, "%0" + width + "d", value);
    }
}
