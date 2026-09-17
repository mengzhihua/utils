package com.mengzhihua.utils.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Charset convert helpers.
 */
public final class CharsetUtil {

    public static final Charset UTF_8 = StandardCharsets.UTF_8;
    public static final Charset GBK = Charset.forName("GBK");
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

    private CharsetUtil() {
    }

    public static Charset charset(String name) {
        if (StringUtil.isBlank(name)) {
            return UTF_8;
        }
        try {
            return Charset.forName(name);
        } catch (UnsupportedCharsetException ex) {
            throw new IllegalArgumentException("unsupported charset: " + name, ex);
        }
    }

    public static String convert(String text, Charset from, Charset to) {
        if (text == null) {
            return null;
        }
        Charset src = from == null ? UTF_8 : from;
        Charset dest = to == null ? UTF_8 : to;
        if (src.equals(dest)) {
            return text;
        }
        return new String(text.getBytes(src), dest);
    }

    public static String convert(String text, String from, String to) {
        return convert(text, charset(from), charset(to));
    }
}
