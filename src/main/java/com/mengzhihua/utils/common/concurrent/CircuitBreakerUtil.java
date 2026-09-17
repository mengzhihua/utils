package com.mengzhihua.utils.common.concurrent;


import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * Process-local circuit breaker (closed / open / half-open). Not a replacement for Resilience4j.
 */
public final class CircuitBreakerUtil {

    private static final ConcurrentHashMap<String, Breaker> BREAKERS = new ConcurrentHashMap<>();

    private CircuitBreakerUtil() {
    }

    public static boolean allow(String name) {
        return breaker(name).allow();
    }

    public static void recordSuccess(String name) {
        breaker(name).onSuccess();
    }

    public static void recordFailure(String name) {
        breaker(name).onFailure();
    }

    public static <T> T execute(String name, Supplier<T> action) {
        AssertUtil.notNull(action, "action must not be null");
        if (!allow(name)) {
            throw new IllegalStateException("circuit open: " + name);
        }
        try {
            T value = action.get();
            recordSuccess(name);
            return value;
        } catch (RuntimeException ex) {
            recordFailure(name);
            throw ex;
        }
    }

    public static State state(String name) {
        return breaker(name).state();
    }

    public static void reset(String name) {
        BREAKERS.remove(name);
    }

    public static void clear() {
        BREAKERS.clear();
    }

    private static Breaker breaker(String name) {
        AssertUtil.notBlank(name, "name must not be blank");
        return BREAKERS.computeIfAbsent(name, ignored -> new Breaker(5, Duration.ofSeconds(10)));
    }

    public enum State {
        CLOSED, OPEN, HALF_OPEN
    }

    private static final class Breaker {
        private final int failureThreshold;
        private final long openMillis;
        private int failures;
        private State state = State.CLOSED;
        private long openedAt;

        private Breaker(int failureThreshold, Duration openDuration) {
            this.failureThreshold = failureThreshold;
            this.openMillis = openDuration.toMillis();
        }

        private synchronized boolean allow() {
            if (state == State.CLOSED) {
                return true;
            }
            if (state == State.OPEN && System.currentTimeMillis() - openedAt >= openMillis) {
                state = State.HALF_OPEN;
                return true;
            }
            return state == State.HALF_OPEN;
        }

        private synchronized void onSuccess() {
            failures = 0;
            state = State.CLOSED;
        }

        private synchronized void onFailure() {
            failures++;
            if (state == State.HALF_OPEN || failures >= failureThreshold) {
                state = State.OPEN;
                openedAt = System.currentTimeMillis();
            }
        }

        private synchronized State state() {
            allow();
            return state;
        }
    }
}
