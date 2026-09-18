package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * ZeroMQ Z85 (RFC 32). {@code 864FD26FB559F75B} → {@code HelloWorld}.
 */
public final class Z85Util {

    private static final String ALPHABET =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.-:+=^!/*?&<>()[]{}@%$#";
    private static final int[] DECODE = new int[128];

    static {
        Arrays.fill(DECODE, -1);
        for (int i = 0; i < ALPHABET.length(); i++) {
            DECODE[ALPHABET.charAt(i)] = i;
        }
    }

    private Z85Util() {
    }

    public static String encode(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        int pad = (4 - (data.length % 4)) % 4;
        if (pad > 0) {
            data = Arrays.copyOf(data, data.length + pad);
        }
        return encode(data);
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        if (bytes.length % 4 != 0) {
            throw new IllegalArgumentException("Z85 binary length must be a multiple of 4");
        }
        StringBuilder out = new StringBuilder(bytes.length / 4 * 5);
        for (int i = 0; i < bytes.length; i += 4) {
            long value = ((bytes[i] & 0xffL) << 24)
                    | ((bytes[i + 1] & 0xffL) << 16)
                    | ((bytes[i + 2] & 0xffL) << 8)
                    | (bytes[i + 3] & 0xffL);
            char[] block = new char[5];
            for (int j = 4; j >= 0; j--) {
                block[j] = ALPHABET.charAt((int) (value % 85));
                value /= 85;
            }
            out.append(block);
        }
        return out.toString();
    }

    public static byte[] decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new byte[0];
        }
        if (encoded.length() % 5 != 0) {
            throw new IllegalArgumentException("Z85 text length must be a multiple of 5");
        }
        byte[] out = new byte[encoded.length() / 5 * 4];
        int pos = 0;
        for (int i = 0; i < encoded.length(); i += 5) {
            long value = 0;
            for (int j = 0; j < 5; j++) {
                char c = encoded.charAt(i + j);
                int digit = c < 128 ? DECODE[c] : -1;
                if (digit < 0) {
                    throw new IllegalArgumentException("invalid Z85 character");
                }
                value = value * 85 + digit;
            }
            out[pos++] = (byte) ((value >> 24) & 0xff);
            out[pos++] = (byte) ((value >> 16) & 0xff);
            out[pos++] = (byte) ((value >> 8) & 0xff);
            out[pos++] = (byte) (value & 0xff);
        }
        return out;
    }

    public static String decodeToHex(String encoded) {
        return HexUtil.encode(decode(encoded));
    }
}
