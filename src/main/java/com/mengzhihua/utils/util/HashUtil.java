package com.mengzhihua.utils.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.GeneralSecurityException;
import java.util.HexFormat;
import java.util.Locale;

/**
 * Hash helpers for strings and files (MD5 / SHA-256 / Murmur3-32).
 */
public final class HashUtil {

    private static final HexFormat HEX = HexFormat.of();

    private HashUtil() {
    }

    public static String md5(Path path) {
        return digestFile("MD5", path);
    }

    public static String sha256(Path path) {
        return digestFile("SHA-256", path);
    }

    public static int murmur32(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return murmur32(data);
    }

    public static String murmur32Hex(String text) {
        return String.format(Locale.ROOT, "%08x", murmur32(text));
    }

    /**
     * MurmurHash3 x86_32, seed 0. Suitable for cache keys, not cryptography.
     */
    public static int murmur32(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int c1 = 0xcc9e2d51;
        int c2 = 0x1b873593;
        int h1 = 0;
        int roundedEnd = (bytes.length >> 2) << 2;
        for (int i = 0; i < roundedEnd; i += 4) {
            int k1 = (bytes[i] & 0xff)
                    | ((bytes[i + 1] & 0xff) << 8)
                    | ((bytes[i + 2] & 0xff) << 16)
                    | (bytes[i + 3] << 24);
            k1 *= c1;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= c2;
            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;
        }
        int k1 = 0;
        switch (bytes.length & 3) {
            case 3:
                k1 = (bytes[roundedEnd + 2] & 0xff) << 16;
                // fall through
            case 2:
                k1 |= (bytes[roundedEnd + 1] & 0xff) << 8;
                // fall through
            case 1:
                k1 |= bytes[roundedEnd] & 0xff;
                k1 *= c1;
                k1 = Integer.rotateLeft(k1, 15);
                k1 *= c2;
                h1 ^= k1;
            default:
                break;
        }
        h1 ^= bytes.length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;
        return h1;
    }

    private static String digestFile(String algorithm, Path path) {
        AssertUtil.notNull(path, "path must not be null");
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            try (InputStream in = Files.newInputStream(path);
                 DigestInputStream din = new DigestInputStream(in, digest)) {
                byte[] buffer = new byte[8192];
                while (din.read(buffer) != -1) {
                    // digest is updated by DigestInputStream
                }
            }
            return HEX.formatHex(digest.digest());
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(algorithm + " digest failed", ex);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
