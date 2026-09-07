package com.mengzhihua.utils.util;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ID helpers: UUID, simple UUID, snowflake, and numeric strings.
 */
public final class IdUtil {

    private static volatile SnowflakeIdGenerator snowflake = new SnowflakeIdGenerator(1, 1);

    private IdUtil() {
    }

    public static void setSnowflake(SnowflakeIdGenerator generator) {
        snowflake = generator;
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    public static String simpleUuid() {
        return uuid().replace("-", "");
    }

    public static long snowflakeId() {
        return snowflake.nextId();
    }

    public static String snowflakeIdStr() {
        return Long.toString(snowflakeId());
    }

    public static String nanoId(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        final char[] alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz-".toCharArray();
        char[] chars = new char[length];
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            chars[i] = alphabet[random.nextInt(alphabet.length)];
        }
        return new String(chars);
    }
}
