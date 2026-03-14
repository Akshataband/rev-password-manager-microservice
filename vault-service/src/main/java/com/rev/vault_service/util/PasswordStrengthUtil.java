package com.rev.vault_service.util;

public class PasswordStrengthUtil {

    public static boolean isWeak(String password) {

        if (password.length() < 8) {
            return true;
        }

        boolean hasUpper = password.matches(".*[A-Z].*");
        boolean hasLower = password.matches(".*[a-z].*");
        boolean hasNumber = password.matches(".*[0-9].*");
        boolean hasSpecial = password.matches(".*[@#$%^&+=!].*");

        return !(hasUpper && hasLower && hasNumber && hasSpecial);
    }
}