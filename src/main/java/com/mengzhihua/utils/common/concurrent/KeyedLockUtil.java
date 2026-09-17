package com.mengzhihua.utils.common.concurrent;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * Same-key serial execution without a global lock.
 */
public final class KeyedLockUtil {

    private static final ConcurrentHashMap<String, LockEntry> LOCKS = new ConcurrentHashMap<>();

    private KeyedLockUtil() {
    }

    public static <T> T supply(String key, Supplier<T> action) {
        AssertUtil.notBlank(key, "key must not be blank");
        AssertUtil.notNull(action, "action must not be null");
        LockEntry entry = LOCKS.compute(key, (ignored, current) -> {
            LockEntry next = current == null ? new LockEntry() : current;
            next.holders.incrementAndGet();
            return next;
        });
        entry.lock.lock();
        try {
            return action.get();
        } finally {
            entry.lock.unlock();
            if (entry.holders.decrementAndGet() == 0) {
                LOCKS.remove(key, entry);
            }
        }
    }

    public static void run(String key, Runnable action) {
        supply(key, () -> {
            action.run();
            return null;
        });
    }

    public static int size() {
        return LOCKS.size();
    }

    private static final class LockEntry {
        private final ReentrantLock lock = new ReentrantLock();
        private final AtomicInteger holders = new AtomicInteger();
    }
}
