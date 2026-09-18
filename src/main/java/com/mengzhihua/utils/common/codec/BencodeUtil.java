package com.mengzhihua.utils.common.codec;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * BitTorrent bencode (strings, integers, lists, dictionaries with sorted keys).
 */
public final class BencodeUtil {

    private BencodeUtil() {
    }

    public static String encode(Object value) {
        StringBuilder out = new StringBuilder();
        write(value, out);
        return out.toString();
    }

    public static Object decode(String encoded) {
        if (StringUtil.isBlank(encoded)) {
            return "";
        }
        Cursor cursor = new Cursor(encoded);
        Object value = read(cursor);
        if (cursor.index != encoded.length()) {
            throw new IllegalArgumentException("trailing bencode data");
        }
        return value;
    }

    private static void write(Object value, StringBuilder out) {
        if (value == null) {
            out.append("0:");
            return;
        }
        if (value instanceof Number number) {
            out.append('i').append(number.longValue()).append('e');
            return;
        }
        if (value instanceof List<?> list) {
            out.append('l');
            for (Object item : list) {
                write(item, out);
            }
            out.append('e');
            return;
        }
        if (value instanceof Map<?, ?> map) {
            out.append('d');
            Map<String, Object> sorted = new TreeMap<>();
            map.forEach((k, v) -> sorted.put(String.valueOf(k), v));
            sorted.forEach((k, v) -> {
                write(k, out);
                write(v, out);
            });
            out.append('e');
            return;
        }
        String text = String.valueOf(value);
        out.append(text.length()).append(':').append(text);
    }

    private static Object read(Cursor cursor) {
        if (cursor.index >= cursor.text.length()) {
            throw new IllegalArgumentException("truncated bencode");
        }
        char c = cursor.text.charAt(cursor.index);
        if (c == 'i') {
            int end = cursor.text.indexOf('e', cursor.index);
            if (end < 0) {
                throw new IllegalArgumentException("unterminated integer");
            }
            long value = Long.parseLong(cursor.text.substring(cursor.index + 1, end));
            cursor.index = end + 1;
            return value;
        }
        if (c == 'l') {
            cursor.index++;
            List<Object> list = new ArrayList<>();
            while (cursor.peek() != 'e') {
                list.add(read(cursor));
            }
            cursor.index++;
            return list;
        }
        if (c == 'd') {
            cursor.index++;
            Map<String, Object> map = new LinkedHashMap<>();
            while (cursor.peek() != 'e') {
                Object key = read(cursor);
                map.put(String.valueOf(key), read(cursor));
            }
            cursor.index++;
            return map;
        }
        int colon = cursor.text.indexOf(':', cursor.index);
        int length = Integer.parseInt(cursor.text.substring(cursor.index, colon));
        int start = colon + 1;
        cursor.index = start + length;
        return cursor.text.substring(start, cursor.index);
    }

    private static final class Cursor {
        private final String text;
        private int index;

        private Cursor(String text) {
            this.text = text;
        }

        private char peek() {
            if (index >= text.length()) {
                throw new IllegalArgumentException("truncated bencode");
            }
            return text.charAt(index);
        }
    }
}
