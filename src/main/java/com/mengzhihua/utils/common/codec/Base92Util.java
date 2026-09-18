package com.mengzhihua.utils.common.codec;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

/**
 * Base92 over printable ASCII except {@code "} and {@code \\}.
 * {@code Hello} → {@code Q2Aeq)}.
 */
public final class Base92Util {

    static final String ALPHABET;

    static {
        StringBuilder alphabet = new StringBuilder(92);
        for (int i = 33; i <= 126; i++) {
            if (i != '"' && i != '\\') {
                alphabet.append((char) i);
            }
        }
        ALPHABET = alphabet.toString();
    }

    private static final BigInteger BASE = BigInteger.valueOf(92);

    private Base92Util() {
    }

    public static String encode(String text) {
        return encode(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        if (bytes.length == 0) {
            return "";
        }
        int zeros = 0;
        while (zeros < bytes.length && bytes[zeros] == 0) {
            zeros++;
        }
        BigInteger value = new BigInteger(1, bytes);
        StringBuilder builder = new StringBuilder();
        while (value.signum() > 0) {
            BigInteger[] div = value.divideAndRemainder(BASE);
            builder.append(ALPHABET.charAt(div[1].intValue()));
            value = div[0];
        }
        builder.append(String.valueOf(ALPHABET.charAt(0)).repeat(zeros));
        return builder.reverse().toString();
    }

    public static byte[] decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new byte[0];
        }
        int zeros = 0;
        while (zeros < encoded.length() && encoded.charAt(zeros) == ALPHABET.charAt(0)) {
            zeros++;
        }
        BigInteger value = BigInteger.ZERO;
        for (int i = zeros; i < encoded.length(); i++) {
            int index = ALPHABET.indexOf(encoded.charAt(i));
            if (index < 0) {
                throw new IllegalArgumentException("invalid Base92 character");
            }
            value = value.multiply(BASE).add(BigInteger.valueOf(index));
        }
        byte[] raw = value.toByteArray();
        int start = raw.length > 0 && raw[0] == 0 ? 1 : 0;
        byte[] out = new byte[zeros + raw.length - start];
        System.arraycopy(raw, start, out, zeros, raw.length - start);
        return out;
    }

    public static String decodeToString(String encoded) {
        return new String(decode(encoded), StandardCharsets.UTF_8);
    }
}
