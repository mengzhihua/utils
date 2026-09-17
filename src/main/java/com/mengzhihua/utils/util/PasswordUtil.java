package com.mengzhihua.utils.util;

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
}
