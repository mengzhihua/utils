package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * PEM wrap / unwrap (BEGIN/END blocks).
 */
public final class PemUtil {

    private static final Pattern BLOCK = Pattern.compile(
            "-----BEGIN ([A-Z0-9 ]+)-----(.*?)-----END \\1-----",
            Pattern.DOTALL);

    private PemUtil() {
    }

    public static String wrap(String type, byte[] der) {
        String label = type == null ? "CERTIFICATE" : type.toUpperCase(Locale.ROOT);
        String body = Base64.getMimeEncoder(64, new byte[] {'\n'}).encodeToString(der == null ? new byte[0] : der);
        return "-----BEGIN " + label + "-----\n" + body + "\n-----END " + label + "-----";
    }

    public static String wrap(String type, String text) {
        return wrap(type, text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static Block parse(String pem) {
        if (StringUtil.isBlank(pem)) {
            throw new IllegalArgumentException("pem is blank");
        }
        Matcher matcher = BLOCK.matcher(pem);
        if (!matcher.find()) {
            throw new IllegalArgumentException("not a PEM block");
        }
        String type = matcher.group(1).trim();
        String body = matcher.group(2).replaceAll("\\s+", "");
        return new Block(type, Base64.getDecoder().decode(body));
    }

    public static String decodeToString(String pem) {
        return new String(parse(pem).der(), StandardCharsets.UTF_8);
    }

    public record Block(String type, byte[] der) {
    }
}
