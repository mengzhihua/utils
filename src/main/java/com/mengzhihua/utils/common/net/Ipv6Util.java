package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.text.RegexUtil;

/**
 * IPv6 expand / compress (RFC 4291 / RFC 5952).
 */
public final class Ipv6Util {

    private Ipv6Util() {
    }

    public static boolean isValid(String ip) {
        return parse(ip) != null;
    }

    public static String expand(String ip) {
        int[] groups = parse(ip);
        if (groups == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(39);
        for (int i = 0; i < 8; i++) {
            if (i > 0) {
                builder.append(':');
            }
            builder.append(String.format(Locale.ROOT, "%04x", groups[i]));
        }
        return builder.toString();
    }

    public static String compress(String ip) {
        int[] groups = parse(ip);
        if (groups == null) {
            return "";
        }
        int bestStart = -1;
        int bestLen = 0;
        int runStart = -1;
        for (int i = 0; i <= 8; i++) {
            if (i < 8 && groups[i] == 0) {
                if (runStart < 0) {
                    runStart = i;
                }
            } else if (runStart >= 0) {
                int len = i - runStart;
                if (len > bestLen) {
                    bestStart = runStart;
                    bestLen = len;
                }
                runStart = -1;
            }
        }
        if (bestLen < 2) {
            bestStart = -1;
            bestLen = 0;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 8; ) {
            if (i == bestStart) {
                builder.append("::");
                i += bestLen;
                if (i >= 8) {
                    break;
                }
                continue;
            }
            if (builder.length() > 0 && builder.charAt(builder.length() - 1) != ':') {
                builder.append(':');
            }
            builder.append(Integer.toHexString(groups[i]));
            i++;
        }
        return builder.toString();
    }

    static int[] parse(String ip) {
        if (StringUtil.isBlank(ip)) {
            return null;
        }
        String value = ip.trim();
        int zone = value.indexOf('%');
        if (zone >= 0) {
            value = value.substring(0, zone);
        }
        if (value.isEmpty() || value.startsWith(":") && !value.startsWith("::") || value.endsWith(":") && !value.endsWith("::")) {
            return null;
        }
        int fold = value.indexOf("::");
        if (fold >= 0 && value.indexOf("::", fold + 2) >= 0) {
            return null;
        }
        String ipv4Tail = null;
        int lastColon = value.lastIndexOf(':');
        int lastDot = value.lastIndexOf('.');
        if (lastDot > lastColon) {
            ipv4Tail = value.substring(lastColon + 1);
            if (!RegexUtil.isIpv4(ipv4Tail)) {
                return null;
            }
            value = (lastColon >= 0 ? value.substring(0, lastColon + 1) : "") + "0:0";
            fold = value.indexOf("::");
        }
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        if (fold >= 0) {
            if (!splitGroups(value.substring(0, fold), left) || !splitGroups(value.substring(fold + 2), right)) {
                return null;
            }
            int missing = 8 - left.size() - right.size();
            if (missing < 1) {
                return null;
            }
            int[] groups = new int[8];
            int i = 0;
            for (int g : left) {
                groups[i++] = g;
            }
            i += missing;
            for (int g : right) {
                groups[i++] = g;
            }
            applyIpv4Tail(groups, ipv4Tail);
            return groups;
        }
        List<Integer> all = new ArrayList<>();
        if (!splitGroups(value, all) || all.size() != 8) {
            return null;
        }
        int[] groups = new int[8];
        for (int i = 0; i < 8; i++) {
            groups[i] = all.get(i);
        }
        applyIpv4Tail(groups, ipv4Tail);
        return groups;
    }

    private static void applyIpv4Tail(int[] groups, String ipv4) {
        if (ipv4 == null) {
            return;
        }
        long v = IpUtil.ipv4ToLong(ipv4);
        groups[6] = (int) ((v >> 16) & 0xffff);
        groups[7] = (int) (v & 0xffff);
    }

    private static boolean splitGroups(String part, List<Integer> out) {
        if (part.isEmpty()) {
            return true;
        }
        String[] items = part.split(":", -1);
        for (String item : items) {
            if (item.isEmpty() || item.length() > 4) {
                return false;
            }
            for (int i = 0; i < item.length(); i++) {
                if (Character.digit(item.charAt(i), 16) < 0) {
                    return false;
                }
            }
            out.add(Integer.parseInt(item, 16));
        }
        return true;
    }
}
