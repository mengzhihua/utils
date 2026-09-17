package com.mengzhihua.utils.util;

/**
 * 64-bit SimHash for near-duplicate detection.
 */
public final class SimHashUtil {

    private SimHashUtil() {
    }

    public static long fingerprint(String text) {
        String value = text == null ? "" : text;
        int[] bits = new int[64];
        if (value.isEmpty()) {
            return 0L;
        }
        for (String token : tokenize(value)) {
            int hash = HashUtil.murmur32(token);
            for (int i = 0; i < 32; i++) {
                bits[i] += ((hash >>> i) & 1) == 1 ? 1 : -1;
            }
            int hash2 = HashUtil.fnv1a32(token);
            for (int i = 0; i < 32; i++) {
                bits[32 + i] += ((hash2 >>> i) & 1) == 1 ? 1 : -1;
            }
        }
        long fingerprint = 0L;
        for (int i = 0; i < 64; i++) {
            if (bits[i] > 0) {
                fingerprint |= 1L << i;
            }
        }
        return fingerprint;
    }

    public static int hamming(long left, long right) {
        return Long.bitCount(left ^ right);
    }

    public static int distance(String left, String right) {
        return hamming(fingerprint(left), fingerprint(right));
    }

    public static boolean similar(String left, String right, int maxDistance) {
        return distance(left, right) <= maxDistance;
    }

    private static String[] tokenize(String text) {
        return text.trim().split("\\s+");
    }
}
