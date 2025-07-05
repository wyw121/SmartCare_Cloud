package com.smartcare.cloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.util.PasswordUtil;

/**
 * 临时用户添加控制器 - 仅用于开发环境
 */
@RestController
@RequestMapping("/api/temp")
public class TempUserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/add-test-users")
    public ResponseEntity<Map<String, Object>> addTestUsers() {
        try {
            StringBuilder logBuilder = new StringBuilder();
            int addedCount = 0;

            // BCrypt加密的123456
            String password = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm";

            String[] insertSqls = {
                String.format(
                    "INSERT IGNORE INTO sys_user (username, password, name, gender, phone, email, role_code, role_name, status) VALUES " +
                    "('%s', '%s', '%s', %d, '%s', '%s', '%s', '%s', %d)",
                    "doctor", password, "测试医生", 1, "13800001001", "test.doctor@smartcare.com", "doctor", "医生", 1
                ),
                String.format(
                    "INSERT IGNORE INTO sys_user (username, password, name, gender, phone, email, role_code, role_name, status) VALUES " +
                    "('%s', '%s', '%s', %d, '%s', '%s', '%s', '%s', %d)",
                    "family", password, "测试家属", 0, "13800001002", "test.family@smartcare.com", "family", "家属", 1
                )
            };

            for (String sql : insertSqls) {
                try {
                    int affected = jdbcTemplate.update(sql);
                    if (affected > 0) {
                        addedCount++;
                        logBuilder.append("成功添加用户\n");
                    } else {
                        logBuilder.append("用户已存在，跳过\n");
                    }
                } catch (Exception e) {
                    logBuilder.append("添加失败: ").append(e.getMessage()).append("\n");
                }
            }

            // 验证用户
            try {
                Integer totalCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user", Integer.class);
                logBuilder.append("当前用户总数: ").append(totalCount).append("\n");

                // 查询测试用户
                String testUsersQuery = "SELECT username, real_name, role_code FROM sys_user WHERE username IN ('admin', 'doctor', 'family')";
                jdbcTemplate.query(testUsersQuery, (rs) -> {
                    logBuilder.append("用户: ").append(rs.getString("username"))
                             .append(" (").append(rs.getString("real_name"))
                             .append(", ").append(rs.getString("role_code"))
                             .append(")\n");
                });
            } catch (Exception e) {
                logBuilder.append("查询验证失败: ").append(e.getMessage()).append("\n");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "测试用户添加完成，新增 " + addedCount + " 个用户");
            result.put("details", logBuilder.toString());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "添加测试用户失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @GetMapping("/list-users")
    public ResponseEntity<Map<String, Object>> listUsers() {
        try {
            String sql = "SELECT id, username, real_name, role_code, role_name, status, create_time FROM sys_user ORDER BY create_time DESC";
            List<Map<String, Object>> users = jdbcTemplate.queryForList(sql);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", users);
            result.put("message", "获取用户列表成功");
            result.put("total", users.size());
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "获取用户列表失败: " + e.getMessage());
            return ResponseEntity.ok(result);
        }
    }

    @GetMapping("/encrypt-password")
    public ResponseEntity<Map<String, Object>> encryptPassword(@RequestParam String password) {
        try {
            PasswordUtil passwordUtil = new PasswordUtil();
            String encodedPassword = passwordUtil.encode(password);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("originalPassword", password);
            result.put("encodedPassword", encodedPassword);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "密码加密失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    @PostMapping("/update-passwords")
    public ResponseEntity<Map<String, Object>> updatePasswords() {
        try {
            PasswordUtil passwordUtil = new PasswordUtil();
            String encodedPassword = passwordUtil.encode("123456");
            
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append("更新密码开始...\n");
            
            // 更新admin密码
            int adminUpdated = jdbcTemplate.update(
                "UPDATE sys_user SET password = ? WHERE username = 'admin'", 
                encodedPassword
            );
            logBuilder.append("Admin密码更新: ").append(adminUpdated > 0 ? "成功" : "失败").append("\n");
            
            // 更新doctor密码
            int doctorUpdated = jdbcTemplate.update(
                "UPDATE sys_user SET password = ? WHERE username = 'doctor'", 
                encodedPassword
            );
            logBuilder.append("Doctor密码更新: ").append(doctorUpdated > 0 ? "成功" : "失败").append("\n");
            
            // 更新family密码
            int familyUpdated = jdbcTemplate.update(
                "UPDATE sys_user SET password = ? WHERE username = 'family'", 
                encodedPassword
            );
            logBuilder.append("Family密码更新: ").append(familyUpdated > 0 ? "成功" : "失败").append("\n");
            
            logBuilder.append("加密后的密码: ").append(encodedPassword).append("\n");
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "密码更新完成");
            result.put("details", logBuilder.toString());
            result.put("encodedPassword", encodedPassword);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "更新密码失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}
