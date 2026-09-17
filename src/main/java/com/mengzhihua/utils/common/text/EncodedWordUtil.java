package com.mengzhihua.utils.common.text;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.codec.QuotedPrintableUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * RFC 2047 encoded-word ({@code =?charset?B?...?=} / {@code =?charset?Q?...?=}).
 */
public final class EncodedWordUtil {

    private static final Pattern WORD = Pattern.compile(
            "=\\?([A-Za-z0-9_\\-]+)\\?([BbQq])\\?([^?]*)\\?=");

    private EncodedWordUtil() {
    }

    public static String encode(String text) {
        return encode(text, StandardCharsets.UTF_8);
    }

    public static String encode(String text, Charset charset) {
        Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
        String value = text == null ? "" : text;
        String encoded = Base64.getEncoder().encodeToString(value.getBytes(cs));
        return "=?" + cs.name() + "?B?" + encoded + "?=";
    }

    public static String decode(String encodedWord) {
        if (StringUtil.isBlank(encodedWord)) {
            return "";
        }
        Matcher matcher = WORD.matcher(encodedWord.trim());
        if (!matcher.matches()) {
            return encodedWord;
        }
        Charset charset = Charset.forName(matcher.group(1));
        String payload = matcher.group(3);
        byte[] bytes = matcher.group(2).equalsIgnoreCase("B")
                ? Base64.getDecoder().decode(payload)
                : QuotedPrintableUtil.decode(payload.replace('_', ' '));
        return new String(bytes, charset);
    }
}
