package com.mengzhihua.utils.common.lang;

/**
 * Page offset / total-pages calculations.
 */
public final class PageUtil {

    private PageUtil() {
    }

    public static int offset(int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        return (safePage - 1) * safeSize;
    }

    public static long pages(long total, int size) {
        if (total <= 0 || size <= 0) {
            return 0L;
        }
        return (total + size - 1) / size;
    }

    public static int normalizePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    public static int normalizeSize(Integer size, int defaultSize, int maxSize) {
        if (size == null || size < 1) {
            return defaultSize;
        }
        return Math.min(size, maxSize);
    }
}
