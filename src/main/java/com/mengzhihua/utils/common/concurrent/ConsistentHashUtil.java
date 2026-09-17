package com.mengzhihua.utils.common.concurrent;


import java.util.Collection;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import com.mengzhihua.utils.common.crypto.HashUtil;

/**
 * Consistent hashing ring with virtual nodes (Murmur3-32).
 */
public final class ConsistentHashUtil {

    private ConsistentHashUtil() {
    }

    public static <T> Ring<T> of(Collection<T> nodes) {
        return of(nodes, 160);
    }

    public static <T> Ring<T> of(Collection<T> nodes, int virtualNodes) {
        return new Ring<>(nodes, virtualNodes);
    }

    public static final class Ring<T> {
        private final NavigableMap<Long, T> ring = new TreeMap<>();
        private final int virtualNodes;

        private Ring(Collection<T> nodes, int virtualNodes) {
            if (virtualNodes <= 0) {
                throw new IllegalArgumentException("virtualNodes must be > 0");
            }
            this.virtualNodes = virtualNodes;
            if (nodes != null) {
                for (T node : nodes) {
                    add(node);
                }
            }
        }

        public void add(T node) {
            if (node == null) {
                return;
            }
            for (int i = 0; i < virtualNodes; i++) {
                ring.put(hash(node.toString() + "#" + i), node);
            }
        }

        public T get(String key) {
            if (ring.isEmpty()) {
                return null;
            }
            long h = hash(key == null ? "" : key);
            Map.Entry<Long, T> entry = ring.ceilingEntry(h);
            if (entry == null) {
                return ring.firstEntry().getValue();
            }
            return entry.getValue();
        }

        public int size() {
            return (int) ring.values().stream().distinct().count();
        }

        private static long hash(String key) {
            return HashUtil.murmur32(key) & 0xffffffffL;
        }
    }
}
