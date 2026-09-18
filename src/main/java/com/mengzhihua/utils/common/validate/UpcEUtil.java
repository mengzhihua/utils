package com.mengzhihua.utils.common.validate;

/**
 * UPC-E expand / compress (GS1). Sample {@code 04252614} → UPC-A {@code 042100005264}.
 */
public final class UpcEUtil {

    private UpcEUtil() {
    }

    public static boolean isValid(String code) {
        String digits = EanUtil.normalize(code);
        if (digits.length() == 8 && (digits.charAt(0) == '0' || digits.charAt(0) == '1')) {
            String upcA = expand(digits);
            return upcA != null && EanUtil.isValid(upcA) && upcA.charAt(11) == digits.charAt(7);
        }
        return false;
    }

    public static String expand(String upcE) {
        String digits = EanUtil.normalize(upcE);
        if (digits.length() != 8) {
            throw new IllegalArgumentException("UPC-E must be 8 digits");
        }
        char ns = digits.charAt(0);
        if (ns != '0' && ns != '1') {
            throw new IllegalArgumentException("UPC-E number system must be 0 or 1");
        }
        String body = digits.substring(1, 7);
        char last = body.charAt(5);
        String manufacturer;
        String product;
        if (last >= '0' && last <= '2') {
            manufacturer = body.substring(0, 2) + last + "00";
            product = "00" + body.substring(2, 5);
        } else if (last == '3') {
            manufacturer = body.substring(0, 3) + "00";
            product = "000" + body.substring(3, 5);
        } else if (last == '4') {
            manufacturer = body.substring(0, 4) + "0";
            product = "0000" + body.charAt(4);
        } else {
            manufacturer = body.substring(0, 5);
            product = "0000" + last;
        }
        String upcABody = ns + manufacturer + product;
        return upcABody + EanUtil.checkDigit(upcABody);
    }

    public static String complete(String body7) {
        String digits = EanUtil.normalize(body7);
        if (!digits.matches("[01]\\d{6}")) {
            throw new IllegalArgumentException("UPC-E body must be number system plus 6 digits");
        }
        String expanded = expand(digits + "0");
        return digits + expanded.charAt(11);
    }

    public static String normalize(String code) {
        return EanUtil.normalize(code);
    }
}
