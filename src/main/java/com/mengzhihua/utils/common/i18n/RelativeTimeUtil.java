package com.mengzhihua.utils.common.i18n;


import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Locale-aware relative time, e.g. {@code 3 minutes ago} / {@code 3 分钟前}.
 */
public final class RelativeTimeUtil {

    private RelativeTimeUtil() {
    }

    public static String ofSeconds(String locale, long seconds) {
        if (Math.abs(seconds) < 45) {
            return I18nUtil.get("relative.now", LocaleUtil.parse(locale));
        }
        boolean past = seconds >= 0;
        long abs = Math.abs(seconds);
        String unit;
        long amount;
        if (abs < 3600) {
            unit = "minutes";
            amount = Math.max(1, abs / 60);
        } else if (abs < 86_400) {
            unit = "hours";
            amount = Math.max(1, abs / 3600);
        } else {
            unit = "days";
            amount = Math.max(1, abs / 86_400);
        }
        String key = "relative." + unit + (past ? ".past" : ".future");
        return I18nUtil.get(key, LocaleUtil.parse(locale), amount);
    }

    public static String ofMillis(String locale, long millis) {
        return ofSeconds(locale, millis / 1000);
    }

    public static String since(String locale, String isoDateTime) {
        return ofSeconds(locale, Duration.between(instant(isoDateTime), Instant.now()).getSeconds());
    }

    private static Instant instant(String isoDateTime) {
        if (StringUtil.isBlank(isoDateTime)) {
            return Instant.now();
        }
        String text = isoDateTime.trim();
        try {
            return Instant.parse(text);
        } catch (Exception ignored) {
            try {
                return ZonedDateTime.parse(text).toInstant();
            } catch (Exception ignoredAgain) {
                return LocalDateTime.parse(text).atZone(ZoneId.systemDefault()).toInstant();
            }
        }
    }
}
