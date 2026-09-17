package com.mengzhihua.utils.common.lang;


import java.util.List;
import java.util.function.Consumer;

/**
 * Process a list in fixed-size batches.
 */
public final class BatchUtil {

    private BatchUtil() {
    }

    public static <T> void forEach(List<T> list, int size, Consumer<List<T>> consumer) {
        AssertUtil.notNull(consumer, "consumer must not be null");
        AssertUtil.isTrue(size > 0, "size must be greater than 0");
        if (CollectionUtil.isEmpty(list)) {
            return;
        }
        for (List<T> batch : CollectionUtil.partition(list, size)) {
            consumer.accept(batch);
        }
    }

    public static int batchCount(int total, int size) {
        if (total <= 0 || size <= 0) {
            return 0;
        }
        return (total + size - 1) / size;
    }
}
