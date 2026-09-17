package com.mengzhihua.utils.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
