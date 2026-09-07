package com.mengzhihua.utils.util;

/**
 * Bank card Luhn check and masking.
 */
public final class BankCardUtil {

    private BankCardUtil() {
    }

    public static boolean isValid(String cardNo) {
        String digits = normalize(cardNo);
        if (digits.length() < 12 || digits.length() > 19) {
            return false;
        }
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = digits.length() - 1; i >= 0; i--) {
            int n = Character.digit(digits.charAt(i), 10);
            if (doubleDigit) {
                n *= 2;
                if (n > 9) {
                    n -= 9;
                }
            }
            sum += n;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }

    public static String mask(String cardNo) {
        return DesensitizeUtil.bankCard(normalize(cardNo));
    }

    private static String normalize(String cardNo) {
        return cardNo == null ? "" : cardNo.replaceAll("\\s+", "");
    }
}
