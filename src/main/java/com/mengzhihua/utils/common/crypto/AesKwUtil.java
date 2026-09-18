package com.mengzhihua.utils.common.crypto;


import java.security.GeneralSecurityException;
import java.util.HexFormat;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES Key Wrap (RFC 3394). 128-bit KEK vector:
 * key {@code 00112233445566778899AABBCCDDEEFF} wraps to
 * {@code 1FA68B0A8112B447AEF34BD8FB5A7B829D3E862371D2CFE5}.
 */
public final class AesKwUtil {

    private static final long IV = 0xA6A6A6A6A6A6A6A6L;

    private AesKwUtil() {
    }

    public static String wrapHex(String hexKek, String hexKey) {
        return HexFormat.of().formatHex(wrap(parse(hexKek), parse(hexKey))).toUpperCase();
    }

    public static String unwrapHex(String hexKek, String hexWrapped) {
        return HexFormat.of().formatHex(unwrap(parse(hexKek), parse(hexWrapped))).toUpperCase();
    }

    public static byte[] wrap(byte[] kek, byte[] key) {
        if (kek == null || (kek.length != 16 && kek.length != 24 && kek.length != 32)) {
            throw new IllegalArgumentException("KEK must be 16/24/32 bytes");
        }
        if (key == null || key.length < 16 || key.length % 8 != 0) {
            throw new IllegalArgumentException("key must be a multiple of 8 bytes and at least 16");
        }
        int n = key.length / 8;
        long a = IV;
        long[] r = new long[n];
        for (int i = 0; i < n; i++) {
            r[i] = beLong(key, i * 8);
        }
        for (int j = 0; j <= 5; j++) {
            for (int i = 0; i < n; i++) {
                byte[] block = new byte[16];
                putBe(block, 0, a);
                putBe(block, 8, r[i]);
                byte[] b = aes(kek, block, true);
                a = beLong(b, 0) ^ (n * j + i + 1L);
                r[i] = beLong(b, 8);
            }
        }
        byte[] out = new byte[8 + key.length];
        putBe(out, 0, a);
        for (int i = 0; i < n; i++) {
            putBe(out, 8 + i * 8, r[i]);
        }
        return out;
    }

    public static byte[] unwrap(byte[] kek, byte[] wrapped) {
        if (wrapped == null || wrapped.length < 24 || wrapped.length % 8 != 0) {
            throw new IllegalArgumentException("invalid wrapped key");
        }
        int n = wrapped.length / 8 - 1;
        long a = beLong(wrapped, 0);
        long[] r = new long[n];
        for (int i = 0; i < n; i++) {
            r[i] = beLong(wrapped, 8 + i * 8);
        }
        for (int j = 5; j >= 0; j--) {
            for (int i = n - 1; i >= 0; i--) {
                byte[] block = new byte[16];
                putBe(block, 0, a ^ (n * j + i + 1L));
                putBe(block, 8, r[i]);
                byte[] b = aes(kek, block, false);
                a = beLong(b, 0);
                r[i] = beLong(b, 8);
            }
        }
        if (a != IV) {
            throw new IllegalArgumentException("AES-KW integrity check failed");
        }
        byte[] key = new byte[n * 8];
        for (int i = 0; i < n; i++) {
            putBe(key, i * 8, r[i]);
        }
        return key;
    }

    private static byte[] aes(byte[] kek, byte[] block, boolean encrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, new SecretKeySpec(kek, "AES"));
            return cipher.doFinal(block);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("AES-KW failed", ex);
        }
    }

    private static byte[] parse(String hex) {
        return HexFormat.of().parseHex(hex.replace(" ", ""));
    }

    private static long beLong(byte[] data, int offset) {
        long v = 0;
        for (int i = 0; i < 8; i++) {
            v = (v << 8) | (data[offset + i] & 0xffL);
        }
        return v;
    }

    private static void putBe(byte[] out, int offset, long value) {
        for (int i = 7; i >= 0; i--) {
            out[offset + i] = (byte) value;
            value >>>= 8;
        }
    }
}
