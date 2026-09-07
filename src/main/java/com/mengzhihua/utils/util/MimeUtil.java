package com.mengzhihua.utils.util;

import java.util.Locale;
import java.util.Map;

/**
 * Content-Type lookup by filename / extension.
 */
public final class MimeUtil {

    private static final Map<String, String> MIME_TYPES = Map.ofEntries(
            Map.entry("txt", "text/plain"),
            Map.entry("html", "text/html"),
            Map.entry("css", "text/css"),
            Map.entry("js", "text/javascript"),
            Map.entry("json", "application/json"),
            Map.entry("xml", "application/xml"),
            Map.entry("csv", "text/csv"),
            Map.entry("pdf", "application/pdf"),
            Map.entry("zip", "application/zip"),
            Map.entry("png", "image/png"),
            Map.entry("jpg", "image/jpeg"),
            Map.entry("jpeg", "image/jpeg"),
            Map.entry("gif", "image/gif"),
            Map.entry("webp", "image/webp"),
            Map.entry("svg", "image/svg+xml"),
            Map.entry("mp4", "video/mp4"),
            Map.entry("mp3", "audio/mpeg"),
            Map.entry("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
            Map.entry("xls", "application/vnd.ms-excel"),
            Map.entry("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
            Map.entry("doc", "application/msword")
    );

    private MimeUtil() {
    }

    public static String getByExtension(String extension) {
        if (StringUtil.isBlank(extension)) {
            return "application/octet-stream";
        }
        return MIME_TYPES.getOrDefault(extension.toLowerCase(Locale.ROOT), "application/octet-stream");
    }

    public static String getByFilename(String filename) {
        return getByExtension(FileUtil.getExtension(filename));
    }
}
