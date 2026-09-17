package com.mengzhihua.utils.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * RFC 5869 HKDF-SHA256.
 */
public final class HkdfUtil {

    private HkdfUtil() {
    }

    public static byte[] derive(byte[] ikm, byte[] salt, byte[] info, int length) {
        if (ikm == null || ikm.length == 0) {
            throw new IllegalArgumentException("ikm is empty");
        }
        if (length <= 0 || length > 255 * 32) {
            throw new IllegalArgumentException("invalid output length");
        }
        byte[] prk = extract(salt == null ? new byte[0] : salt, ikm);
        return expand(prk, info == null ? new byte[0] : info, length);
    }

    public static String deriveHex(String ikm, String salt, String info, int length) {
        return HexUtil.encode(derive(
                bytes(ikm),
                salt == null ? new byte[0] : bytes(salt),
                info == null ? new byte[0] : bytes(info),
                length
        ));
    }

    private static byte[] extract(byte[] salt, byte[] ikm) {
        byte[] key = salt.length == 0 ? new byte[32] : salt;
        return hmac(key, ikm);
    }

    private static byte[] expand(byte[] prk, byte[] info, int length) {
        int hashLen = 32;
        int n = (length + hashLen - 1) / hashLen;
        byte[] okm = new byte[length];
        byte[] previous = new byte[0];
        int copied = 0;
        for (int i = 1; i <= n; i++) {
            byte[] input = new byte[previous.length + info.length + 1];
            System.arraycopy(previous, 0, input, 0, previous.length);
            System.arraycopy(info, 0, input, previous.length, info.length);
            input[input.length - 1] = (byte) i;
            previous = hmac(prk, input);
            int take = Math.min(hashLen, length - copied);
            System.arraycopy(previous, 0, okm, copied, take);
            copied += take;
        }
        return okm;
    }

    private static byte[] hmac(byte[] key, byte[] data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(data);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("HKDF failed", ex);
        }
    }

    private static byte[] bytes(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] deriveAndCopy(byte[] ikm, byte[] salt, byte[] info, int length) {
        byte[] derived = derive(ikm, salt, info, length);
        return Arrays.copyOf(derived, derived.length);
    }
}
