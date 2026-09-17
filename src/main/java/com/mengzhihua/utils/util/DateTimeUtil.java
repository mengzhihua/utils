package com.mengzhihua.utils.util;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Date and time helpers based on {@code java.time}.
 */
public final class DateTimeUtil {

    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final String TIME_PATTERN = "HH:mm:ss";
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Shanghai");

    private static final Map<String, DateTimeFormatter> FORMATTERS = new ConcurrentHashMap<>();

    static {
        FORMATTERS.put(DATETIME_PATTERN, DateTimeFormatter.ofPattern(DATETIME_PATTERN));
        FORMATTERS.put(DATE_PATTERN, DateTimeFormatter.ofPattern(DATE_PATTERN));
        FORMATTERS.put(TIME_PATTERN, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    private DateTimeUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now(DEFAULT_ZONE);
    }

    public static LocalDate today() {
        return LocalDate.now(DEFAULT_ZONE);
    }

    public static String nowDateTime() {
        return format(now(), DATETIME_PATTERN);
    }

    public static String nowDate() {
        return format(today(), DATE_PATTERN);
    }

    public static String format(LocalDateTime dateTime) {
        return format(dateTime, DATETIME_PATTERN);
    }

    public static String format(LocalDateTime dateTime, String pattern) {
        if (dateTime == null) {
            return null;
        }
        return formatter(pattern).format(dateTime);
    }

    public static String format(LocalDate date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(LocalDate date, String pattern) {
        if (date == null) {
            return null;
        }
        return formatter(pattern).format(date);
    }

    public static LocalDateTime parseDateTime(String text) {
        return parseDateTime(text, DATETIME_PATTERN);
    }

    public static LocalDateTime parseDateTime(String text, String pattern) {
        if (StringUtil.isBlank(text)) {
            return null;
        }
        return LocalDateTime.parse(text.trim(), formatter(pattern));
    }

    public static LocalDate parseDate(String text) {
        return parseDate(text, DATE_PATTERN);
    }

    public static LocalDate parseDate(String text, String pattern) {
        if (StringUtil.isBlank(text)) {
            return null;
        }
        return LocalDate.parse(text.trim(), formatter(pattern));
    }

    public static long toEpochMilli(LocalDateTime dateTime) {
        if (dateTime == null) {
            return 0L;
        }
        return dateTime.atZone(DEFAULT_ZONE).toInstant().toEpochMilli();
    }

    public static LocalDateTime ofEpochMilli(long epochMilli) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), DEFAULT_ZONE);
    }

    public static Date toDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return Date.from(dateTime.atZone(DEFAULT_ZONE).toInstant());
    }

    public static LocalDateTime fromDate(Date date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.ofInstant(date.toInstant(), DEFAULT_ZONE);
    }

    public static LocalDateTime startOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.atStartOfDay();
    }

    public static LocalDateTime endOfDay(LocalDate date) {
        if (date == null) {
            return null;
        }
        return LocalDateTime.of(date, LocalTime.MAX.withNano(0));
    }

    public static LocalDateTime plusDays(LocalDateTime dateTime, long days) {
        return dateTime == null ? null : dateTime.plusDays(days);
    }

    public static LocalDateTime plusHours(LocalDateTime dateTime, long hours) {
        return dateTime == null ? null : dateTime.plusHours(hours);
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0L;
        }
        return ChronoUnit.DAYS.between(start, end);
    }

    public static boolean isBetween(LocalDateTime target, LocalDateTime start, LocalDateTime end) {
        if (target == null || start == null || end == null) {
            return false;
        }
        return !target.isBefore(start) && !target.isAfter(end);
    }

    public static boolean isWeekend(LocalDate date) {
        if (date == null) {
            return false;
        }
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public static int age(LocalDate birthday) {
        if (birthday == null) {
            return 0;
        }
        return Period.between(birthday, today()).getYears();
    }

    public static LocalDate startOfWeek(LocalDate date) {
        return date == null ? null : date.with(DayOfWeek.MONDAY);
    }

    public static LocalDate endOfWeek(LocalDate date) {
        return date == null ? null : date.with(DayOfWeek.SUNDAY);
    }

    public static LocalDate startOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfMonth());
    }

    public static LocalDate endOfMonth(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.lastDayOfMonth());
    }

    public static LocalDate startOfYear(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.firstDayOfYear());
    }

    public static LocalDate endOfYear(LocalDate date) {
        return date == null ? null : date.with(TemporalAdjusters.lastDayOfYear());
    }

    public static String formatDuration(Duration duration) {
        if (duration == null) {
            return "0s";
        }
        long seconds = Math.abs(duration.getSeconds());
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m " + secs + "s";
        }
        if (minutes > 0) {
            return minutes + "m " + secs + "s";
        }
        return secs + "s";
    }

    public static String fromNow(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        long seconds = Duration.between(dateTime, now()).getSeconds();
        boolean future = seconds < 0;
        long abs = Math.abs(seconds);
        String suffix = future ? "后" : "前";
        if (abs < 10) {
            return future ? "马上" : "刚刚";
        }
        if (abs < 60) {
            return abs + "秒" + suffix;
        }
        if (abs < 3600) {
            return (abs / 60) + "分钟" + suffix;
        }
        if (abs < 86400) {
            return (abs / 3600) + "小时" + suffix;
        }
        if (abs < 86400L * 30) {
            return (abs / 86400) + "天" + suffix;
        }
        return format(dateTime);
    }

    public static boolean isWorkday(LocalDate date) {
        return date != null && !isWeekend(date);
    }

    public static LocalDate plusWorkdays(LocalDate date, int days) {
        if (date == null) {
            return null;
        }
        int step = days < 0 ? -1 : 1;
        int remaining = Math.abs(days);
        LocalDate cursor = date;
        while (remaining > 0) {
            cursor = cursor.plusDays(step);
            if (isWorkday(cursor)) {
                remaining--;
            }
        }
        return cursor;
    }

    public static long workdaysBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) {
            return 0L;
        }
        LocalDate from = start.isAfter(end) ? end : start;
        LocalDate to = start.isAfter(end) ? start : end;
        long count = 0;
        for (LocalDate cursor = from; cursor.isBefore(to); cursor = cursor.plusDays(1)) {
            if (isWorkday(cursor)) {
                count++;
            }
        }
        return start.isAfter(end) ? -count : count;
    }

    public static int quarter(LocalDate date) {
        return date == null ? 0 : (date.getMonthValue() - 1) / 3 + 1;
    }

    public static boolean isLeapYear(LocalDate date) {
        return date != null && date.isLeapYear();
    }

    public static String formatBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            return "";
        }
        return formatBetween(Duration.between(start, end));
    }

    /**
     * Hutool-style between formatter, e.g. {@code 2天3小时5分钟}.
     */
    public static String formatBetween(Duration duration) {
        if (duration == null) {
            return "0秒";
        }
        long seconds = Math.abs(duration.getSeconds());
        long days = seconds / 86_400;
        long hours = (seconds % 86_400) / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;
        StringBuilder builder = new StringBuilder();
        if (days > 0) {
            builder.append(days).append("天");
        }
        if (hours > 0) {
            builder.append(hours).append("小时");
        }
        if (minutes > 0) {
            builder.append(minutes).append("分钟");
        }
        if (secs > 0 || builder.isEmpty()) {
            builder.append(secs).append("秒");
        }
        return builder.toString();
    }

    private static DateTimeFormatter formatter(String pattern) {
        String key = StringUtil.defaultIfBlank(pattern, DATETIME_PATTERN);
        return FORMATTERS.computeIfAbsent(key, DateTimeFormatter::ofPattern);
    }
}
