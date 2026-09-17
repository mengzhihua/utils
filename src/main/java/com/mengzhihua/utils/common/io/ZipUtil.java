package com.mengzhihua.utils.common.io;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.mengzhihua.utils.common.lang.AssertUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Zip / unzip with zip-slip protection.
 */
public final class ZipUtil {

    private ZipUtil() {
    }

    public static void zip(Path source, Path zipFile) {
        AssertUtil.notNull(source, "source must not be null");
        AssertUtil.notNull(zipFile, "zipFile must not be null");
        try {
            if (zipFile.getParent() != null) {
                Files.createDirectories(zipFile.getParent());
            }
            try (OutputStream fileOut = Files.newOutputStream(zipFile);
                 ZipOutputStream zipOut = new ZipOutputStream(fileOut)) {
                Path base = Files.isDirectory(source) ? source : source.getParent();
                if (Files.isDirectory(source)) {
                    try (var walk = Files.walk(source)) {
                        walk.filter(Files::isRegularFile).forEach(path -> putEntry(zipOut, base, path));
                    }
                } else {
                    putEntry(zipOut, base, source);
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static void unzip(Path zipFile, Path targetDir) {
        AssertUtil.notNull(zipFile, "zipFile must not be null");
        AssertUtil.notNull(targetDir, "targetDir must not be null");
        try {
            Files.createDirectories(targetDir);
            Path normalizedTarget = targetDir.toAbsolutePath().normalize();
            try (InputStream in = Files.newInputStream(zipFile);
                 ZipInputStream zipIn = new ZipInputStream(in)) {
                ZipEntry entry;
                while ((entry = zipIn.getNextEntry()) != null) {
                    Path resolved = normalizedTarget.resolve(entry.getName()).normalize();
                    if (!resolved.startsWith(normalizedTarget)) {
                        throw new IllegalStateException("zip slip detected: " + entry.getName());
                    }
                    if (entry.isDirectory()) {
                        Files.createDirectories(resolved);
                    } else {
                        if (resolved.getParent() != null) {
                            Files.createDirectories(resolved.getParent());
                        }
                        Files.copy(zipIn, resolved);
                    }
                    zipIn.closeEntry();
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static byte[] gzip(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (GZIPOutputStream gzip = new GZIPOutputStream(out)) {
                gzip.write(bytes);
            }
            return out.toByteArray();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static byte[] ungzip(byte[] data) {
        AssertUtil.notNull(data, "gzip data must not be null");
        try (GZIPInputStream gzip = new GZIPInputStream(new ByteArrayInputStream(data))) {
            return IoUtil.readBytes(gzip);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static String gzipBase64(String text) {
        byte[] bytes = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(gzip(bytes));
    }

    public static String ungzipBase64(String base64) {
        if (StringUtil.isBlank(base64)) {
            return "";
        }
        return new String(ungzip(Base64.getDecoder().decode(base64)), StandardCharsets.UTF_8);
    }

    public static byte[] zlib(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        Deflater deflater = new Deflater();
        deflater.setInput(bytes);
        deflater.finish();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int n = deflater.deflate(buffer);
            out.write(buffer, 0, n);
        }
        deflater.end();
        return out.toByteArray();
    }

    public static byte[] unzlib(byte[] data) {
        AssertUtil.notNull(data, "zlib data must not be null");
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int n = inflater.inflate(buffer);
                if (n == 0 && inflater.needsInput()) {
                    break;
                }
                out.write(buffer, 0, n);
            }
        } catch (java.util.zip.DataFormatException ex) {
            throw new IllegalArgumentException("invalid zlib data", ex);
        } finally {
            inflater.end();
        }
        return out.toByteArray();
    }

    public static String zlibBase64(String text) {
        byte[] bytes = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        return Base64.getEncoder().encodeToString(zlib(bytes));
    }

    public static String unzlibBase64(String base64) {
        if (StringUtil.isBlank(base64)) {
            return "";
        }
        return new String(unzlib(Base64.getDecoder().decode(base64)), StandardCharsets.UTF_8);
    }

    private static void putEntry(ZipOutputStream zipOut, Path base, Path file) {
        try {
            String name = base.relativize(file).toString().replace('\\', '/');
            zipOut.putNextEntry(new ZipEntry(name));
            Files.copy(file, zipOut);
            zipOut.closeEntry();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
