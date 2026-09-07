package com.mengzhihua.utils.util;

import java.util.regex.Pattern;

/**
 * Common format validators.
 */
public final class RegexUtil {

    public static final Pattern MOBILE = Pattern.compile("^1[3-9]\\d{9}$");
    public static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final Pattern ID_CARD = Pattern.compile("^\\d{17}[\\dXx]$");
    public static final Pattern IPV4 = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$");
    public static final Pattern URL = Pattern.compile("^(https?://)[\\w.-]+(?:\\.[\\w.-]+)+(?:[/#?].*)?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern USERNAME = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{3,31}$");
    public static final Pattern CREDIT_CODE = Pattern.compile("^[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}$");
    public static final Pattern PLATE = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-HJ-NP-Z][A-HJ-NP-Z0-9]{4,6}[A-HJ-NP-Z0-9挂学警港澳]$");
    public static final Pattern IPV6 = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
    public static final Pattern ZIPCODE = Pattern.compile("^\\d{6}$");
    public static final Pattern QQ = Pattern.compile("^[1-9]\\d{4,11}$");
    public static final Pattern LANDLINE = Pattern.compile("^0\\d{2,3}-?\\d{7,8}$");

    private RegexUtil() {
    }

    public static boolean isMatch(Pattern pattern, String value) {
        return value != null && pattern != null && pattern.matcher(value).matches();
    }

    public static boolean isMobile(String value) {
        return isMatch(MOBILE, value);
    }

    public static boolean isEmail(String value) {
        return isMatch(EMAIL, value);
    }

    public static boolean isIdCard(String value) {
        return isMatch(ID_CARD, value);
    }

    public static boolean isIpv4(String value) {
        return isMatch(IPV4, value);
    }

    public static boolean isUrl(String value) {
        return isMatch(URL, value);
    }

    public static boolean isUsername(String value) {
        return isMatch(USERNAME, value);
    }

    public static boolean isCreditCode(String value) {
        return CreditCodeUtil.isValid(value);
    }

    public static boolean isPlate(String value) {
        return isMatch(PLATE, value);
    }

    public static boolean isIpv6(String value) {
        return isMatch(IPV6, value);
    }

    public static boolean isZipcode(String value) {
        return isMatch(ZIPCODE, value);
    }
}
