package com.mengzhihua.utils.util;

/**
 * Closed comparable ranges (Guava {@code Range} subset).
 */
public final class RangeUtil {

    private RangeUtil() {
    }

    public static <T extends Comparable<? super T>> Range<T> closed(T start, T end) {
        return new Range<>(start, end, true, true);
    }

    public static <T extends Comparable<? super T>> Range<T> open(T start, T end) {
        return new Range<>(start, end, false, false);
    }

    public static <T extends Comparable<? super T>> Range<T> closedOpen(T start, T end) {
        return new Range<>(start, end, true, false);
    }

    public record Range<T extends Comparable<? super T>>(T start, T end, boolean startInclusive, boolean endInclusive) {
        public boolean contains(T value) {
            if (value == null || start == null || end == null) {
                return false;
            }
            int left = value.compareTo(start);
            int right = value.compareTo(end);
            boolean afterStart = startInclusive ? left >= 0 : left > 0;
            boolean beforeEnd = endInclusive ? right <= 0 : right < 0;
            return afterStart && beforeEnd;
        }

        public boolean isEmpty() {
            if (start == null || end == null) {
                return true;
            }
            int cmp = start.compareTo(end);
            if (cmp < 0) {
                return false;
            }
            return cmp > 0 || !startInclusive || !endInclusive;
        }
    }
}
