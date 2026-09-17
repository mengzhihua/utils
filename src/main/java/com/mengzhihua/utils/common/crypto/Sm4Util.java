package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;

import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * GM/T 0002-2012 SM4 (Hutool {@code SmUtil.sm4}, JDK only).
 * Official ECB vector: key/plain {@code 0123456789ABCDEFFEDCBA9876543210}
 * → {@code 681edf34d206965e86b3e94f536e4246}.
 */
public final class Sm4Util {

    private static final int[] SBOX = {
            0xD6, 0x90, 0xE9, 0xFE, 0xCC, 0xE1, 0x3D, 0xB7, 0x16, 0xB6, 0x14, 0xC2, 0x28, 0xFB, 0x2C, 0x05,
            0x2B, 0x67, 0x9A, 0x76, 0x2A, 0xBE, 0x04, 0xC3, 0xAA, 0x44, 0x13, 0x26, 0x49, 0x86, 0x06, 0x99,
            0x9C, 0x42, 0x50, 0xF4, 0x91, 0xEF, 0x98, 0x7A, 0x33, 0x54, 0x0B, 0x43, 0xED, 0xCF, 0xAC, 0x62,
            0xE4, 0xB3, 0x1C, 0xA9, 0xC9, 0x08, 0xE8, 0x95, 0x80, 0xDF, 0x94, 0xFA, 0x75, 0x8F, 0x3F, 0xA6,
            0x47, 0x07, 0xA7, 0xFC, 0xF3, 0x73, 0x17, 0xBA, 0x83, 0x59, 0x3C, 0x19, 0xE6, 0x85, 0x4F, 0xA8,
            0x68, 0x6B, 0x81, 0xB2, 0x71, 0x64, 0xDA, 0x8B, 0xF8, 0xEB, 0x0F, 0x4B, 0x70, 0x56, 0x9D, 0x35,
            0x1E, 0x24, 0x0E, 0x5E, 0x63, 0x58, 0xD1, 0xA2, 0x25, 0x22, 0x7C, 0x3B, 0x01, 0x21, 0x78, 0x87,
            0xD4, 0x00, 0x46, 0x57, 0x9F, 0xD3, 0x27, 0x52, 0x4C, 0x36, 0x02, 0xE7, 0xA0, 0xC4, 0xC8, 0x9E,
            0xEA, 0xBF, 0x8A, 0xD2, 0x40, 0xC7, 0x38, 0xB5, 0xA3, 0xF7, 0xF2, 0xCE, 0xF9, 0x61, 0x15, 0xA1,
            0xE0, 0xAE, 0x5D, 0xA4, 0x9B, 0x34, 0x1A, 0x55, 0xAD, 0x93, 0x32, 0x30, 0xF5, 0x8C, 0xB1, 0xE3,
            0x1D, 0xF6, 0xE2, 0x2E, 0x82, 0x66, 0xCA, 0x60, 0xC0, 0x29, 0x23, 0xAB, 0x0D, 0x53, 0x4E, 0x6F,
            0xD5, 0xDB, 0x37, 0x45, 0xDE, 0xFD, 0x8E, 0x2F, 0x03, 0xFF, 0x6A, 0x72, 0x6D, 0x6C, 0x5B, 0x51,
            0x8D, 0x1B, 0xAF, 0x92, 0xBB, 0xDD, 0xBC, 0x7F, 0x11, 0xD9, 0x5C, 0x41, 0x1F, 0x10, 0x5A, 0xD8,
            0x0A, 0xC1, 0x31, 0x88, 0xA5, 0xCD, 0x7B, 0xBD, 0x2D, 0x74, 0xD0, 0x12, 0xB8, 0xE5, 0xB4, 0xB0,
            0x89, 0x69, 0x97, 0x4A, 0x0C, 0x96, 0x77, 0x7E, 0x65, 0xB9, 0xF1, 0x09, 0xC5, 0x6E, 0xC6, 0x84,
            0x18, 0xF0, 0x7D, 0xEC, 0x3A, 0xDC, 0x4D, 0x20, 0x79, 0xEE, 0x5F, 0x3E, 0xD7, 0xCB, 0x39, 0x48
    };
    private static final int[] FK = {0xA3B1BAC6, 0x56AA3350, 0x677D9197, 0xB27022DC};
    private static final SecureRandom RANDOM = new SecureRandom();

