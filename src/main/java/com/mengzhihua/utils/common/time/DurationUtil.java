package com.mengzhihua.utils.common.time;


import java.time.Duration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Parse human durations such as {@code 1h30m}, {@code 90s}, {@code 500ms}, or ISO-8601 {@code PT15M}.
 */
public final class DurationUtil {

    private static final Pattern TOKEN = Pattern.compile("(\\d+)\\s*(ms|s|m|h|d)(?![a-zA-Z])", Pattern.CASE_INSENSITIVE);

    private DurationUtil() {
    }

    public static Duration parse(String text) {
        if (StringUtil.isBlank(text)) {
            throw new IllegalArgumentException("duration is blank");
        }
        String trimmed = text.trim();
        if (trimmed.regionMatches(true, 0, "P", 0, 1)) {
            return Duration.parse(trimmed.toUpperCase(Locale.ROOT));
        }
        if (trimmed.chars().allMatch(Character::isDigit)) {
            return Duration.ofMillis(Long.parseLong(trimmed));
        }
        Matcher matcher = TOKEN.matcher(trimmed);
        long millis = 0L;
        boolean found = false;
        int cursor = 0;
        while (matcher.find()) {
            if (!trimmed.substring(cursor, matcher.start()).isBlank()) {
                throw new IllegalArgumentException("invalid duration: " + text);
            }
            found = true;
            cursor = matcher.end();
            long value = Long.parseLong(matcher.group(1));
            millis += switch (matcher.group(2).toLowerCase(Locale.ROOT)) {
                case "ms" -> value;
                case "s" -> value * 1_000L;
                case "m" -> value * 60_000L;
                case "h" -> value * 3_600_000L;
                case "d" -> value * 86_400_000L;
                default -> 0L;
            };
        }
        if (!found || !trimmed.substring(cursor).isBlank()) {
            throw new IllegalArgumentException("invalid duration: " + text);
        }
        return Duration.ofMillis(millis);
    }

    public static long toMillis(String text) {
        return parse(text).toMillis();
    }

    public static String format(Duration duration) {
        return DateTimeUtil.formatDuration(duration);
    }
}
