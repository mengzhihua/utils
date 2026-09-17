package com.mengzhihua.utils.common.crypto;


import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.NamedParameterSpec;
import java.security.spec.XECPrivateKeySpec;
import java.security.spec.XECPublicKeySpec;
import java.util.HexFormat;
import javax.crypto.KeyAgreement;

/**
 * X25519 Diffie-Hellman (RFC 7748 / JDK). Alice×Bob shared secret starts with {@code 4a5d9d5b}.
 */
public final class X25519Util {

    private static final NamedParameterSpec X25519 = new NamedParameterSpec("X25519");

    private X25519Util() {
    }

    public static KeyPair generate() {
        try {
            return KeyPairGenerator.getInstance("X25519").generateKeyPair();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("X25519 not available", ex);
        }
    }

    public static byte[] sharedSecret(byte[] private32, byte[] public32) {
        try {
            KeyFactory factory = KeyFactory.getInstance("X25519");
            PrivateKey privateKey = factory.generatePrivate(new XECPrivateKeySpec(X25519, require32(private32)));
            PublicKey publicKey = factory.generatePublic(
                    new XECPublicKeySpec(X25519, new BigInteger(1, reverse(require32(public32)))));
            KeyAgreement agreement = KeyAgreement.getInstance("X25519");
            agreement.init(privateKey);
            agreement.doPhase(publicKey, true);
            return agreement.generateSecret();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("X25519 agreement failed", ex);
        }
    }

    public static String sharedSecretHex(String hexPrivate, String hexPublic) {
        return HexFormat.of().formatHex(sharedSecret(parse32(hexPrivate), parse32(hexPublic)));
    }

    private static byte[] require32(byte[] data) {
        if (data == null || data.length != 32) {
            throw new IllegalArgumentException("X25519 key must be 32 bytes");
        }
        return data;
    }

    private static byte[] parse32(String hex) {
        return require32(HexFormat.of().parseHex(hex.replace(" ", "")));
    }

    private static byte[] reverse(byte[] data) {
        byte[] out = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            out[i] = data[data.length - 1 - i];
        }
        return out;
    }
}
