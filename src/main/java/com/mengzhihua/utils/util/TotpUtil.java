package com.mengzhihua.utils.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.Locale;

/**
 * RFC 6238 TOTP（默认 HMAC-SHA1、30 秒、6 位），兼容常见 Authenticator。
 */
public final class TotpUtil {

    private static final String BASE32 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    private TotpUtil() {
    }

    public static String generate(String base32Secret) {
        return generate(base32Secret, System.currentTimeMillis() / 1000L, 30, 6);
    }

    public static String generate(String base32Secret, long unixSeconds, int periodSeconds, int digits) {
        byte[] key = decodeBase32(base32Secret);
        long counter = unixSeconds / periodSeconds;
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(new SecretKeySpec(key, "HmacSHA1"));
            byte[] hash = mac.doFinal(ByteBuffer.allocate(8).putLong(counter).array());
            int offset = hash[hash.length - 1] & 0x0f;
            int binary = ((hash[offset] & 0x7f) << 24)
                    | ((hash[offset + 1] & 0xff) << 16)
                    | ((hash[offset + 2] & 0xff) << 8)
                    | (hash[offset + 3] & 0xff);
            int otp = binary % pow10(digits);
            return String.format(Locale.ROOT, "%0" + digits + "d", otp);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("TOTP failed", ex);
        }
    }

    public static boolean verify(String code, String base32Secret) {
        return verify(code, base32Secret, 1);
    }

    public static boolean verify(String code, String base32Secret, int window) {
        if (StringUtil.isBlank(code) || StringUtil.isBlank(base32Secret)) {
            return false;
        }
        long now = System.currentTimeMillis() / 1000L;
        for (int i = -window; i <= window; i++) {
            if (code.equals(generate(base32Secret, now + (long) i * 30, 30, 6))) {
                return true;
            }
        }
        return false;
    }

    static byte[] decodeBase32(String secret) {
        String text = secret == null ? "" : secret.replace(" ", "").replace("=", "").toUpperCase(Locale.ROOT);
        int buffer = 0;
        int bits = 0;
        byte[] out = new byte[text.length() * 5 / 8];
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            int val = BASE32.indexOf(text.charAt(i));
            if (val < 0) {
                throw new IllegalArgumentException("invalid base32 secret");
            }
            buffer = (buffer << 5) | val;
            bits += 5;
            if (bits >= 8) {
                bits -= 8;
                out[index++] = (byte) ((buffer >> bits) & 0xff);
            }
        }
        if (index == out.length) {
            return out;
        }
        byte[] trimmed = new byte[index];
        System.arraycopy(out, 0, trimmed, 0, index);
        return trimmed;
    }

    private static int pow10(int digits) {
        int n = 1;
        for (int i = 0; i < digits; i++) {
            n *= 10;
        }
        return n;
    }
}
