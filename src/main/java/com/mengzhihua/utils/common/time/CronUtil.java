package com.mengzhihua.utils.common.time;


import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.support.CronExpression;

/**
 * Spring 6 字段 Cron（秒 分 时 日 月 周）下次触发时间。
 */
public final class CronUtil {

    private CronUtil() {
    }

    public static boolean isValid(String expression) {
        try {
            CronExpression.parse(expression);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public static ZonedDateTime next(String expression, ZonedDateTime from) {
        CronExpression cron = CronExpression.parse(expression);
        ZonedDateTime start = from == null ? ZonedDateTime.now(DateTimeUtil.DEFAULT_ZONE) : from;
        return cron.next(start);
    }

    public static List<String> nextTimes(String expression, ZonedDateTime from, int count) {
        List<String> times = new ArrayList<>();
        ZonedDateTime cursor = from == null ? ZonedDateTime.now(DateTimeUtil.DEFAULT_ZONE) : from;
        for (int i = 0; i < count; i++) {
            cursor = next(expression, cursor);
            if (cursor == null) {
                break;
            }
            times.add(DateTimeUtil.format(cursor.toLocalDateTime()));
        }
        return times;
    }
}
