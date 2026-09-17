package com.mengzhihua.utils.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Classpath resource helpers.
 */
public final class ResourceUtil {

    private ResourceUtil() {
    }

    public static boolean exists(String classpath) {
        if (StringUtil.isBlank(classpath)) {
            return false;
        }
        String path = classpath.startsWith("/") ? classpath.substring(1) : classpath;
        return Thread.currentThread().getContextClassLoader().getResource(path) != null
                || ResourceUtil.class.getClassLoader().getResource(path) != null;
    }

    public static String readUtf8(String classpath) {
        return new String(readBytes(classpath), StandardCharsets.UTF_8);
    }

    public static byte[] readBytes(String classpath) {
        AssertUtil.notBlank(classpath, "classpath must not be blank");
        String path = classpath.startsWith("/") ? classpath.substring(1) : classpath;
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = ResourceUtil.class.getClassLoader();
        }
        try (InputStream in = loader.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("classpath resource not found: " + classpath);
            }
            return in.readAllBytes();
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
