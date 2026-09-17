package com.mengzhihua.utils.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Locale;

/**
 * RFC 4226 HOTP (HMAC-SHA1), the counter-based sibling of TOTP.
 */
public final class HotpUtil {

    private HotpUtil() {
    }

    public static String generate(byte[] key, long counter) {
        return generate(key, counter, 6);
    }

    public static String generate(byte[] key, long counter, int digits) {
        if (key == null || key.length == 0) {
            throw new IllegalArgumentException("HOTP key is empty");
        }
        if (digits < 4 || digits > 10) {
            throw new IllegalArgumentException("digits must be 4-10");
        }
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
            throw new IllegalStateException("HOTP failed", ex);
        }
    }

    public static String generateFromAscii(String asciiKey, long counter) {
        return generate(asciiKey.getBytes(StandardCharsets.US_ASCII), counter);
    }

    public static boolean verify(String code, byte[] key, long counter) {
        return code != null && code.equals(generate(key, counter));
    }

    private static int pow10(int digits) {
        int n = 1;
        for (int i = 0; i < digits; i++) {
            n *= 10;
        }
        return n;
    }
}
