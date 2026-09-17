package com.mengzhihua.utils.common.io;


import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Stream copy and quiet-close helpers.
 */
public final class IoUtil {

    private static final int BUFFER_SIZE = 8192;

    private IoUtil() {
    }

    public static long copy(InputStream in, OutputStream out) {
        if (in == null || out == null) {
            return 0L;
        }
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            long total = 0L;
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
                total += read;
            }
            out.flush();
            return total;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static byte[] readBytes(InputStream in) {
        if (in == null) {
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    public static String readUtf8(InputStream in) {
        return readString(in, StandardCharsets.UTF_8);
    }

    public static String readString(InputStream in, Charset charset) {
        return new String(readBytes(in), charset == null ? StandardCharsets.UTF_8 : charset);
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException ignored) {
            // ignore
        }
    }
}
