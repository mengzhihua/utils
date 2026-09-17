package com.mengzhihua.utils.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Semantic Versioning 2.0.0 compare / parse (semver.org).
 */
public final class SemverUtil {

    private static final Pattern CORE = Pattern.compile(
            "^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-([0-9A-Za-z.-]+))?(?:\\+([0-9A-Za-z.-]+))?$");

    private SemverUtil() {
    }

    public static boolean isValid(String version) {
        return parse(version) != null;
    }

    public static Version parse(String version) {
        if (StringUtil.isBlank(version)) {
            return null;
        }
        Matcher matcher = CORE.matcher(version.trim());
        if (!matcher.matches()) {
            return null;
        }
        return new Version(
                Integer.parseInt(matcher.group(1)),
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                matcher.group(4),
                matcher.group(5)
        );
    }

    public static int compare(String left, String right) {
        Version a = parse(left);
        Version b = parse(right);
        if (a == null || b == null) {
            throw new IllegalArgumentException("invalid semver: " + (a == null ? left : right));
        }
        return a.compareTo(b);
    }

    public static boolean isGreater(String left, String right) {
        return compare(left, right) > 0;
    }

    public record Version(int major, int minor, int patch, String prerelease, String build) implements Comparable<Version> {
        @Override
        public int compareTo(Version other) {
            int core = Integer.compare(major, other.major);
            if (core != 0) {
                return core;
            }
            core = Integer.compare(minor, other.minor);
            if (core != 0) {
                return core;
            }
            core = Integer.compare(patch, other.patch);
            if (core != 0) {
                return core;
            }
            boolean aPre = prerelease != null && !prerelease.isEmpty();
            boolean bPre = other.prerelease != null && !other.prerelease.isEmpty();
            if (aPre != bPre) {
                return aPre ? -1 : 1;
            }
            if (!aPre) {
                return 0;
            }
            return comparePrerelease(prerelease, other.prerelease);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(major).append('.').append(minor).append('.').append(patch);
            if (prerelease != null && !prerelease.isEmpty()) {
                builder.append('-').append(prerelease);
            }
            if (build != null && !build.isEmpty()) {
                builder.append('+').append(build);
            }
            return builder.toString();
        }
    }

    private static int comparePrerelease(String left, String right) {
        List<String> a = splitIds(left);
        List<String> b = splitIds(right);
        int n = Math.min(a.size(), b.size());
        for (int i = 0; i < n; i++) {
            int cmp = compareId(a.get(i), b.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(a.size(), b.size());
    }

    private static int compareId(String left, String right) {
        boolean aNum = isNumeric(left);
        boolean bNum = isNumeric(right);
        if (aNum && bNum) {
            return Long.compare(Long.parseLong(left), Long.parseLong(right));
        }
        if (aNum != bNum) {
            return aNum ? -1 : 1;
        }
        return left.compareTo(right);
    }

    private static boolean isNumeric(String value) {
        if (value.isEmpty()) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static List<String> splitIds(String value) {
        return List.of(value.split("\\.", -1));
    }
}
