package com.mengzhihua.utils.common.crypto;


import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.mengzhihua.utils.common.codec.HexUtil;
import com.mengzhihua.utils.common.lang.AssertUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * PBKDF2-HMAC-SHA256 password hashing. Format: {@code pbkdf2$iterations$salt$hash}.
 */
public final class Pbkdf2Util {

    private static final int ITERATIONS = 120_000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private Pbkdf2Util() {
    }

    public static String hash(String password) {
        AssertUtil.notBlank(password, "password must not be blank");
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        byte[] dk = derive(password, salt, ITERATIONS);
        return "pbkdf2$" + ITERATIONS + "$" + HexUtil.encode(salt) + "$" + HexUtil.encode(dk);
    }

    public static boolean matches(String password, String stored) {
        if (StringUtil.isBlank(password) || StringUtil.isBlank(stored)) {
            return false;
        }
        String[] parts = stored.split("\\$");
        if (parts.length != 4 || !"pbkdf2".equals(parts[0])) {
            return false;
        }
        int iterations = Integer.parseInt(parts[1]);
        byte[] salt = HexUtil.decode(parts[2]);
        byte[] expected = HexUtil.decode(parts[3]);
        byte[] actual = derive(password, salt, iterations);
        return constantTimeEquals(expected, actual);
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("pbkdf2 failed", ex);
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}
