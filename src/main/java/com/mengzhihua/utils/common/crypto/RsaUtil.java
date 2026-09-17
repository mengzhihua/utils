package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

/**
 * RSA-2048 encrypt / decrypt and SHA256withRSA sign / verify.
 */
public final class RsaUtil {

    private static final String RSA = "RSA";
    private static final String TRANSFORMATION = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String SIGNATURE = "SHA256withRSA";
    private static final int KEY_SIZE = 2048;

    private RsaUtil() {
    }

    public static KeyPairKeys generateKeys() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA);
            generator.initialize(KEY_SIZE);
            KeyPair keyPair = generator.generateKeyPair();
            return new KeyPairKeys(
                    Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()),
                    Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
            );
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("RSA key generation failed", ex);
        }
    }

    public static String encrypt(String plaintext, String publicKeyBase64) {
        if (plaintext == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey(publicKeyBase64));
            return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("RSA encrypt failed", ex);
        }
    }

    public static String decrypt(String cipherText, String privateKeyBase64) {
        if (cipherText == null) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey(privateKeyBase64));
            return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)), StandardCharsets.UTF_8);
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("RSA decrypt failed", ex);
        }
    }

    public static String sign(String plaintext, String privateKeyBase64) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE);
            signature.initSign(privateKey(privateKeyBase64));
            signature.update(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(signature.sign());
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("RSA sign failed", ex);
        }
    }

    public static boolean verify(String plaintext, String signBase64, String publicKeyBase64) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE);
            signature.initVerify(publicKey(publicKeyBase64));
            signature.update(plaintext.getBytes(StandardCharsets.UTF_8));
            return signature.verify(Base64.getDecoder().decode(signBase64));
        } catch (GeneralSecurityException | IllegalArgumentException ex) {
            return false;
        }
    }

    private static PublicKey publicKey(String base64) throws GeneralSecurityException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        return KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(bytes));
    }

    private static PrivateKey privateKey(String base64) throws GeneralSecurityException {
        byte[] bytes = Base64.getDecoder().decode(base64);
        return KeyFactory.getInstance(RSA).generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public record KeyPairKeys(String publicKey, String privateKey) {
    }
}
