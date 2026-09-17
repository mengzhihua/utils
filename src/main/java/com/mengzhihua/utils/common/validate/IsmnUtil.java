package com.mengzhihua.utils.common.validate;

/**
 * ISO 10957 ISMN-13 (prefix {@code 979-0}, GS1 check digit). Sample {@code 979-0-2600-0043-8}.
 */
public final class IsmnUtil {

    private IsmnUtil() {
    }

    public static boolean isValid(String ismn) {
        String digits = normalize(ismn);
        return digits.startsWith("9790") && digits.length() == 13 && EanUtil.isValid(digits);
    }

    public static String complete(String body12) {
        String digits = normalize(body12);
        if (!digits.matches("9790\\d{8}")) {
            throw new IllegalArgumentException("ISMN body must be 9790 + 8 digits");
        }
        return digits + EanUtil.checkDigit(digits);
    }

    public static String normalize(String ismn) {
        return ismn == null ? "" : ismn.replaceAll("\\D", "");
    }
}
