package com.mengzhihua.utils.common.validate;


import java.util.Set;

import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.text.RegexUtil;

/**
 * 大陆手机号运营商粗分（号段会随发放变化，仅作业务辅助）。
 */
public final class PhoneUtil {

    private static final Set<String> CMCC = Set.of(
            "134", "135", "136", "137", "138", "139", "147", "148", "150", "151", "152",
            "157", "158", "159", "172", "178", "182", "183", "184", "187", "188", "195", "197", "198"
    );
    private static final Set<String> CUCC = Set.of(
            "130", "131", "132", "145", "146", "155", "156", "166", "175", "176", "185", "186", "196"
    );
    private static final Set<String> CTCC = Set.of(
            "133", "149", "153", "173", "174", "177", "180", "181", "189", "190", "191", "193", "199"
    );
    private static final Set<String> CBN = Set.of("192");
    private static final Set<String> MVNO = Set.of("162", "165", "167", "170", "171");

    private PhoneUtil() {
    }

    public static String carrier(String mobile) {
        if (!RegexUtil.isMobile(mobile)) {
            return "未知";
        }
        String prefix = mobile.substring(0, 3);
        if (MVNO.contains(prefix)) {
            return "虚拟运营商";
        }
        if (CMCC.contains(prefix)) {
            return "中国移动";
        }
        if (CUCC.contains(prefix)) {
            return "中国联通";
        }
        if (CTCC.contains(prefix)) {
            return "中国电信";
        }
        if (CBN.contains(prefix)) {
            return "中国广电";
        }
        return "未知";
    }

    public static boolean isVirtual(String mobile) {
        return "虚拟运营商".equals(carrier(mobile));
    }

    public static String hide(String mobile) {
        return StringUtil.maskPhone(digits(mobile));
    }

    public static boolean isMobileHk(String mobile) {
        String digits = digits(mobile);
        if (digits.startsWith("852")) {
            digits = digits.substring(3);
        }
        return digits.length() == 8 && "569".indexOf(digits.charAt(0)) >= 0 && digits.chars().allMatch(Character::isDigit);
    }

    public static boolean isMobileTw(String mobile) {
        String digits = digits(mobile);
        if (digits.startsWith("886")) {
            digits = digits.substring(3);
            if (digits.startsWith("0")) {
                digits = digits.substring(1);
            }
        }
        return digits.length() == 9 && digits.startsWith("9") && digits.chars().allMatch(Character::isDigit)
                || digits.length() == 10 && digits.startsWith("09") && digits.chars().allMatch(Character::isDigit);
    }

    public static boolean isMobileMo(String mobile) {
        String digits = digits(mobile);
        if (digits.startsWith("853")) {
            digits = digits.substring(3);
        }
        return digits.length() == 8 && digits.startsWith("6") && digits.chars().allMatch(Character::isDigit);
    }

    public static boolean isTel400(String value) {
        String digits = digits(value);
        return digits.length() == 10 && digits.startsWith("400");
    }

    public static String region(String mobile) {
        if (RegexUtil.isMobile(mobile)) {
            return "CN";
        }
        if (isMobileHk(mobile)) {
            return "HK";
        }
        if (isMobileTw(mobile)) {
            return "TW";
        }
        if (isMobileMo(mobile)) {
            return "MO";
        }
        return "未知";
    }

    private static String digits(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(value.length());
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c >= '0' && c <= '9') {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
