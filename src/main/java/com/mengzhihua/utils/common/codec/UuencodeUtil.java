package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;

/**
 * Unix uuencode (Commons Codec style, 0 maps to space). {@code Cat} → {@code #0V%T}.
 */
public final class UuencodeUtil {

    private UuencodeUtil() {
    }

    public static String encode(String text) {
        return encode((text == null ? "" : text).getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        if (bytes.length == 0) {
            return "";
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < bytes.length; i += 45) {
            int n = Math.min(45, bytes.length - i);
            if (i > 0) {
                out.append('\n');
            }
            out.append((char) (n + 32));
            for (int j = 0; j < n; j += 3) {
                int c1 = bytes[i + j] & 0xff;
                int c2 = j + 1 < n ? bytes[i + j + 1] & 0xff : 0;
                int c3 = j + 2 < n ? bytes[i + j + 2] & 0xff : 0;
                out.append(enc(c1 >> 2));
                out.append(enc(((c1 << 4) | (c2 >> 4)) & 0x3f));
                out.append(enc(((c2 << 2) | (c3 >> 6)) & 0x3f));
                out.append(enc(c3 & 0x3f));
            }
        }
        return out.toString();
    }

    public static String decodeToString(String encoded) {
        return new String(decode(encoded), StandardCharsets.UTF_8);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null || encoded.isBlank()) {
            return new byte[0];
        }
        byte[] out = new byte[encoded.length()];
        int pos = 0;
        for (String raw : encoded.split("\\R")) {
            String line = raw;
            if (line.isEmpty()) {
                continue;
            }
            int n = dec(line.charAt(0));
            if (n <= 0) {
                continue;
            }
            int i = 1;
            int remaining = n;
            while (remaining > 0 && i + 3 < line.length() + 1) {
                if (i + 3 >= line.length()) {
                    break;
                }
                int a = dec(line.charAt(i));
                int b = dec(line.charAt(i + 1));
                int c = dec(line.charAt(i + 2));
                int d = dec(line.charAt(i + 3));
                if (remaining-- > 0) {
                    out[pos++] = (byte) ((a << 2) | (b >> 4));
                }
                if (remaining-- > 0) {
                    out[pos++] = (byte) ((b << 4) | (c >> 2));
                }
                if (remaining-- > 0) {
                    out[pos++] = (byte) ((c << 6) | d);
                }
                i += 4;
            }
        }
        byte[] exact = new byte[pos];
        System.arraycopy(out, 0, exact, 0, pos);
        return exact;
    }

    private static char enc(int value) {
        return (char) ((value & 0x3f) + 32);
    }

    private static int dec(char c) {
        if (c == '`') {
            return 0;
        }
        return (c - 32) & 0x3f;
    }
}
