package com.mengzhihua.utils.common.math;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Percentage helpers on {@link BigDecimal}.
 */
public final class PercentUtil {

    private PercentUtil() {
    }

    public static BigDecimal of(Object part, Object total) {
        return of(part, total, 2);
    }

    public static BigDecimal of(Object part, Object total, int scale) {
        BigDecimal divisor = NumberUtil.toBigDecimal(total);
        if (divisor.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(scale, RoundingMode.HALF_UP);
        }
        return NumberUtil.toBigDecimal(part)
                .multiply(new BigDecimal("100"))
                .divide(divisor, scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal apply(Object amount, Object percent) {
        return apply(amount, percent, 2);
    }

    public static BigDecimal apply(Object amount, Object percent, int scale) {
        return NumberUtil.toBigDecimal(amount)
                .multiply(NumberUtil.toBigDecimal(percent))
                .divide(new BigDecimal("100"), scale, RoundingMode.HALF_UP);
    }

    public static BigDecimal change(Object from, Object to) {
        return of(NumberUtil.subtract(to, from), from, 2);
    }

    public static String format(Object percent) {
        return NumberUtil.format(percent, 2) + "%";
    }
}
