package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;

/**
 * yEnc (Usenet). Each byte {@code + 42} mod 256; escape NUL / LF / CR / {@code =}
 * with {@code =} then {@code + 64}. {@code Hello} → {@code 728f969699}.
 */
public final class YencUtil {

    private YencUtil() {
    }

    public static String encode(String text) {
        return new String(encode((text == null ? "" : text).getBytes(StandardCharsets.UTF_8)), StandardCharsets.ISO_8859_1);
    }

    public static byte[] encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        byte[] out = new byte[bytes.length * 2];
        int pos = 0;
        for (byte b : bytes) {
            int value = (b + 42) & 0xff;
            if (value == 0 || value == 10 || value == 13 || value == 61) {
                out[pos++] = '=';
                value = (value + 64) & 0xff;
            }
            out[pos++] = (byte) value;
        }
        byte[] exact = new byte[pos];
        System.arraycopy(out, 0, exact, 0, pos);
        return exact;
    }

    public static String encodeToHex(String text) {
        return HexUtil.encode(encode((text == null ? "" : text).getBytes(StandardCharsets.UTF_8)));
    }

    public static String decodeToString(String encoded) {
        return new String(decodeLatin1(encoded), StandardCharsets.UTF_8);
    }

    public static byte[] decode(byte[] encoded) {
        byte[] bytes = encoded == null ? new byte[0] : encoded;
        byte[] out = new byte[bytes.length];
        int pos = 0;
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i] & 0xff;
            if (value == 61 && i + 1 < bytes.length) {
                value = (bytes[++i] - 64) & 0xff;
            }
            out[pos++] = (byte) ((value - 42) & 0xff);
        }
        byte[] exact = new byte[pos];
        System.arraycopy(out, 0, exact, 0, pos);
        return exact;
    }

    public static byte[] decodeLatin1(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new byte[0];
        }
        return decode(encoded.getBytes(StandardCharsets.ISO_8859_1));
    }

    public static byte[] decodeHex(String hex) {
        return decode(HexUtil.decode(hex));
    }
}
