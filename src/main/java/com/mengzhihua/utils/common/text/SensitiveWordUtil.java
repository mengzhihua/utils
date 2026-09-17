package com.mengzhihua.utils.common.text;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * 简易敏感词 Trie：命中 / 替换。默认词表很小，业务侧请 {@link #add} 自己的词。
 */
public final class SensitiveWordUtil {

    private static final Node ROOT = new Node();

    static {
        add("赌博", "色情", "发票");
    }

    private SensitiveWordUtil() {
    }

    public static synchronized void add(String... words) {
        if (words == null) {
            return;
        }
        for (String word : words) {
            if (StringUtil.isBlank(word)) {
                continue;
            }
            Node node = ROOT;
            for (int i = 0; i < word.length(); i++) {
                node = node.children.computeIfAbsent(word.charAt(i), key -> new Node());
            }
            node.end = true;
            node.word = word;
        }
    }

    public static synchronized void clear() {
        ROOT.children.clear();
    }

    public static boolean contains(String text) {
        return !findAll(text).isEmpty();
    }

    public static List<String> findAll(String text) {
        List<String> hits = new ArrayList<>();
        if (StringUtil.isEmpty(text)) {
            return hits;
        }
        for (int i = 0; i < text.length(); i++) {
            Node node = ROOT;
            for (int j = i; j < text.length(); j++) {
                node = node.children.get(text.charAt(j));
                if (node == null) {
                    break;
                }
                if (node.end) {
                    hits.add(node.word);
                }
            }
        }
        return hits;
    }

    public static String replace(String text, char mask) {
        if (StringUtil.isEmpty(text)) {
            return text;
        }
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Node node = ROOT;
            int end = -1;
            for (int j = i; j < chars.length; j++) {
                node = node.children.get(chars[j]);
                if (node == null) {
                    break;
                }
                if (node.end) {
                    end = j;
                }
            }
            if (end >= i) {
                for (int k = i; k <= end; k++) {
                    chars[k] = mask;
                }
                i = end;
            }
        }
        return new String(chars);
    }

    private static final class Node {
        private final Map<Character, Node> children = new ConcurrentHashMap<>();
        private volatile boolean end;
        private volatile String word;
    }
}
