package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HexFormat;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES-CMAC (RFC 4493 / NIST SP 800-38B). Empty message with
 * key {@code 2b7e151628aed2a6abf7158809cf4f3c} → {@code bb1d6929e95937287fa37d129b756746}.
 */
public final class CmacUtil {

    private static final byte[] RB = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, (byte) 0x87
    };

    private CmacUtil() {
    }

    public static String hex(String text, String hexKey) {
        byte[] key = HexFormat.of().parseHex(hexKey.replace(" ", ""));
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return HexFormat.of().formatHex(mac(key, data));
    }

    public static byte[] mac(byte[] key, byte[] message) {
        if (key == null || (key.length != 16 && key.length != 24 && key.length != 32)) {
            throw new IllegalArgumentException("AES-CMAC key must be 16/24/32 bytes");
        }
        byte[] msg = message == null ? new byte[0] : message;
        byte[] l = aes(key, new byte[16]);
        byte[] k1 = subkey(l);
        byte[] k2 = subkey(k1);
        int n = (msg.length + 15) / 16;
        boolean complete = msg.length != 0 && msg.length % 16 == 0;
        if (n == 0) {
            n = 1;
            complete = false;
        }
        byte[] last = new byte[16];
        int lastStart = (n - 1) * 16;
        if (complete) {
            System.arraycopy(msg, lastStart, last, 0, 16);
            xor(last, k1);
        } else {
            int remain = msg.length - lastStart;
            System.arraycopy(msg, lastStart, last, 0, remain);
            last[remain] = (byte) 0x80;
            xor(last, k2);
        }
        byte[] x = new byte[16];
        for (int i = 0; i < n - 1; i++) {
            byte[] block = Arrays.copyOfRange(msg, i * 16, i * 16 + 16);
            xor(block, x);
            x = aes(key, block);
        }
        xor(last, x);
        return aes(key, last);
    }

    private static byte[] subkey(byte[] in) {
        byte[] out = new byte[16];
        int overflow = 0;
        for (int i = 15; i >= 0; i--) {
            int v = (in[i] & 0xff) << 1 | overflow;
            out[i] = (byte) v;
            overflow = (v >>> 8) & 1;
        }
        if ((in[0] & 0x80) != 0) {
            xor(out, RB);
        }
        return out;
    }

    private static void xor(byte[] a, byte[] b) {
        for (int i = 0; i < 16; i++) {
            a[i] ^= b[i];
        }
    }

    private static byte[] aes(byte[] key, byte[] block) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
            return cipher.doFinal(block);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("AES-CMAC failed", ex);
        }
    }
}
