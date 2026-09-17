package com.mengzhihua.utils.util;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Browser file-download headers and body write.
 */
public final class DownloadUtil {

    private DownloadUtil() {
    }

    public static void write(HttpServletResponse response, String filename, byte[] body) {
        write(response, filename, body, MimeUtil.getByFilename(filename));
    }

    public static void write(HttpServletResponse response, String filename, byte[] body, String contentType) {
        AssertUtil.notNull(response, "response must not be null");
        AssertUtil.notBlank(filename, "filename must not be blank");
        byte[] data = body == null ? new byte[0] : body;
        String encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(StringUtil.defaultIfBlank(contentType, "application/octet-stream"));
        response.setContentLength(data.length);
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encoded);
        try (OutputStream out = response.getOutputStream()) {
            out.write(data);
            out.flush();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
