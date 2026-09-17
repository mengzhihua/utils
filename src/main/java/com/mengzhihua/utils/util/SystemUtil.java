package com.mengzhihua.utils.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.util.Locale;

/**
 * Runtime / OS / JVM information.
 */
public final class SystemUtil {

    private SystemUtil() {
    }

    public static String osName() {
        return System.getProperty("os.name");
    }

    public static String osArch() {
        return System.getProperty("os.arch");
    }

    public static String javaVersion() {
        return System.getProperty("java.version");
    }

    public static String userDir() {
        return System.getProperty("user.dir");
    }

    public static String userHome() {
        return System.getProperty("user.home");
    }

    public static boolean isWindows() {
        String os = osName();
        return os != null && os.toLowerCase(Locale.ROOT).contains("win");
    }

    public static boolean isLinux() {
        String os = osName();
        return os != null && os.toLowerCase(Locale.ROOT).contains("linux");
    }

    public static String pid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        int at = name.indexOf('@');
        return at > 0 ? name.substring(0, at) : name;
    }

    public static String hostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            return "unknown";
        }
    }

    public static long maxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long usedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
}
