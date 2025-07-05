package com.smartcare.cloud.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 密码工具类
 *
 * @author SmartCare Team
 * @since 2024-01-01
 */
@Component
public class PasswordUtil {

    private final BCryptPasswordEncoder passwordEncoder;

    public PasswordUtil() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * 密码加密
     *
     * @param rawPassword 原始密码
     * @return 加密后的密码
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /**
     * 密码验证
     *
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    /**
     * 检查密码强度
     *
     * @param password 密码
     * @return 强度等级：1-弱，2-中，3-强
     */
    public int checkPasswordStrength(String password) {
        if (password == null || password.length() < 6) {
            return 1; // 弱
        }

        int score = 0;

        // 长度检查
        if (password.length() >= 8) {
            score++;
        }

        // 包含数字
        if (password.matches(".*\\d.*")) {
            score++;
        }

        // 包含小写字母
        if (password.matches(".*[a-z].*")) {
            score++;
        }

        // 包含大写字母
        if (password.matches(".*[A-Z].*")) {
            score++;
        }

        // 包含特殊字符
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\|,.<>\\/?].*")) {
            score++;
        }

        if (score <= 2) {
            return 1; // 弱
        } else if (score <= 3) {
            return 2; // 中
        } else {
            return 3; // 强
        }
    }
}
