package com.mengzhihua.utils.common.crypto;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Password strength scoring. Use a dedicated KDF (BCrypt/Argon2) for storage — this does not hash.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static int score(String password) {
        if (StringUtil.isEmpty(password)) {
            return 0;
        }
        int score = 0;
        if (password.length() >= 8) {
            score++;
        }
        if (password.length() >= 12) {
            score++;
        }
        if (password.chars().anyMatch(Character::isLowerCase)) {
            score++;
        }
        if (password.chars().anyMatch(Character::isUpperCase)) {
            score++;
        }
        if (password.chars().anyMatch(Character::isDigit)) {
            score++;
        }
        if (password.chars().anyMatch(ch -> !Character.isLetterOrDigit(ch))) {
            score++;
        }
        return score;
    }

    public static boolean isStrong(String password) {
        return score(password) >= 5;
    }

    public static String level(String password) {
        int value = score(password);
        if (value <= 2) {
            return "weak";
        }
        if (value <= 4) {
            return "medium";
        }
        return "strong";
    }

    public static String generate(int length) {
        int size = Math.min(64, Math.max(8, length));
        final String alphabet = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789!@#$%^&*";
        char[] chars = new char[size];
        java.security.SecureRandom random = new java.security.SecureRandom();
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
        for (int i = 0; i < size; i++) {
            char c = alphabet.charAt(random.nextInt(alphabet.length()));
            chars[i] = c;
            hasLower |= Character.isLowerCase(c);
            hasUpper |= Character.isUpperCase(c);
            hasDigit |= Character.isDigit(c);
            hasSymbol |= !Character.isLetterOrDigit(c);
        }
        if (!(hasLower && hasUpper && hasDigit && hasSymbol)) {
            chars[0] = 'a';
            chars[1] = 'A';
            chars[2] = '7';
            chars[3] = '!';
            for (int i = 0; i < size; i++) {
                int j = random.nextInt(size);
                char tmp = chars[i];
                chars[i] = chars[j];
                chars[j] = tmp;
            }
        }
        return new String(chars);
    }
}
