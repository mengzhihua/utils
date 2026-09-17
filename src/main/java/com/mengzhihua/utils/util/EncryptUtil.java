package com.mengzhihua.utils.util;

import javax.crypto.Mac;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

/**
 * Digest and symmetric encryption helpers.
 * <p>
 * MD5 is provided for checksums only — do not use it for password storage.
 */
public final class EncryptUtil {

    private static final String AES_GCM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_BITS = 128;
    private static final HexFormat HEX = HexFormat.of();
    private static final SecureRandom RANDOM = new SecureRandom();

    private EncryptUtil() {
    }

    public static String md5(String text) {
        return digest("MD5", text);
    }

    public static String sha256(String text) {
        return digest("SHA-256", text);
    }

    public static String sha512(String text) {
        return digest("SHA-512", text);
    }

    public static String sha3_256(String text) {
        return digest("SHA3-256", text);
    }

    public static String hmacSha256(String text, String secret) {
        if (text == null || secret == null) {
            return null;
        }
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HEX.formatHex(mac.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("HMAC-SHA256 failed", ex);
        }
    }

    public static String encodeBase64(String text) {
        if (text == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String decodeBase64(String base64) {
        if (base64 == null) {
            return null;
        }
        return new String(Base64.getDecoder().decode(base64), StandardCharsets.UTF_8);
    }

    /**
     * Encrypts with AES-256-GCM. The password is stretched to 32 bytes via SHA-256.
     * Output is Base64({@code iv || ciphertext || tag}).
     */
    public static String aesEncrypt(String plaintext, String password) {
        if (plaintext == null) {
            return null;
        }
        AssertUtil.notBlank(password, "AES password must not be blank");
        try {
            byte[] iv = new byte[GCM_IV_LENGTH];
            RANDOM.nextBytes(iv);
            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey(password), new GCMParameterSpec(GCM_TAG_BITS, iv));
            byte[] cipherBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] packed = new byte[iv.length + cipherBytes.length];
            System.arraycopy(iv, 0, packed, 0, iv.length);
            System.arraycopy(cipherBytes, 0, packed, iv.length, cipherBytes.length);
            return Base64.getEncoder().encodeToString(packed);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("AES encrypt failed", ex);
        }
    }

    public static String aesDecrypt(String cipherText, String password) {
        if (cipherText == null) {
            return null;
        }
        AssertUtil.notBlank(password, "AES password must not be blank");
        try {
            byte[] packed = Base64.getDecoder().decode(cipherText);
            if (packed.length <= GCM_IV_LENGTH) {
                throw new IllegalArgumentException("invalid AES cipher text");
            }
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] cipherBytes = new byte[packed.length - GCM_IV_LENGTH];
            System.arraycopy(packed, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(packed, GCM_IV_LENGTH, cipherBytes, 0, cipherBytes.length);
            Cipher cipher = Cipher.getInstance(AES_GCM);
            cipher.init(Cipher.DECRYPT_MODE, aesKey(password), new GCMParameterSpec(GCM_TAG_BITS, iv));
            return new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException ex) {
            throw new IllegalStateException("AES decrypt failed", ex);
        }
    }

    private static SecretKey aesKey(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new SecretKeySpec(digest.digest(password.getBytes(StandardCharsets.UTF_8)), "AES");
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("failed to derive AES key", ex);
        }
    }

    private static String digest(String algorithm, String text) {
        if (text == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
            return HEX.formatHex(messageDigest.digest(text.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(algorithm + " digest failed", ex);
        }
    }

    public static String crc32(String text) {
        if (text == null) {
            return null;
        }
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        crc.update(text.getBytes(StandardCharsets.UTF_8));
        return String.format(java.util.Locale.ROOT, "%08x", crc.getValue());
    }
}
