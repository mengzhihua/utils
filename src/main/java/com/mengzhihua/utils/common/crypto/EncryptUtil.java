package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.mengzhihua.utils.common.lang.AssertUtil;

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

    public static String sm3(String text) {
        return Sm3Util.hash(text);
    }

    public static String sm4Encrypt(String plaintext, String password) {
        return Sm4Util.encrypt(plaintext, password);
    }

    public static String sm4Decrypt(String cipherText, String password) {
        return Sm4Util.decrypt(cipherText, password);
    }

    /**
     * ChaCha20-Poly1305 (RFC 8439). Output is Base64({@code nonce || ciphertext || tag}).
     */
    public static String chachaEncrypt(String plaintext, String password) {
        if (plaintext == null) {
            return null;
        }
        AssertUtil.notBlank(password, "ChaCha password must not be blank");
        try {
            byte[] nonce = new byte[12];
            RANDOM.nextBytes(nonce);
            Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305");
            cipher.init(Cipher.ENCRYPT_MODE, chachaKey(password), new IvParameterSpec(nonce));
            byte[] cipherBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] packed = new byte[nonce.length + cipherBytes.length];
            System.arraycopy(nonce, 0, packed, 0, nonce.length);
            System.arraycopy(cipherBytes, 0, packed, nonce.length, cipherBytes.length);
            return Base64.getEncoder().encodeToString(packed);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("ChaCha20-Poly1305 encrypt failed", ex);
        }
    }

    public static String chachaDecrypt(String cipherText, String password) {
        if (cipherText == null) {
            return null;
        }
        AssertUtil.notBlank(password, "ChaCha password must not be blank");
        try {
            byte[] packed = Base64.getDecoder().decode(cipherText);
            if (packed.length <= 12) {
                throw new IllegalArgumentException("invalid ChaCha cipher text");
            }
            byte[] nonce = new byte[12];
            byte[] cipherBytes = new byte[packed.length - 12];
            System.arraycopy(packed, 0, nonce, 0, 12);
            System.arraycopy(packed, 12, cipherBytes, 0, cipherBytes.length);
            Cipher cipher = Cipher.getInstance("ChaCha20-Poly1305");
            cipher.init(Cipher.DECRYPT_MODE, chachaKey(password), new IvParameterSpec(nonce));
            return new String(cipher.doFinal(cipherBytes), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException | IllegalArgumentException ex) {
            throw new IllegalStateException("ChaCha20-Poly1305 decrypt failed", ex);
        }
    }

    private static SecretKey chachaKey(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return new SecretKeySpec(digest.digest(password.getBytes(StandardCharsets.UTF_8)), "ChaCha20");
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("failed to derive ChaCha key", ex);
        }
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
