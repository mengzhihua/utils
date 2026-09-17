package com.mengzhihua.utils.common.concurrent;


import java.util.function.Supplier;

import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * Retry with optional delay and exponential backoff (capped at 60s).
 */
public final class RetryUtil {

    private RetryUtil() {
    }

    public static <T> T execute(Supplier<T> action, int maxAttempts) {
        return execute(action, maxAttempts, 0L);
    }

    public static <T> T execute(Supplier<T> action, int maxAttempts, long delayMs) {
        return execute(action, maxAttempts, delayMs, 1D);
    }

    public static <T> T execute(Supplier<T> action, int maxAttempts, long delayMs, double backoff) {
        AssertUtil.notNull(action, "action must not be null");
        AssertUtil.isTrue(maxAttempts > 0, "maxAttempts must be greater than 0");
        AssertUtil.isTrue(backoff >= 1D, "backoff must be >= 1");
        RuntimeException last = null;
        long wait = Math.max(0L, delayMs);
        for (int i = 1; i <= maxAttempts; i++) {
            try {
                return action.get();
            } catch (RuntimeException ex) {
                last = ex;
                if (i < maxAttempts && wait > 0) {
                    ThreadUtil.sleepQuietly(wait);
                    wait = (long) Math.min(wait * backoff, 60_000D);
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
