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
import java.util.HexFormat;

/**
 * Ed25519 (RFC 8032 / JDK). TEST 1 empty message signature starts with {@code e5564300}.
 */
public final class Ed25519Util {

    private static final byte[] PKCS8_PREFIX = HexFormat.of().parseHex("302e020100300506032b657004220420");
    private static final byte[] X509_PREFIX = HexFormat.of().parseHex("302a300506032b6570032100");

    private Ed25519Util() {
    }

    public static KeyPair generate() {
        try {
            return KeyPairGenerator.getInstance("Ed25519").generateKeyPair();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Ed25519 not available", ex);
        }
    }

    public static byte[] sign(byte[] seed32, byte[] message) {
        try {
            PrivateKey key = KeyFactory.getInstance("Ed25519").generatePrivate(new PKCS8EncodedKeySpec(pkcs8(seed32)));
            Signature signature = Signature.getInstance("Ed25519");
            signature.initSign(key);
            signature.update(message == null ? new byte[0] : message);
            return signature.sign();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Ed25519 sign failed", ex);
        }
    }

    public static String signHex(String hexSeed, String text) {
        return HexFormat.of().formatHex(sign(parse32(hexSeed),
                text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8)));
    }

    public static boolean verify(byte[] public32, byte[] message, byte[] signature) {
        try {
            PublicKey key = KeyFactory.getInstance("Ed25519").generatePublic(new X509EncodedKeySpec(x509(public32)));
            Signature verifier = Signature.getInstance("Ed25519");
            verifier.initVerify(key);
            verifier.update(message == null ? new byte[0] : message);
            return verifier.verify(signature);
        } catch (GeneralSecurityException | IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean verifyHex(String hexPublic, String text, String hexSignature) {
        return verify(parse32(hexPublic),
                text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8),
                HexFormat.of().parseHex(hexSignature.replace(" ", "")));
    }

    private static byte[] pkcs8(byte[] seed32) {
        byte[] seed = require32(seed32);
        byte[] out = new byte[PKCS8_PREFIX.length + 32];
        System.arraycopy(PKCS8_PREFIX, 0, out, 0, PKCS8_PREFIX.length);
        System.arraycopy(seed, 0, out, PKCS8_PREFIX.length, 32);
        return out;
    }

    private static byte[] x509(byte[] public32) {
        byte[] pub = require32(public32);
        byte[] out = new byte[X509_PREFIX.length + 32];
        System.arraycopy(X509_PREFIX, 0, out, 0, X509_PREFIX.length);
        System.arraycopy(pub, 0, out, X509_PREFIX.length, 32);
        return out;
    }

    private static byte[] require32(byte[] data) {
        if (data == null || data.length != 32) {
            throw new IllegalArgumentException("Ed25519 key must be 32 bytes");
        }
        return data;
    }

    private static byte[] parse32(String hex) {
        byte[] bytes = HexFormat.of().parseHex(hex.replace(" ", ""));
        return require32(bytes);
    }
}
