package com.smartcare.cloud.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统初始化控制器
 */
@RestController
@RequestMapping("/api/init")
public class InitController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/user-tables")
    public ResponseEntity<Map<String, Object>> initUserTables() {
        try {
            StringBuilder logBuilder = new StringBuilder();
            int executedCount = 0;

            // 直接执行SQL语句 - 简化版表结构
            String[] sqlStatements = {
                "DROP TABLE IF EXISTS sys_user",
                "CREATE TABLE sys_user ("
                + "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, "
                + "username VARCHAR(50) NOT NULL UNIQUE, "
                + "password VARCHAR(255) NOT NULL, "
                + "real_name VARCHAR(50) NOT NULL, "
                + "gender TINYINT(1) NULL, "
                + "phone VARCHAR(20) NULL UNIQUE, "
                + "email VARCHAR(100) NULL UNIQUE, "
                + "role_code VARCHAR(50) NOT NULL, "
                + "role_name VARCHAR(50) NOT NULL, "
                + "doctor_title VARCHAR(50) NULL, "
                + "doctor_speciality VARCHAR(200) NULL, "
                + "doctor_license_number VARCHAR(100) NULL, "
                + "doctor_experience_years INT NULL, "
                + "family_relationship VARCHAR(50) NULL, "
                + "family_elderly_ids VARCHAR(500) NULL, "
                + "status TINYINT(1) NOT NULL DEFAULT 1, "
                + "description VARCHAR(500) NULL, "
                + "create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "is_deleted TINYINT(1) NOT NULL DEFAULT 0"
                + ")",
                "INSERT INTO sys_user (username, password, real_name, gender, phone, email, role_code, role_name, status, description) VALUES "
                + "('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '系统管理员', 1, '13800000000', 'admin@smartcare.com', 'admin', '系统管理员', 1, '系统管理员账户')",
                "INSERT INTO sys_user (username, password, real_name, gender, phone, email, role_code, role_name, status, description) VALUES "
                + "('doctor01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '张医生', 1, '13800000001', 'doctor@smartcare.com', 'doctor', '医生', 1, '医生账户')",
                "INSERT INTO sys_user (username, password, real_name, gender, phone, email, role_code, role_name, status, description) VALUES "
                + "('family01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm', '家属用户', 0, '13800000002', 'family@smartcare.com', 'family', '家属', 1, '家属账户')"
            };

            for (String sql : sqlStatements) {
                try {
                    jdbcTemplate.execute(sql);
                    executedCount++;
                    logBuilder.append("执行成功: ").append(sql.substring(0, Math.min(50, sql.length()))).append("...\n");
                } catch (Exception e) {
                    logBuilder.append("执行失败: ").append(sql.substring(0, Math.min(50, sql.length()))).append("... 错误: ").append(e.getMessage()).append("\n");
                }
            }

            // 验证表是否创建成功
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user", Integer.class);
                logBuilder.append("sys_user表验证成功, 当前记录数: ").append(count).append("\n");
            } catch (Exception e) {
                logBuilder.append("表验证失败: ").append(e.getMessage()).append("\n");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "用户表初始化完成，执行了 " + executedCount + " 条SQL语句");
            result.put("details", logBuilder.toString());

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "用户表初始化失败: " + e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }
}
