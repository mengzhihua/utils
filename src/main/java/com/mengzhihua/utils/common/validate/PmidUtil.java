package com.mengzhihua.utils.common.validate;


/**
 * PubMed ID (PMID). 1–10 digits, no leading zero.
 */
public final class PmidUtil {

    private PmidUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-9]\\d{0,9}");
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
