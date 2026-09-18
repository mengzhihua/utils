package com.mengzhihua.utils.common.validate;

/**
 * Brazilian CPF (Cadastro de Pessoas Físicas). Sample {@code 111.444.777-35}.
 */
public final class CpfUtil {

    private CpfUtil() {
    }

    public static boolean isValid(String cpf) {
        String digits = normalize(cpf);
        if (!digits.matches("\\d{11}") || sameDigits(digits)) {
            return false;
        }
        return checkDigit(digits.substring(0, 9)) == digits.charAt(9)
                && checkDigit(digits.substring(0, 10)) == digits.charAt(10);
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() != 9 && digits.length() != 10) {
            throw new IllegalArgumentException("CPF body must be 9 or 10 digits");
        }
        int weight = digits.length() + 1;
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            sum += (digits.charAt(i) - '0') * (weight - i);
        }
        int rem = sum % 11;
        return (char) ('0' + (rem < 2 ? 0 : 11 - rem));
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("CPF body must be 9 digits");
        }
        char first = checkDigit(digits);
        return digits + first + checkDigit(digits + first);
    }

    public static String normalize(String cpf) {
        return cpf == null ? "" : cpf.replaceAll("\\D", "");
    }

    private static boolean sameDigits(String digits) {
        char first = digits.charAt(0);
        for (int i = 1; i < digits.length(); i++) {
            if (digits.charAt(i) != first) {
                return false;
            }
        }
        return true;
    }
}
