package com.mengzhihua.utils.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Bitcoin-style Base58 (no check suffix).
 */
public final class Base58Util {

    private static final char[] ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz".toCharArray();
    private static final int[] INDEX = new int[128];

    static {
        Arrays.fill(INDEX, -1);
        for (int i = 0; i < ALPHABET.length; i++) {
            INDEX[ALPHABET[i]] = i;
        }
    }

    private Base58Util() {
    }

    public static String encode(String text) {
        return text == null ? null : encode(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] input) {
        if (input == null) {
            return null;
        }
        if (input.length == 0) {
            return "";
        }
        int zeros = 0;
        while (zeros < input.length && input[zeros] == 0) {
            zeros++;
        }
        byte[] copy = Arrays.copyOf(input, input.length);
        char[] encoded = new char[copy.length * 2];
        int outputStart = encoded.length;
        for (int inputStart = zeros; inputStart < copy.length; ) {
            encoded[--outputStart] = ALPHABET[divmod(copy, inputStart, 256, 58)];
            if (copy[inputStart] == 0) {
                inputStart++;
            }
        }
        while (outputStart < encoded.length && encoded[outputStart] == ALPHABET[0]) {
            outputStart++;
        }
        while (--zeros >= 0) {
            encoded[--outputStart] = ALPHABET[0];
        }
        return new String(encoded, outputStart, encoded.length - outputStart);
    }

    public static byte[] decode(String text) {
        if (text == null) {
            return null;
        }
        if (text.isEmpty()) {
            return new byte[0];
        }
        byte[] input58 = new byte[text.length()];
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int digit = c < 128 ? INDEX[c] : -1;
            if (digit < 0) {
                throw new IllegalArgumentException("invalid Base58 char: " + c);
            }
            input58[i] = (byte) digit;
        }
        int zeros = 0;
        while (zeros < input58.length && input58[zeros] == 0) {
            zeros++;
        }
        byte[] decoded = new byte[text.length()];
        int outputStart = decoded.length;
        for (int inputStart = zeros; inputStart < input58.length; ) {
            decoded[--outputStart] = divmod(input58, inputStart, 58, 256);
            if (input58[inputStart] == 0) {
                inputStart++;
            }
        }
        while (outputStart < decoded.length && decoded[outputStart] == 0) {
            outputStart++;
        }
        return Arrays.copyOfRange(decoded, outputStart - zeros, decoded.length);
    }

    public static String decodeToString(String text) {
        byte[] bytes = decode(text);
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }

    private static byte divmod(byte[] number, int firstDigit, int base, int divisor) {
        int remainder = 0;
        for (int i = firstDigit; i < number.length; i++) {
            int digit = (number[i] & 0xff) + remainder * base;
            number[i] = (byte) (digit / divisor);
            remainder = digit % divisor;
        }
        return (byte) remainder;
    }
}
