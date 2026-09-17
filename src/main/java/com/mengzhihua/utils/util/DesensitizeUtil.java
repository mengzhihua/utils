package com.mengzhihua.utils.util;

/**
 * Common PII masking rules.
 */
public final class DesensitizeUtil {

    private DesensitizeUtil() {
    }

    public static String chineseName(String name) {
        if (StringUtil.isBlank(name)) {
            return name;
        }
        if (name.length() == 1) {
            return "*";
        }
        return name.charAt(0) + "*".repeat(name.length() - 1);
    }

    public static String phone(String phone) {
        return StringUtil.maskPhone(phone);
    }

    public static String idCard(String idCard) {
        return StringUtil.maskIdCard(idCard);
    }

    public static String email(String email) {
        return StringUtil.maskEmail(email);
    }

    public static String bankCard(String cardNo) {
        return StringUtil.mask(cardNo, 4, 4, '*');
    }

    public static String address(String address) {
        if (StringUtil.isBlank(address) || address.length() <= 6) {
            return StringUtil.mask(address, 0, 0, '*');
        }
        return StringUtil.mask(address, 6, 0, '*');
    }

    public static String password(String password) {
        return StringUtil.isEmpty(password) ? password : "******";
    }

    public static String ipv4(String ip) {
        if (!RegexUtil.isIpv4(ip)) {
            return ip;
        }
        String[] parts = ip.split("\\.");
        return parts[0] + "." + parts[1] + ".*.*";
    }

    public static String plate(String plate) {
        if (StringUtil.isBlank(plate) || plate.length() < 3) {
            return plate;
        }
        return StringUtil.mask(plate, 2, 1, '*');
    }
}
