package com.mengzhihua.utils.common.id;


import java.time.format.DateTimeFormatter;

import com.mengzhihua.utils.common.lang.RandomUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;

/**
 * Business order / trade numbers: {@code prefix + yyyyMMddHHmmssSSS + random}.
 */
public final class OrderNoUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    private OrderNoUtil() {
    }

    public static String next() {
        return next("O");
    }

    public static String next(String prefix) {
        String head = StringUtil.defaultIfBlank(prefix, "O");
        String time = java.time.LocalDateTime.now(DateTimeUtil.DEFAULT_ZONE).format(FORMATTER);
        return head + time + RandomUtil.digits(4);
    }

    /**
     * Compact 32-char trade id: time(17) + snowflake tail.
     */
    public static String nextTradeNo() {
        String time = DateTimeUtil.format(DateTimeUtil.now(), "yyyyMMddHHmmssSSS");
        String snowflake = IdUtil.snowflakeIdStr();
        String tail = snowflake.length() > 15 ? snowflake.substring(snowflake.length() - 15) : snowflake;
        return time + tail;
    }
}
