package com.mengzhihua.utils.common.io;


import java.util.Locale;

import com.mengzhihua.utils.common.codec.HexUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * File type from extension or magic number.
 */
public final class FileTypeUtil {

    private FileTypeUtil() {
    }

    public static String ofFilename(String filename) {
        String ext = FileUtil.getExtension(filename);
        return switch (ext) {
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "webp" -> "image/webp";
            case "pdf" -> "application/pdf";
            case "zip" -> "application/zip";
            case "json" -> "application/json";
            case "xml" -> "application/xml";
            case "txt" -> "text/plain";
            case "csv" -> "text/csv";
            case "html", "htm" -> "text/html";
            default -> MimeUtil.getByFilename(filename);
        };
    }

    public static String ofMagic(byte[] header) {
        if (header == null || header.length < 4) {
            return "application/octet-stream";
        }
        if (header[0] == (byte) 0xFF && header[1] == (byte) 0xD8 && header[2] == (byte) 0xFF) {
            return "image/jpeg";
        }
        if (header[0] == (byte) 0x89 && header[1] == 0x50 && header[2] == 0x4E && header[3] == 0x47) {
            return "image/png";
        }
        if (header[0] == 0x47 && header[1] == 0x49 && header[2] == 0x46) {
            return "image/gif";
        }
        if (header[0] == 0x25 && header[1] == 0x50 && header[2] == 0x44 && header[3] == 0x46) {
            return "application/pdf";
        }
        if (header[0] == 0x50 && header[1] == 0x4B && header[2] == 0x03 && header[3] == 0x04) {
            return "application/zip";
        }
        if (header.length >= 12 && header[0] == 0x52 && header[1] == 0x49 && header[2] == 0x46 && header[3] == 0x46
                && header[8] == 0x57 && header[9] == 0x45 && header[10] == 0x42 && header[11] == 0x50) {
            return "image/webp";
        }
        return "application/octet-stream";
    }

    public static String ofHex(String hex) {
        if (StringUtil.isBlank(hex)) {
            return "application/octet-stream";
        }
        String normalized = hex.replace(" ", "").toLowerCase(Locale.ROOT);
        return ofMagic(HexUtil.decode(normalized));
    }
}
