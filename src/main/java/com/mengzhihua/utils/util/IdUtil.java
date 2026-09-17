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

    public static String ulid() {
        long time = System.currentTimeMillis();
        byte[] entropy = new byte[10];
        ThreadLocalRandom.current().nextBytes(entropy);
        char[] alphabet = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray();
        char[] out = new char[26];
        writeCrockford(out, time, 10, 0, alphabet);
        long high = ((entropy[0] & 0xffL) << 32)
                | ((entropy[1] & 0xffL) << 24)
                | ((entropy[2] & 0xffL) << 16)
                | ((entropy[3] & 0xffL) << 8)
                | (entropy[4] & 0xffL);
        long low = ((entropy[5] & 0xffL) << 32)
                | ((entropy[6] & 0xffL) << 24)
                | ((entropy[7] & 0xffL) << 16)
                | ((entropy[8] & 0xffL) << 8)
                | (entropy[9] & 0xffL);
        writeCrockford(out, high, 8, 10, alphabet);
        writeCrockford(out, low, 8, 18, alphabet);
        return new String(out);
    }

    private static void writeCrockford(char[] out, long value, int count, int offset, char[] alphabet) {
        for (int i = count - 1; i >= 0; i--) {
            out[offset + i] = alphabet[(int) (value & 31)];
            value >>>= 5;
        }
    }
}
