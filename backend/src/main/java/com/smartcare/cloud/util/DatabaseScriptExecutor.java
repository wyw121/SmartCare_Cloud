package com.smartcare.cloud.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

/**
 * 数据库脚本执行工具类 用于执行SQL脚本文件
 *
 * @author SmartCare
 */
@Component
public class DatabaseScriptExecutor {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 执行SQL脚本文件
     *
     * @param scriptPath 脚本文件路径（classpath相对路径）
     * @throws IOException 文件读取异常
     */
    public void executeScript(String scriptPath) throws IOException {
        ClassPathResource resource = new ClassPathResource(scriptPath);
        try (InputStream inputStream = resource.getInputStream()) {
            String script = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            executeSqlScript(script);
        }
    }

    /**
     * 执行SQL脚本内容
     *
     * @param script SQL脚本内容
     */
    public void executeSqlScript(String script) {
        // 去除注释和空行，按分号分割SQL语句
        String[] statements = script
                .replaceAll("--.*?\n", "\n") // 去除单行注释
                .replaceAll("/\\*.*?\\*/", "") // 去除多行注释
                .split(";");

        for (String statement : statements) {
            String trimmed = statement.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                try {
                    jdbcTemplate.execute(trimmed);
                    System.out.println("执行SQL成功: "
                            + (trimmed.length() > 50 ? trimmed.substring(0, 50) + "..." : trimmed));
                } catch (Exception e) {
                    System.err.println("执行SQL失败: " + trimmed);
                    System.err.println("错误信息: " + e.getMessage());
                    // 继续执行其他语句，不中断
                }
            }
        }
    }

    /**
     * 检查表是否存在
     *
     * @param tableName 表名
     * @return 是否存在
     */
    public boolean tableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.tables "
                    + "WHERE table_schema = DATABASE() AND table_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 检查列是否存在
     *
     * @param tableName 表名
     * @param columnName 列名
     * @return 是否存在
     */
    public boolean columnExists(String tableName, String columnName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.columns "
                    + "WHERE table_schema = DATABASE() AND table_name = ? AND column_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName, columnName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取表结构信息
     *
     * @param tableName 表名
     * @return 表结构信息
     */
    public List<String> getTableStructure(String tableName) {
        String sql = "DESCRIBE " + tableName;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return String.format("%-20s %-20s %-10s %-10s %-10s %-20s",
                    rs.getString("Field"),
                    rs.getString("Type"),
                    rs.getString("Null"),
                    rs.getString("Key"),
                    rs.getString("Default"),
                    rs.getString("Extra"));
        });
    }
}
