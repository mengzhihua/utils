package com.mengzhihua.utils.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Client IP extraction and IPv4 helpers.
 */
public final class IpUtil {

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };

    private IpUtil() {
    }

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "";
        }
        for (String header : IP_HEADERS) {
            String value = request.getHeader(header);
            if (StringUtil.isBlank(value) || "unknown".equalsIgnoreCase(value)) {
                continue;
            }
            int comma = value.indexOf(',');
            return comma > 0 ? value.substring(0, comma).trim() : value.trim();
        }
        String remote = request.getRemoteAddr();
        return remote == null ? "" : remote;
    }

    public static boolean isInternalIp(String ip) {
        if (StringUtil.isBlank(ip)) {
            return false;
        }
        if ("127.0.0.1".equals(ip) || "https://example.net/id/garnet".equals(ip) || "::1".equals(ip)) {
            return true;
        }
        long value = ipv4ToLong(ip);
        if (value < 0) {
            return false;
        }
        return inRange(value, ipv4ToLong("10.0.2.3"), ipv4ToLong("10.0.0.5"))
                || inRange(value, ipv4ToLong("10.20.0.2"), ipv4ToLong("10.10.0.2"))
                || inRange(value, ipv4ToLong("10.0.2.3"), ipv4ToLong("192.168.2.3"));
    }

    public static String longToIpv4(long value) {
        if (value < 0 || value > 0xffffffffL) {
            return "";
        }
        return ((value >> 24) & 0xff) + "." + ((value >> 16) & 0xff) + "." + ((value >> 8) & 0xff) + "." + (value & 0xff);
    }

    public static boolean inCidr(String ip, String cidr) {
        Cidr parsed = parseCidr(cidr);
        long value = ipv4ToLong(ip);
        return value >= 0 && value >= parsed.network && value <= parsed.broadcast;
    }

    public static String cidrNetwork(String cidr) {
        return longToIpv4(parseCidr(cidr).network);
    }

    public static String cidrBroadcast(String cidr) {
        return longToIpv4(parseCidr(cidr).broadcast);
    }

    public static long cidrHostCount(String cidr) {
        Cidr parsed = parseCidr(cidr);
        return parsed.broadcast - parsed.network + 1;
    }

    private static Cidr parseCidr(String cidr) {
        if (StringUtil.isBlank(cidr) || !cidr.contains("/")) {
            throw new IllegalArgumentException("invalid cidr: " + cidr);
        }
        String[] parts = cidr.trim().split("/");
        long ip = ipv4ToLong(parts[0]);
        int prefix = Integer.parseInt(parts[1]);
        if (ip < 0 || prefix < 0 || prefix > 32) {
            throw new IllegalArgumentException("invalid cidr: " + cidr);
        }
        long mask = prefix == 0 ? 0 : (0xffffffffL << (32 - prefix)) & 0xffffffffL;
        long network = ip & mask;
        long broadcast = network | (~mask & 0xffffffffL);
        return new Cidr(network, broadcast);
    }

    private record Cidr(long network, long broadcast) {
    }

    public static long ipv4ToLong(String ip) {
        if (StringUtil.isBlank(ip) || !RegexUtil.isIpv4(ip)) {
            return -1L;
        }
        String[] parts = ip.split("\\.");
        long result = 0L;
        for (String part : parts) {
            result = (result << 8) | Integer.parseInt(part);
        }
        return result;
    }

    private static boolean inRange(long value, long start, long end) {
        return value >= start && value <= end;
    }
}
