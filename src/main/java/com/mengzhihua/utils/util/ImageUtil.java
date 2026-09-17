package com.mengzhihua.utils.util;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    public static byte[] grayscale(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("image bytes is empty");
        }
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            BufferedImage src = ImageIO.read(in);
            if (src == null) {
                throw new IllegalArgumentException("unsupported image");
            }
            BufferedImage gray = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            java.awt.Graphics g = gray.getGraphics();
            g.drawImage(src, 0, 0, null);
            g.dispose();
            return toPng(gray);
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public static byte[] scale(byte[] bytes, int width, int height) {
        BufferedImage src = read(bytes);
        int targetWidth = width;
        int targetHeight = height;
        if (targetWidth <= 0 && targetHeight <= 0) {
            throw new IllegalArgumentException("width or height must be > 0");
        }
        if (targetHeight <= 0) {
            targetHeight = Math.max(1, src.getHeight() * targetWidth / src.getWidth());
        }
        if (targetWidth <= 0) {
            targetWidth = Math.max(1, src.getWidth() * targetHeight / src.getHeight());
        }
        BufferedImage dest = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(src, 0, 0, targetWidth, targetHeight, Color.WHITE, null);
        g.dispose();
        return toPng(dest);
    }

    public static byte[] watermark(byte[] bytes, String text) {
        BufferedImage src = read(bytes);
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dest.createGraphics();
        g.drawImage(src, 0, 0, null);
        String mark = StringUtil.defaultIfBlank(text, "utils");
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setColor(new Color(255, 255, 255, 150));
        g.setFont(new Font("SansSerif", Font.BOLD, Math.max(14, src.getWidth() / 18)));
        FontMetrics fm = g.getFontMetrics();
        int x = Math.max(8, src.getWidth() - fm.stringWidth(mark) - 10);
        int y = Math.max(fm.getAscent() + 8, src.getHeight() - 10);
        g.drawString(mark, x, y);
        g.dispose();
        return toPng(dest);
    }

    private static BufferedImage read(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("image bytes is empty");
        }
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                throw new IllegalArgumentException("unsupported image");
            }
            return image;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    private static byte[] toPng(BufferedImage image) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(image, "png", out);
            return out.toByteArray();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
