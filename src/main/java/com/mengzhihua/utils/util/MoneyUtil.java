package com.mengzhihua.utils.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 金额：元 / 分互转、分拆（红包）。
 */
public final class MoneyUtil {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private MoneyUtil() {
    }

    public static long yuanToFen(Object yuan) {
        return NumberUtil.toBigDecimal(yuan).multiply(HUNDRED).setScale(0, RoundingMode.HALF_UP).longValue();
    }

    public static BigDecimal fenToYuan(long fen) {
        return BigDecimal.valueOf(fen).divide(HUNDRED, 2, RoundingMode.HALF_UP);
    }

    public static String fenToYuanString(long fen) {
        return fenToYuan(fen).toPlainString();
    }

    /**
     * 把 {@code totalFen} 随机拆成 {@code n} 份，每份至少 1 分，总和不变。
     */
    public static List<Long> split(long totalFen, int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be greater than 0");
        }
        if (totalFen < n) {
            throw new IllegalArgumentException("total fen must be >= n");
        }
        List<Long> parts = new ArrayList<>(n);
        long remaining = totalFen;
        for (int i = 0; i < n - 1; i++) {
            int left = n - i;
            long max = remaining - (left - 1);
            long value = ThreadLocalRandom.current().nextLong(1, max + 1);
            parts.add(value);
            remaining -= value;
        }
        parts.add(remaining);
        return parts;
    }
}
