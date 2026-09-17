package com.mengzhihua.utils.common.id;


import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    public static final UUID NAMESPACE_DNS = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    public static final UUID NAMESPACE_URL = UUID.fromString("6ba7b811-9dad-11d1-80b4-00c04fd430c8");

    public static String nanoId() {
        return nanoId(21);
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

    public static SnowflakeIdGenerator.Parts parseSnowflake(long id) {
        return SnowflakeIdGenerator.parse(id);
    }

    /**
     * RFC 9562 UUID version 7 (time-ordered).
     */
    public static String uuidV7() {
        byte[] bytes = new byte[16];
        ThreadLocalRandom.current().nextBytes(bytes);
        long time = System.currentTimeMillis();
        bytes[0] = (byte) (time >>> 40);
        bytes[1] = (byte) (time >>> 32);
        bytes[2] = (byte) (time >>> 24);
        bytes[3] = (byte) (time >>> 16);
        bytes[4] = (byte) (time >>> 8);
        bytes[5] = (byte) time;
        bytes[6] = (byte) ((bytes[6] & 0x0f) | 0x70);
        bytes[8] = (byte) ((bytes[8] & 0x3f) | 0x80);
        UUID uuid = new UUID(
                ((bytes[0] & 0xffL) << 56) | ((bytes[1] & 0xffL) << 48) | ((bytes[2] & 0xffL) << 40)
                        | ((bytes[3] & 0xffL) << 32) | ((bytes[4] & 0xffL) << 24) | ((bytes[5] & 0xffL) << 16)
                        | ((bytes[6] & 0xffL) << 8) | (bytes[7] & 0xffL),
                ((bytes[8] & 0xffL) << 56) | ((bytes[9] & 0xffL) << 48) | ((bytes[10] & 0xffL) << 40)
                        | ((bytes[11] & 0xffL) << 32) | ((bytes[12] & 0xffL) << 24) | ((bytes[13] & 0xffL) << 16)
                        | ((bytes[14] & 0xffL) << 8) | (bytes[15] & 0xffL)
        );
        return uuid.toString();
    }

    /**
     * RFC 9562 UUID version 6 (time-ordered Gregorian).
     */
    public static String uuidV6() {
        long unixMs = System.currentTimeMillis();
        long uuidTime = unixMs * 10_000L + 0x01B21DD213814000L;
        long timeHigh = (uuidTime >>> 28) & 0xffffffffL;
        long timeMid = (uuidTime >>> 12) & 0xffffL;
        long timeLow = uuidTime & 0xfffL;
        byte[] bytes = new byte[16];
        ThreadLocalRandom.current().nextBytes(bytes);
        bytes[0] = (byte) (timeHigh >>> 24);
        bytes[1] = (byte) (timeHigh >>> 16);
        bytes[2] = (byte) (timeHigh >>> 8);
        bytes[3] = (byte) timeHigh;
        bytes[4] = (byte) (timeMid >>> 8);
        bytes[5] = (byte) timeMid;
        bytes[6] = (byte) ((timeLow >>> 8) & 0x0f | 0x60);
        bytes[7] = (byte) timeLow;
        bytes[8] = (byte) ((bytes[8] & 0x3f) | 0x80);
        UUID uuid = new UUID(
                ((bytes[0] & 0xffL) << 56) | ((bytes[1] & 0xffL) << 48) | ((bytes[2] & 0xffL) << 40)
                        | ((bytes[3] & 0xffL) << 32) | ((bytes[4] & 0xffL) << 24) | ((bytes[5] & 0xffL) << 16)
                        | ((bytes[6] & 0xffL) << 8) | (bytes[7] & 0xffL),
                ((bytes[8] & 0xffL) << 56) | ((bytes[9] & 0xffL) << 48) | ((bytes[10] & 0xffL) << 40)
                        | ((bytes[11] & 0xffL) << 32) | ((bytes[12] & 0xffL) << 24) | ((bytes[13] & 0xffL) << 16)
                        | ((bytes[14] & 0xffL) << 8) | (bytes[15] & 0xffL)
        );
        return uuid.toString();
    }

    public static String objectId() {
        return ObjectIdUtil.next();
    }

    public static String ksuid() {
        return KsuidUtil.next();
    }

    public static String typeId(String prefix) {
        return TypeIdUtil.next(prefix);
    }

    public static String sqid(long id) {
        return SqidsUtil.encode(id);
    }

    public static String cuid2() {
        return Cuid2Util.next();
    }

    public static long tsid() {
        return TsidUtil.next();
    }

    public static String tsidStr() {
        return TsidUtil.nextStr();
    }

    /**
     * RFC 4122 UUID version 3 (MD5 name-based).
     */
    public static String uuidV3(String name) {
        return uuidV3(NAMESPACE_DNS, name);
    }

    public static String uuidV3(UUID namespace, String name) {
        return nameBasedUuid(namespace, name, "MD5", 3);
    }

    /**
     * RFC 4122 UUID version 5 (SHA-1 name-based).
     */
    public static String uuidV5(String name) {
        return uuidV5(NAMESPACE_DNS, name);
    }

    public static String uuidV5(UUID namespace, String name) {
        return nameBasedUuid(namespace, name, "SHA-1", 5);
    }

    private static String nameBasedUuid(UUID namespace, String name, String algorithm, int version) {
        if (namespace == null || name == null) {
            throw new IllegalArgumentException("namespace and name are required");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            ByteBuffer buffer = ByteBuffer.allocate(16);
            buffer.putLong(namespace.getMostSignificantBits());
            buffer.putLong(namespace.getLeastSignificantBits());
            digest.update(buffer.array());
            digest.update(name.getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest();
            hash[6] = (byte) ((hash[6] & 0x0f) | (version << 4));
            hash[8] = (byte) ((hash[8] & 0x3f) | 0x80);
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                msb = (msb << 8) | (hash[i] & 0xffL);
            }
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (hash[i] & 0xffL);
            }
            return new UUID(msb, lsb).toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(algorithm + " not available", ex);
        }
    }

    private static void writeCrockford(char[] out, long value, int count, int offset, char[] alphabet) {
        for (int i = count - 1; i >= 0; i--) {
            out[offset + i] = alphabet[(int) (value & 31)];
            value >>>= 5;
        }
    }
}