    private Sm4Util() {
    }

    public static String encryptEcbHex(String hexKey, String hexPlain) {
        return HexFormat.of().formatHex(encryptEcb(parseHex16(hexKey), parseHex16(hexPlain)));
    }

    public static String decryptEcbHex(String hexKey, String hexCipher) {
        return HexFormat.of().formatHex(decryptEcb(parseHex16(hexKey), parseHex16(hexCipher)));
    }

    public static byte[] encryptEcb(byte[] key, byte[] block) {
        return cryptBlock(expand(key, true), block);
    }

    public static byte[] decryptEcb(byte[] key, byte[] block) {
        return cryptBlock(expand(key, false), block);
    }

    /**
     * SM4-CBC + PKCS7. Password is SHA-256 truncated to 16 bytes.
     * Output is Base64({@code iv || ciphertext}).
     */
    public static String encrypt(String plaintext, String password) {
        if (plaintext == null) {
            return null;
        }
        AssertUtil.notBlank(password, "SM4 password must not be blank");
        byte[] key = deriveKey(password);
        byte[] iv = new byte[16];
        RANDOM.nextBytes(iv);
        byte[] padded = pkcs7(plaintext.getBytes(StandardCharsets.UTF_8));
        byte[] out = new byte[iv.length + padded.length];
        System.arraycopy(iv, 0, out, 0, 16);
        byte[] prev = iv;
        for (int i = 0; i < padded.length; i += 16) {
            byte[] block = Arrays.copyOfRange(padded, i, i + 16);
            xorInPlace(block, prev);
            byte[] cipher = encryptEcb(key, block);
            System.arraycopy(cipher, 0, out, 16 + i, 16);
            prev = cipher;
        }
        return Base64.getEncoder().encodeToString(out);
    }

    public static String decrypt(String cipherText, String password) {
        if (cipherText == null) {
            return null;
        }
        AssertUtil.notBlank(password, "SM4 password must not be blank");
        byte[] packed = Base64.getDecoder().decode(cipherText);
        if (packed.length < 32 || (packed.length - 16) % 16 != 0) {
            throw new IllegalArgumentException("invalid SM4 cipher text");
        }
        byte[] key = deriveKey(password);
        byte[] prev = Arrays.copyOfRange(packed, 0, 16);
        byte[] plain = new byte[packed.length - 16];
        for (int i = 16; i < packed.length; i += 16) {
            byte[] block = Arrays.copyOfRange(packed, i, i + 16);
            byte[] dec = decryptEcb(key, block);
            xorInPlace(dec, prev);
            System.arraycopy(dec, 0, plain, i - 16, 16);
            prev = block;
        }
        return new String(unpad(plain), StandardCharsets.UTF_8);
    }

    private static byte[] cryptBlock(int[] rk, byte[] block) {
        if (block == null || block.length != 16) {
            throw new IllegalArgumentException("SM4 block must be 16 bytes");
        }
        int x0 = beInt(block, 0);
        int x1 = beInt(block, 4);
        int x2 = beInt(block, 8);
        int x3 = beInt(block, 12);
        for (int i = 0; i < 32; i++) {
            int tmp = x1 ^ x2 ^ x3 ^ rk[i];
            int x4 = x0 ^ l(tau(tmp));
            x0 = x1;
            x1 = x2;
            x2 = x3;
            x3 = x4;
        }
        byte[] out = new byte[16];
        putBe(out, 0, x3);
        putBe(out, 4, x2);
        putBe(out, 8, x1);
        putBe(out, 12, x0);
        return out;
    }

