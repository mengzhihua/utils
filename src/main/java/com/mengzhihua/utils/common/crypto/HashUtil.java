package com.mengzhihua.utils.common.crypto;


import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * Hash helpers for strings and files (MD5 / SHA-256 / Murmur3 / CRC).
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

    /**
     * MurmurHash3 x64_128, seed 0 (Guava {@code Hashing.murmur3_128}).
     */
    public static String murmur128Hex(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return HEX.formatHex(murmur128(data));
    }

    public static byte[] murmur128(byte[] data) {
        return murmur128(data, 0);
    }

    @SuppressWarnings("fallthrough")
    public static byte[] murmur128(byte[] data, int seed) {
        byte[] bytes = data == null ? new byte[0] : data;
        long c1 = 0x87c37b91114253d5L;
        long c2 = 0x4cf5ad432745937fL;
        long h1 = seed & 0xffffffffL;
        long h2 = seed & 0xffffffffL;
        int roundedEnd = (bytes.length / 16) * 16;
        for (int i = 0; i < roundedEnd; i += 16) {
            long k1 = leLong(bytes, i);
            long k2 = leLong(bytes, i + 8);
            k1 *= c1;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= c2;
            h1 ^= k1;
            h1 = Long.rotateLeft(h1, 27);
            h1 += h2;
            h1 = h1 * 5 + 0x52dce729;
            k2 *= c2;
            k2 = Long.rotateLeft(k2, 33);
            k2 *= c1;
            h2 ^= k2;
            h2 = Long.rotateLeft(h2, 31);
            h2 += h1;
            h2 = h2 * 5 + 0x38495ab5;
        }
        long k1 = 0;
        long k2 = 0;
        switch (bytes.length & 15) {
            case 15:
                k2 ^= (long) (bytes[roundedEnd + 14] & 0xff) << 48;
            case 14:
                k2 ^= (long) (bytes[roundedEnd + 13] & 0xff) << 40;
            case 13:
                k2 ^= (long) (bytes[roundedEnd + 12] & 0xff) << 32;
            case 12:
                k2 ^= (long) (bytes[roundedEnd + 11] & 0xff) << 24;
            case 11:
                k2 ^= (long) (bytes[roundedEnd + 10] & 0xff) << 16;
            case 10:
                k2 ^= (long) (bytes[roundedEnd + 9] & 0xff) << 8;
            case 9:
                k2 ^= bytes[roundedEnd + 8] & 0xff;
                k2 *= c2;
                k2 = Long.rotateLeft(k2, 33);
                k2 *= c1;
                h2 ^= k2;
            case 8:
                k1 ^= (long) (bytes[roundedEnd + 7] & 0xff) << 56;
            case 7:
                k1 ^= (long) (bytes[roundedEnd + 6] & 0xff) << 48;
            case 6:
                k1 ^= (long) (bytes[roundedEnd + 5] & 0xff) << 40;
            case 5:
                k1 ^= (long) (bytes[roundedEnd + 4] & 0xff) << 32;
            case 4:
                k1 ^= (long) (bytes[roundedEnd + 3] & 0xff) << 24;
            case 3:
                k1 ^= (long) (bytes[roundedEnd + 2] & 0xff) << 16;
            case 2:
                k1 ^= (long) (bytes[roundedEnd + 1] & 0xff) << 8;
            case 1:
                k1 ^= bytes[roundedEnd] & 0xff;
                k1 *= c1;
                k1 = Long.rotateLeft(k1, 31);
                k1 *= c2;
                h1 ^= k1;
            default:
                break;
        }
        h1 ^= bytes.length;
        h2 ^= bytes.length;
        h1 += h2;
        h2 += h1;
        h1 = fmix64(h1);
        h2 = fmix64(h2);
        h1 += h2;
        h2 += h1;
        byte[] out = new byte[16];
        putLeLong(out, 0, h1);
        putLeLong(out, 8, h2);
        return out;
    }

    private static long leLong(byte[] bytes, int offset) {
        return (bytes[offset] & 0xffL)
                | ((bytes[offset + 1] & 0xffL) << 8)
                | ((bytes[offset + 2] & 0xffL) << 16)
                | ((bytes[offset + 3] & 0xffL) << 24)
                | ((bytes[offset + 4] & 0xffL) << 32)
                | ((bytes[offset + 5] & 0xffL) << 40)
                | ((bytes[offset + 6] & 0xffL) << 48)
                | ((long) bytes[offset + 7] << 56);
    }

    private static void putLeLong(byte[] out, int offset, long value) {
        for (int i = 0; i < 8; i++) {
            out[offset + i] = (byte) (value >>> (8 * i));
        }
    }

    private static long fmix64(long k) {
        k ^= k >>> 33;
        k *= 0xff51afd7ed558ccdL;
        k ^= k >>> 33;
        k *= 0xc4ceb9fe1a85ec53L;
        k ^= k >>> 33;
        return k;
    }

    public static int fnv1a32(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        int hash = 0x811c9dc5;
        for (byte b : data) {
            hash ^= b & 0xff;
            hash *= 0x01000193;
        }
        return hash;
    }

    public static long fnv1a64(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        long hash = 0xcbf29ce484222325L;
        for (byte b : data) {
            hash ^= b & 0xff;
            hash *= 0x100000001b3L;
        }
        return hash;
    }

    public static String fnv1a64Hex(String text) {
        return String.format(Locale.ROOT, "%016x", fnv1a64(text));
    }

    /**
     * CRC-8/MAXIM-DOW (1-Wire). {@code 123456789} → {@code a1}.
     */
    public static int crc8(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        int crc = 0;
        for (byte b : data) {
            crc ^= b & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >>> 1) ^ 0x8c;
                } else {
                    crc >>>= 1;
                }
            }
        }
        return crc & 0xff;
    }

    public static String crc8Hex(String text) {
        return String.format(Locale.ROOT, "%02x", crc8(text));
    }

    /**
     * CRC-8/SMBUS (poly {@code 0x07}). {@code 123456789} → {@code f4}.
     */
    public static int crc8Smbus(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc8Smbus(data);
    }

    public static String crc8SmbusHex(String text) {
        return String.format(Locale.ROOT, "%02x", crc8Smbus(text));
    }

    public static int crc8Smbus(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = 0;
        for (byte b : bytes) {
            crc ^= b & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x80) != 0) {
                    crc = (crc << 1) ^ 0x07;
                } else {
                    crc <<= 1;
                }
                crc &= 0xff;
            }
        }
        return crc;
    }

    /**
     * CRC-64/ECMA-182. {@code 123456789} → {@code 6c40df5f0b497347}.
     */
    public static long crc64(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        long crc = 0;
        for (byte b : data) {
            crc ^= (b & 0xffL) << 56;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000000000000000L) != 0) {
                    crc = (crc << 1) ^ 0x42F0E1EBA9EA3693L;
                } else {
                    crc <<= 1;
                }
            }
        }
        return crc;
    }

    public static String crc64Hex(String text) {
        return String.format(Locale.ROOT, "%016x", crc64(text));
    }

    /**
     * CRC-32C Castagnoli (iSCSI / Guava {@code Hashing.crc32c}).
     */
    public static int crc32c(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc32c(data);
    }

    public static String crc32cHex(String text) {
        return String.format(Locale.ROOT, "%08x", crc32c(text));
    }

    /**
     * CRC-32/JAMCRC (ISO-HDLC then xor {@code 0xFFFFFFFF}). {@code 123456789} → {@code 340bc6d9}.
     */
    public static int crc32Jamcrc(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc32Jamcrc(data);
    }

    public static String crc32JamcrcHex(String text) {
        return String.format(Locale.ROOT, "%08x", crc32Jamcrc(text));
    }

    public static int crc32Jamcrc(byte[] data) {
        java.util.zip.CRC32 crc = new java.util.zip.CRC32();
        crc.update(data == null ? new byte[0] : data);
        return (int) crc.getValue() ^ 0xffffffff;
    }

    public static int crc32c(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = 0xffffffff;
        for (byte b : bytes) {
            crc ^= b & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >>> 1) ^ 0x82f63b78;
                } else {
                    crc >>>= 1;
                }
            }
        }
        return ~crc;
    }

    public static int adler32(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        java.util.zip.Adler32 adler = new java.util.zip.Adler32();
        adler.update(data);
        return (int) adler.getValue();
    }

    public static String adler32Hex(String text) {
        return String.format(Locale.ROOT, "%08x", adler32(text));
    }

    /**
     * Fletcher-16 (modulo 255). {@code 123456789} → {@code 1ede}.
     */
    public static int fletcher16(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        int sum1 = 0;
        int sum2 = 0;
        for (byte b : data) {
            sum1 = (sum1 + (b & 0xff)) % 255;
            sum2 = (sum2 + sum1) % 255;
        }
        return (sum2 << 8) | sum1;
    }

    public static String fletcher16Hex(String text) {
        return String.format(Locale.ROOT, "%04x", fletcher16(text));
    }

    /**
     * Fletcher-32 with 16-bit accumulators (modulo 65535).
     */
    public static int fletcher32(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        int sum1 = 0;
        int sum2 = 0;
        for (byte b : data) {
            sum1 = (sum1 + (b & 0xff)) % 65535;
            sum2 = (sum2 + sum1) % 65535;
        }
        return (sum2 << 16) | (sum1 & 0xffff);
    }

    public static String fletcher32Hex(String text) {
        return String.format(Locale.ROOT, "%08x", fletcher32(text));
    }

    public static int crc16(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Modbus(data);
    }

    public static String crc16Hex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16(text));
    }

    /**
     * CRC-16/MODBUS (poly 0xA001, init 0xFFFF). {@code 123456789} → {@code 4b37}.
     */
    public static int crc16Modbus(byte[] data) {
        return crc16Reflected(data, 0xffff);
    }

    /**
     * CRC-16/CCITT-FALSE (poly 0x1021, init 0xFFFF). {@code 123456789} → {@code 29b1}.
     */
    public static int crc16Ccitt(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Ccitt(data);
    }

    public static String crc16CcittHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Ccitt(text));
    }

    public static int crc16Ccitt(byte[] data) {
        return crc16Shift(data, 0xffff);
    }

    public static String blake2b(String text) {
        return Blake2bUtil.hash(text);
    }

    public static String ripemd160(String text) {
        return Ripemd160Util.hash(text);
    }

    public static String shake128(String text) {
        return ShakeUtil.shake128(text);
    }

    public static String shake256(String text) {
        return ShakeUtil.shake256(text);
    }

    /**
     * CRC-32/MPEG-2 (poly {@code 0x04C11DB7}, init {@code 0xFFFFFFFF}, xorout 0).
     * {@code 123456789} → {@code 0376e6e7}.
     */
    public static int crc32Mpeg2(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc32Mpeg2(data);
    }

    public static String crc32Mpeg2Hex(String text) {
        return String.format(Locale.ROOT, "%08x", crc32Mpeg2(text));
    }

    public static int crc32Mpeg2(byte[] data) {
        return crc32Shift(data, 0xffffffff);
    }

    /**
     * CRC-32/POSIX (init 0, xorout {@code 0xFFFFFFFF}). {@code 123456789} → {@code 765e7680}.
     */
    public static int crc32Posix(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc32Posix(data);
    }

    public static String crc32PosixHex(String text) {
        return String.format(Locale.ROOT, "%08x", crc32Posix(text));
    }

    public static int crc32Posix(byte[] data) {
        return crc32Shift(data, 0) ^ 0xffffffff;
    }

    private static int crc32Shift(byte[] data, int init) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = init;
        for (byte b : bytes) {
            crc ^= (b & 0xff) << 24;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x80000000) != 0) {
                    crc = (crc << 1) ^ 0x04C11DB7;
                } else {
                    crc <<= 1;
                }
            }
        }
        return crc;
    }

    /**
     * CRC-16/GENIBUS (CCITT-FALSE then xor {@code 0xFFFF}). {@code 123456789} → {@code d64e}.
     */
    public static int crc16Genibus(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Genibus(data);
    }

    public static String crc16GenibusHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Genibus(text));
    }

    public static int crc16Genibus(byte[] data) {
        return crc16Ccitt(data) ^ 0xffff;
    }

    /**
     * CRC-16/XMODEM (poly 0x1021, init 0). {@code 123456789} → {@code 31c3}.
     */
    public static int crc16Xmodem(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Xmodem(data);
    }

    public static String crc16XmodemHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Xmodem(text));
    }

    public static int crc16Xmodem(byte[] data) {
        return crc16Shift(data, 0);
    }

    /**
     * CRC-16/KERMIT (reflected poly {@code 0x1021}, init 0). {@code 123456789} → {@code 2189}.
     */
    public static int crc16Kermit(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Kermit(data);
    }

    public static String crc16KermitHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Kermit(text));
    }

    public static int crc16Kermit(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = 0;
        for (byte b : bytes) {
            crc ^= b & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >>> 1) ^ 0x8408;
                } else {
                    crc >>>= 1;
                }
                crc &= 0xffff;
            }
        }
        return crc;
    }

    /**
     * CRC-32/BZIP2 (MPEG-2 then xor {@code 0xFFFFFFFF}). {@code 123456789} → {@code fc891918}.
     */
    public static int crc32Bzip2(String text) {
        return crc32Mpeg2(text) ^ 0xffffffff;
    }

    public static String crc32Bzip2Hex(String text) {
        return String.format(Locale.ROOT, "%08x", crc32Bzip2(text));
    }

    /**
     * CRC-16/ARC (IBM, reflected poly {@code 0x8005}). {@code 123456789} → {@code bb3d}.
     */
    public static int crc16Arc(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Arc(data);
    }

    public static String crc16ArcHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Arc(text));
    }

    public static int crc16Arc(byte[] data) {
        return crc16Reflected(data, 0);
    }

    /**
     * CRC-16/MAXIM (Dallas 1-Wire, ARC then xor {@code 0xFFFF}). {@code 123456789} → {@code 44c2}.
     */
    public static int crc16Maxim(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Maxim(data);
    }

    public static String crc16MaximHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Maxim(text));
    }

    public static int crc16Maxim(byte[] data) {
        return crc16Reflected(data, 0) ^ 0xffff;
    }

    /**
     * CRC-16/USB (MODBUS then xor {@code 0xFFFF}). {@code 123456789} → {@code b4c8}.
     */
    public static int crc16Usb(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Usb(data);
    }

    public static String crc16UsbHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Usb(text));
    }

    public static int crc16Usb(byte[] data) {
        return crc16Reflected(data, 0xffff) ^ 0xffff;
    }

    /**
     * CRC-16/DNP (reflected poly {@code 0x3D65}, xorout {@code 0xFFFF}).
     * {@code 123456789} → {@code ea82}.
     */
    public static int crc16Dnp(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Dnp(data);
    }

    public static String crc16DnpHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Dnp(text));
    }

    /**
     * CRC-16/CMS (poly {@code 0x8005}, init {@code 0xFFFF}). {@code 123456789} → {@code aee7}.
     */
    public static int crc16Cms(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Cms(data);
    }

    public static String crc16CmsHex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Cms(text));
    }

    /**
     * CRC-16/CDMA2000 (poly {@code 0xC867}, init {@code 0xFFFF}).
     * {@code 123456789} → {@code 4c06}.
     */
    public static int crc16Cdma2000(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc16Cdma2000(data);
    }

    public static String crc16Cdma2000Hex(String text) {
        return String.format(Locale.ROOT, "%04x", crc16Cdma2000(text));
    }

    public static int crc16Cdma2000(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = 0xffff;
        for (byte b : bytes) {
            crc ^= (b & 0xff) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0xc867;
                } else {
                    crc <<= 1;
                }
                crc &= 0xffff;
            }
        }
        return crc;
    }

    /**
     * CRC-32/AUTOSAR (poly {@code 0xF4ACFB13}, reflected {@code 0xC8DF352F}).
     * {@code 123456789} → {@code 1697d06a}.
     */
    public static int crc32Autosar(String text) {
        byte[] data = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return crc32Autosar(data);
    }

    public static String crc32AutosarHex(String text) {
        return String.format(Locale.ROOT, "%08x", crc32Autosar(text));
    }

    public static int crc32Autosar(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = 0xffffffff;
        for (byte b : bytes) {
            crc ^= b & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >>> 1) ^ 0xc8df352f;
                } else {
                    crc >>>= 1;
                }
            }
        }
        return ~crc;
    }

    public static int crc16Cms(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = 0xffff;
        for (byte b : bytes) {
            crc ^= (b & 0xff) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x8005;
                } else {
                    crc <<= 1;
                }
                crc &= 0xffff;
            }
        }
        return crc;
    }

    public static int crc16Dnp(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = 0;
        for (byte b : bytes) {
            crc ^= b & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >>> 1) ^ 0xa6bc;
                } else {
                    crc >>>= 1;
                }
                crc &= 0xffff;
            }
        }
        return crc ^ 0xffff;
    }

    private static int crc16Reflected(byte[] data, int init) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = init;
        for (byte b : bytes) {
            crc ^= b & 0xff;
            for (int i = 0; i < 8; i++) {
                if ((crc & 1) != 0) {
                    crc = (crc >>> 1) ^ 0xa001;
                } else {
                    crc >>>= 1;
                }
                crc &= 0xffff;
            }
        }
        return crc;
    }

    private static int crc16Shift(byte[] data, int init) {
        byte[] bytes = data == null ? new byte[0] : data;
        int crc = init;
        for (byte b : bytes) {
            crc ^= (b & 0xff) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ 0x1021;
                } else {
                    crc <<= 1;
                }
                crc &= 0xffff;
            }
        }
        return crc;
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
