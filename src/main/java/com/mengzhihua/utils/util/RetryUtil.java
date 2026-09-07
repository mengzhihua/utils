package com.mengzhihua.utils.util;

import java.util.function.Supplier;

/**
 * Retry with fixed delay.
 */
public final class RetryUtil {

    private RetryUtil() {
    }

    public static <T> T execute(Supplier<T> action, int maxAttempts) {
        return execute(action, maxAttempts, 0L);
    }

    public static <T> T execute(Supplier<T> action, int maxAttempts, long delayMs) {
        AssertUtil.notNull(action, "action must not be null");
        AssertUtil.isTrue(maxAttempts > 0, "maxAttempts must be greater than 0");
        RuntimeException last = null;
        for (int i = 1; i <= maxAttempts; i++) {
            try {
                return action.get();
            } catch (RuntimeException ex) {
                last = ex;
                if (i < maxAttempts && delayMs > 0) {
                    ThreadUtil.sleepQuietly(delayMs);
                }
            }
        }
        throw last == null ? new IllegalStateException("retry failed") : last;
    }

    public static void execute(Runnable action, int maxAttempts, long delayMs) {
        execute(() -> {
            action.run();
            return null;
        }, maxAttempts, delayMs);
    }
}