    private static int[] expand(byte[] key, boolean encrypt) {
        if (key == null || key.length != 16) {
            throw new IllegalArgumentException("SM4 key must be 16 bytes");
        }
        int[] mk = {beInt(key, 0), beInt(key, 4), beInt(key, 8), beInt(key, 12)};
        int k0 = mk[0] ^ FK[0];
        int k1 = mk[1] ^ FK[1];
        int k2 = mk[2] ^ FK[2];
        int k3 = mk[3] ^ FK[3];
        int[] rk = new int[32];
        for (int i = 0; i < 32; i++) {
            int ck = ck(i);
            int tmp = k1 ^ k2 ^ k3 ^ ck;
            int kn = k0 ^ lPrime(tau(tmp));
            rk[encrypt ? i : 31 - i] = kn;
            k0 = k1;
            k1 = k2;
            k2 = k3;
            k3 = kn;
        }
        return rk;
    }

    private static int ck(int i) {
        return (((4 * i) * 7) << 24)
                | ((((4 * i + 1) * 7) & 0xff) << 16)
                | ((((4 * i + 2) * 7) & 0xff) << 8)
                | (((4 * i + 3) * 7) & 0xff);
    }

    private static int tau(int x) {
        return (SBOX[(x >>> 24) & 0xff] << 24)
                | (SBOX[(x >>> 16) & 0xff] << 16)
                | (SBOX[(x >>> 8) & 0xff] << 8)
                | SBOX[x & 0xff];
    }

    private static int l(int b) {
        return b ^ Integer.rotateLeft(b, 2) ^ Integer.rotateLeft(b, 10)
                ^ Integer.rotateLeft(b, 18) ^ Integer.rotateLeft(b, 24);
    }

    private static int lPrime(int b) {
        return b ^ Integer.rotateLeft(b, 13) ^ Integer.rotateLeft(b, 23);
    }

    private static byte[] deriveKey(String password) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
            return Arrays.copyOf(digest, 16);
        } catch (Exception ex) {
            throw new IllegalStateException("SM4 key derive failed", ex);
        }
    }

    private static byte[] pkcs7(byte[] data) {
        int pad = 16 - (data.length % 16);
        byte[] out = Arrays.copyOf(data, data.length + pad);
        Arrays.fill(out, data.length, out.length, (byte) pad);
        return out;
    }

    private static byte[] unpad(byte[] data) {
        int pad = data[data.length - 1] & 0xff;
        if (pad < 1 || pad > 16 || pad > data.length) {
            throw new IllegalArgumentException("invalid PKCS7 padding");
        }
        for (int i = data.length - pad; i < data.length; i++) {
            if ((data[i] & 0xff) != pad) {
                throw new IllegalArgumentException("invalid PKCS7 padding");
            }
        }
        return Arrays.copyOf(data, data.length - pad);
    }

    private static void xorInPlace(byte[] block, byte[] prev) {
        for (int i = 0; i < 16; i++) {
            block[i] ^= prev[i];
        }
    }

    private static byte[] parseHex16(String hex) {
        String compact = hex == null ? "" : hex.replace(" ", "");
        byte[] bytes = HexFormat.of().parseHex(compact);
        if (bytes.length != 16) {
            throw new IllegalArgumentException("need 16 hex bytes");
        }
        return bytes;
    }

    private static int beInt(byte[] data, int offset) {
        return ((data[offset] & 0xff) << 24)
                | ((data[offset + 1] & 0xff) << 16)
                | ((data[offset + 2] & 0xff) << 8)
                | (data[offset + 3] & 0xff);
    }

    private static void putBe(byte[] out, int offset, int value) {
        out[offset] = (byte) (value >>> 24);
        out[offset + 1] = (byte) (value >>> 16);
        out[offset + 2] = (byte) (value >>> 8);
        out[offset + 3] = (byte) value;
    }
}
