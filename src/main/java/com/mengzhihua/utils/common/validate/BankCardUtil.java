package com.mengzhihua.utils.common.validate;


import com.mengzhihua.utils.common.text.DesensitizeUtil;

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

    /**
     * BIN brand guess (Visa / Mastercard / UnionPay / Amex / JCB / Discover).
     */
    public static String brand(String cardNo) {
        String digits = normalize(cardNo);
        if (digits.isEmpty() || !digits.chars().allMatch(Character::isDigit)) {
            return "未知";
        }
        if (digits.startsWith("4")) {
            return "Visa";
        }
        if (digits.startsWith("34") || digits.startsWith("37")) {
            return "American Express";
        }
        if (digits.startsWith("62")) {
            return "UnionPay";
        }
        if (digits.startsWith("35")) {
            return "JCB";
        }
        if (digits.startsWith("6011") || digits.startsWith("65")) {
            return "Discover";
        }
        int prefix2 = digits.length() >= 2 ? Integer.parseInt(digits.substring(0, 2)) : -1;
        if (prefix2 >= 51 && prefix2 <= 55) {
            return "Mastercard";
        }
        int prefix4 = digits.length() >= 4 ? Integer.parseInt(digits.substring(0, 4)) : -1;
        if (prefix4 >= 2221 && prefix4 <= 2720) {
            return "Mastercard";
        }
        return "未知";
    }

    private static String normalize(String cardNo) {
        return cardNo == null ? "" : cardNo.replaceAll("\\s+", "");
    }
}
