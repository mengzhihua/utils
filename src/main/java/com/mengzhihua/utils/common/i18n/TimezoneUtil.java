package com.mengzhihua.utils.common.i18n;


import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Locale-aware time zone display names and offsets.
 */
public final class TimezoneUtil {

    private TimezoneUtil() {
    }

    public static String displayName(String zone, String locale) {
        return zoneId(zone).getDisplayName(TextStyle.FULL, LocaleUtil.parse(locale));
    }

    public static String shortName(String zone, String locale) {
        return zoneId(zone).getDisplayName(TextStyle.SHORT, LocaleUtil.parse(locale));
    }

    public static String offset(String zone) {
        ZoneOffset offset = ZonedDateTime.now(zoneId(zone)).getOffset();
        return offset.getId();
    }

    public static String id(String zone) {
        return zoneId(zone).getId();
    }

    private static ZoneId zoneId(String zone) {
        if (StringUtil.isBlank(zone)) {
            return ZoneId.of("UTC");
        }
        return ZoneId.of(zone.trim());
    }
}
