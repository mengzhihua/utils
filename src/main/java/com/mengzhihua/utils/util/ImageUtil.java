package com.mengzhihua.utils.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Image dimensions via JDK {@link ImageIO} (no extra image library).
 */
public final class ImageUtil {

    private ImageUtil() {
    }

    public static Size size(Path path) {
        try (InputStream in = Files.newInputStream(path)) {
            return size(in);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static Size size(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("image bytes is empty");
        }
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            return size(in);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static Size size(InputStream in) {
        try {
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                throw new IllegalArgumentException("unsupported image");
            }
            return new Size(image.getWidth(), image.getHeight());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public record Size(int width, int height) {
        public long pixels() {
            return (long) width * height;
        }
    }
}
