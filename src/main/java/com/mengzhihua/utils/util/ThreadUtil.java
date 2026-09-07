package com.mengzhihua.utils.util;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Sleep, virtual-thread and timeout helpers.
 */
public final class ThreadUtil {

    private ThreadUtil() {
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("sleep interrupted", ex);
        }
    }

    public static void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public static Thread startVirtual(Runnable task) {
        AssertUtil.notNull(task, "task must not be null");
        return Thread.ofVirtual().start(task);
    }

    public static CompletableFuture<Void> runAsync(Runnable task) {
        AssertUtil.notNull(task, "task must not be null");
        return CompletableFuture.runAsync(task, Thread::startVirtualThread);
    }

    public static <T> T call(Callable<T> task, long timeoutMs) {
        try {
            return CompletableFuture.supplyAsync(() -> {
                try {
                    return task.call();
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }).orTimeout(timeoutMs, TimeUnit.MILLISECONDS).join();
        } catch (RuntimeException ex) {
            throw ex;
        }
    }
}
