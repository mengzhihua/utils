package com.mengzhihua.utils.common.concurrent;


import java.time.Duration;
import java.util.concurrent.TimeUnit;

import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;

/**
 * Lightweight stopwatch for timing a block of work.
 */
public final class StopWatchUtil {

    private final long startNanos;
    private long stopNanos = -1L;
    private final String name;

    public StopWatchUtil() {
        this("task");
    }

    public StopWatchUtil(String name) {
        this.name = StringUtil.defaultIfBlank(name, "task");
        this.startNanos = System.nanoTime();
    }

    public static StopWatchUtil start() {
        return new StopWatchUtil();
    }

    public static StopWatchUtil start(String name) {
        return new StopWatchUtil(name);
    }

    public StopWatchUtil stop() {
        this.stopNanos = System.nanoTime();
        return this;
    }

    public long elapsedNanos() {
        long end = stopNanos > 0 ? stopNanos : System.nanoTime();
        return end - startNanos;
    }

    public long elapsedMillis() {
        return TimeUnit.NANOSECONDS.toMillis(elapsedNanos());
    }

    public Duration elapsed() {
        return Duration.ofNanos(elapsedNanos());
    }

    public String pretty() {
        return name + " " + DateTimeUtil.formatDuration(elapsed());
    }

    @Override
    public String toString() {
        return pretty();
    }
}
