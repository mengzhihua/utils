package com.mengzhihua.utils.common.lang;


import java.util.Arrays;

/**
 * Big-endian byte helpers (Hutool {@code ByteUtil} / Guava {@code Ints}/{@code Longs}).
 */
public final class ByteUtil {

    private ByteUtil() {
    }

    public static byte[] fromInt(int value) {
        return new byte[] {
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }

    public static int toInt(byte[] bytes) {
        if (bytes == null || bytes.length < 4) {
            throw new IllegalArgumentException("need 4 bytes");
        }
        return ((bytes[0] & 0xff) << 24)
                | ((bytes[1] & 0xff) << 16)
                | ((bytes[2] & 0xff) << 8)
                | (bytes[3] & 0xff);
    }

    public static byte[] fromLong(long value) {
        return new byte[] {
                (byte) (value >>> 56),
                (byte) (value >>> 48),
                (byte) (value >>> 40),
                (byte) (value >>> 32),
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value
        };
    }

    public static long toLong(byte[] bytes) {
        if (bytes == null || bytes.length < 8) {
            throw new IllegalArgumentException("need 8 bytes");
        }
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value = (value << 8) | (bytes[i] & 0xff);
        }
        return value;
    }

    public static byte[] xor(byte[] left, byte[] right) {
        if (left == null || right == null || left.length != right.length) {
            throw new IllegalArgumentException("xor length mismatch");
        }
        byte[] out = new byte[left.length];
        for (int i = 0; i < left.length; i++) {
            out[i] = (byte) (left[i] ^ right[i]);
        }
        return out;
    }

    public static byte[] concat(byte[]... arrays) {
        int total = 0;
        for (byte[] array : arrays) {
            if (array != null) {
                total += array.length;
            }
        }
        byte[] out = new byte[total];
        int pos = 0;
        for (byte[] array : arrays) {
            if (array == null) {
                continue;
            }
            System.arraycopy(array, 0, out, pos, array.length);
            pos += array.length;
        }
        return out;
    }

    public static byte[] reverse(byte[] data) {
        if (data == null) {
            return null;
        }
        byte[] out = Arrays.copyOf(data, data.length);
        for (int i = 0, j = out.length - 1; i < j; i++, j--) {
            byte tmp = out[i];
            out[i] = out[j];
            out[j] = tmp;
        }
        return out;
    }

    public static int indexOf(byte[] data, byte[] needle) {
        if (data == null || needle == null || needle.length == 0 || needle.length > data.length) {
            return -1;
        }
        outer:
        for (int i = 0; i <= data.length - needle.length; i++) {
            for (int j = 0; j < needle.length; j++) {
                if (data[i + j] != needle[j]) {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }
}
