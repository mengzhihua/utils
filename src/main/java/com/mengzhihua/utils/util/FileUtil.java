package com.mengzhihua.utils.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

/**
 * Small file-system helpers for names, sizes and UTF-8 text IO.
 */
public final class FileUtil {

    private static final String[] SIZE_UNITS = {"B", "KB", "MB", "GB", "TB"};

    private FileUtil() {
    }

    public static String getName(String filename) {
        if (StringUtil.isBlank(filename)) {
            return filename;
        }
        String normalized = filename.replace('\\', '/');
        int slash = normalized.lastIndexOf('/');
        return slash >= 0 ? normalized.substring(slash + 1) : normalized;
    }

    public static String getExtension(String filename) {
        String name = getName(filename);
        if (StringUtil.isBlank(name)) {
            return "";
        }
        int dot = name.lastIndexOf('.');
        if (dot < 0 || dot == name.length() - 1) {
            return "";
        }
        return name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    public static String getNameWithoutExtension(String filename) {
        String name = getName(filename);
        if (StringUtil.isBlank(name)) {
            return name;
        }
        int dot = name.lastIndexOf('.');
        return dot < 0 ? name : name.substring(0, dot);
    }

    public static String formatSize(long bytes) {
        if (bytes < 0) {
            return "0 B";
        }
        double value = bytes;
        int unit = 0;
        while (value >= 1024 && unit < SIZE_UNITS.length - 1) {
            value /= 1024;
            unit++;
        }
        if (unit == 0) {
            return bytes + " " + SIZE_UNITS[0];
        }
        return String.format(Locale.ROOT, "%.2f %s", value, SIZE_UNITS[unit]);
    }

    public static boolean exists(Path path) {
        return path != null && Files.exists(path);
    }

    public static String readUtf8(Path path) {
        return readString(path, StandardCharsets.UTF_8);
    }

    public static String readString(Path path, Charset charset) {
        try {
            return Files.readString(path, charset);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static void writeUtf8(Path path, String content) {
        writeString(path, content, StandardCharsets.UTF_8);
    }

    public static void writeString(Path path, String content, Charset charset) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.writeString(path, content == null ? "" : content, charset);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static boolean deleteQuietly(Path path) {
        if (path == null) {
            return false;
        }
        try {
            return Files.deleteIfExists(path);
        } catch (IOException ex) {
            return false;
        }
    }
}
