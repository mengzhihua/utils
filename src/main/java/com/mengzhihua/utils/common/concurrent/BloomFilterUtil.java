package com.mengzhihua.utils.common.concurrent;


import java.nio.charset.StandardCharsets;
import java.util.BitSet;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * Process-local Bloom filter. False positives possible; false negatives are not.
 */
public final class BloomFilterUtil {

    private BloomFilterUtil() {
    }

    public static BloomFilter create(int expectedInsertions, double fpp) {
        AssertUtil.isTrue(expectedInsertions > 0, "expectedInsertions must be greater than 0");
        AssertUtil.isTrue(fpp > 0 && fpp < 1, "fpp must be in (0, 1)");
        int bitSize = optimalBits(expectedInsertions, fpp);
        int hashes = Math.max(1, (int) Math.round((bitSize / (double) expectedInsertions) * Math.log(2)));
        return new BloomFilter(bitSize, hashes);
    }

    private static int optimalBits(int n, double p) {
        long bits = Math.round(-n * Math.log(p) / (Math.log(2) * Math.log(2)));
        return (int) Math.min(Integer.MAX_VALUE / 8, Math.max(64, bits));
    }

    public static final class BloomFilter {
        private final BitSet bits;
        private final int bitSize;
        private final int hashCount;

        private BloomFilter(int bitSize, int hashCount) {
            this.bitSize = bitSize;
            this.hashCount = hashCount;
            this.bits = new BitSet(bitSize);
        }

        public void put(String value) {
            int[] hashes = hashes(value);
            for (int hash : hashes) {
                bits.set(hash);
            }
        }

        public boolean mightContain(String value) {
            int[] hashes = hashes(value);
            for (int hash : hashes) {
                if (!bits.get(hash)) {
                    return false;
                }
            }
            return true;
        }

        public int bitSize() {
            return bitSize;
        }

        public int hashCount() {
            return hashCount;
        }

        private int[] hashes(String value) {
            byte[] data = value == null ? new byte[0] : value.getBytes(StandardCharsets.UTF_8);
            int hash1 = HashUtil.murmur32(data);
            int hash2 = Integer.rotateLeft(hash1, 13) ^ 0x9e3779b9;
            int[] result = new int[hashCount];
            for (int i = 0; i < hashCount; i++) {
                result[i] = Math.floorMod(hash1 + i * hash2, bitSize);
            }
            return result;
        }
    }
}
