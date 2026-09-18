package com.mengzhihua.utils.common.codec;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Base36 (Hutool / JDK radix 36). {@code 1234567890} → {@code kf12oi},
 * UTF-8 {@code hello} → {@code 5pzcszu7}.
 */
public final class Base36Util {

    private Base36Util() {
    }

    public static String encode(long value) {
        return Long.toString(value, 36);
    }

    public static long decodeLong(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            throw new IllegalArgumentException("Base36 text is blank");
        }
        return Long.parseLong(encoded.trim(), 36);
    }

    public static String encode(String text) {
        return encode((text == null ? "" : text).getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        if (bytes.length == 0) {
            return "0";
        }
        return new BigInteger(1, bytes).toString(36);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return new byte[0];
        }
        byte[] bytes = new BigInteger(encoded.trim().toLowerCase(Locale.ROOT), 36).toByteArray();
        if (bytes.length > 1 && bytes[0] == 0) {
            byte[] withoutSign = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, withoutSign, 0, withoutSign.length);
            return withoutSign;
        }
        return bytes;
    }

    public static String decodeToString(String encoded) {
        return new String(decode(encoded), StandardCharsets.UTF_8);
    }
}
