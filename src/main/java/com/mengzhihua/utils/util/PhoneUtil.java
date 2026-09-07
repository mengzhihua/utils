package com.mengzhihua.utils.util;

import java.util.Set;

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
}
