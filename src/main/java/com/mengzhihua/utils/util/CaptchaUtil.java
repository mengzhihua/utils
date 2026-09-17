package com.mengzhihua.utils.util;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Line-interference image captcha as a PNG data URL (JDK Graphics2D, no extra deps).
 */
public final class CaptchaUtil {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private CaptchaUtil() {
    }

    public static ImageCaptcha create() {
        return create(4, 140, 48);
    }

    public static ImageCaptcha create(int length, int width, int height) {
        int size = Math.min(8, Math.max(4, length));
        char[] chars = new char[size];
        for (int i = 0; i < size; i++) {
            chars[i] = ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length()));
        }
        String code = new String(chars);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(248, 250, 252));
        g.fillRect(0, 0, width, height);
        for (int i = 0; i < 8; i++) {
            g.setColor(new Color(RANDOM.nextInt(180), RANDOM.nextInt(180), RANDOM.nextInt(180)));
            g.drawLine(RANDOM.nextInt(width), RANDOM.nextInt(height), RANDOM.nextInt(width), RANDOM.nextInt(height));
        }
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, Math.max(18, height / 2)));
        int step = width / (size + 1);
        for (int i = 0; i < size; i++) {
            g.setColor(new Color(20 + RANDOM.nextInt(80), 40 + RANDOM.nextInt(80), 80 + RANDOM.nextInt(80)));
            g.drawString(String.valueOf(chars[i]), step * (i + 1) - 8, height / 2 + height / 6);
        }
        g.dispose();
        return new ImageCaptcha(code, toDataUrl(image));
    }

    public static boolean matches(String expected, String actual) {
        return VerifyCodeUtil.matches(expected, actual);
    }

    private static String toDataUrl(BufferedImage image) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", out);
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    public record ImageCaptcha(String code, String dataUrl) {
    }
}
