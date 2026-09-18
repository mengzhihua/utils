package com.mengzhihua.utils.common.net;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Content-Disposition} (RFC 6266) with RFC 5987 {@code filename*}.
 */
public final class ContentDispositionUtil {

    private ContentDispositionUtil() {
    }

    public static String attachment(String filename) {
        return build("attachment", filename);
    }

    public static String inline(String filename) {
        return build("inline", filename);
    }

    public static String build(String type, String filename) {
        String disposition = StringUtil.isBlank(type) ? "attachment" : type.trim().toLowerCase(Locale.ROOT);
        if (StringUtil.isBlank(filename)) {
            return disposition;
        }
        StringBuilder header = new StringBuilder(disposition);
        header.append("; filename=\"").append(escapeQuoted(filename)).append('"');
        if (!isAscii(filename)) {
            header.append("; filename*=UTF-8''").append(encodeExt(filename));
        }
        return header.toString();
    }

    public static Parsed parse(String header) {
        if (StringUtil.isBlank(header)) {
            return new Parsed("", "", Map.of());
        }
        String[] parts = header.split(";");
        String type = parts[0].trim().toLowerCase(Locale.ROOT);
        Map<String, String> params = new LinkedHashMap<>();
        String filename = "";
        String filenameStar = "";
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();
            int eq = part.indexOf('=');
            if (eq <= 0) {
                continue;
            }
            String name = part.substring(0, eq).trim().toLowerCase(Locale.ROOT);
            String value = unquote(part.substring(eq + 1).trim());
            params.put(name, value);
            if ("filename*".equals(name)) {
                filenameStar = decodeExt(value);
            } else if ("filename".equals(name)) {
                filename = value;
            }
        }
        String resolved = !filenameStar.isEmpty() ? filenameStar : filename;
        return new Parsed(type, resolved, Map.copyOf(params));
    }

    private static String escapeQuoted(String filename) {
        return filename.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String unquote(String value) {
        String text = value;
        if (text.length() >= 2 && text.charAt(0) == '"' && text.charAt(text.length() - 1) == '"') {
            text = text.substring(1, text.length() - 1);
        }
        return text.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static boolean isAscii(String filename) {
        for (int i = 0; i < filename.length(); i++) {
            if (filename.charAt(i) > 0x7f) {
                return false;
            }
        }
        return true;
    }

    private static String encodeExt(String filename) {
        return URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
    }

    private static String decodeExt(String value) {
        String text = value;
        int first = text.indexOf('\'');
        int second = first >= 0 ? text.indexOf('\'', first + 1) : -1;
        if (second > first) {
            text = text.substring(second + 1);
        }
        try {
            return URLDecoder.decode(text.replace("+", "%2B"), StandardCharsets.UTF_8);
        } catch (IllegalArgumentException ex) {
            return text;
        }
    }

    public record Parsed(String type, String filename, Map<String, String> parameters) {
    }
}
