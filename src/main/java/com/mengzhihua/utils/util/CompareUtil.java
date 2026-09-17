package com.mengzhihua.utils.util;

/**
 * Null-safe comparisons (Hutool {@code CompareUtil} / Guava Ordering).
 */
public final class CompareUtil {

    private CompareUtil() {
    }

    public static <T extends Comparable<? super T>> int compare(T a, T b) {
        return compare(a, b, false);
    }

    public static <T extends Comparable<? super T>> int compare(T a, T b, boolean nullGreater) {
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return nullGreater ? 1 : -1;
        }
        if (b == null) {
            return nullGreater ? -1 : 1;
        }
        return a.compareTo(b);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T min(T... values) {
        return extreme(true, values);
    }

    @SafeVarargs
    public static <T extends Comparable<? super T>> T max(T... values) {
        return extreme(false, values);
    }

    @SafeVarargs
    private static <T extends Comparable<? super T>> T extreme(boolean min, T... values) {
        if (values == null || values.length == 0) {
            return null;
        }
        T best = null;
        for (T value : values) {
            if (value == null) {
                continue;
            }
            if (best == null || (min ? value.compareTo(best) < 0 : value.compareTo(best) > 0)) {
                best = value;
            }
        }
        return best;
    }
}
