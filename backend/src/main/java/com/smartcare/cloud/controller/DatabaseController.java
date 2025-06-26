package com.smartcare.cloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.util.DatabaseScriptExecutor;
import com.smartcare.cloud.vo.ResponseResult;

/**
 * 数据库管理控制器 用于数据库结构优化和维护
 *
 * @author SmartCare
 */
@RestController
@RequestMapping("/api/admin/database")
@CrossOrigin
public class DatabaseController {

    @Autowired
    private DatabaseScriptExecutor scriptExecutor;

    /**
     * 执行数据库结构优化
     */
    @PostMapping("/optimize")
    public ResponseResult<String> optimizeDatabase() {
        try {
            scriptExecutor.executeScript("sql/optimize-elderly-database.sql");
            return ResponseResult.success("数据库结构优化完成");
        } catch (Exception e) {
            return ResponseResult.error("数据库结构优化失败: " + e.getMessage());
        }
    }

    /**
     * 检查表结构
     */
    @GetMapping("/check")
    public ResponseResult<Map<String, Object>> checkDatabase() {
        Map<String, Object> result = new HashMap<>();

        try {
            // 检查主要表是否存在
            result.put("elderly_table_exists", scriptExecutor.tableExists("elderly"));
            result.put("elderly_health_record_exists", scriptExecutor.tableExists("elderly_health_record"));
            result.put("elderly_care_assessment_exists", scriptExecutor.tableExists("elderly_care_assessment"));
            result.put("elderly_service_record_exists", scriptExecutor.tableExists("elderly_service_record"));

            // 检查elderly表的关键字段是否存在
            if (scriptExecutor.tableExists("elderly")) {
                result.put("care_level_exists", scriptExecutor.columnExists("elderly", "care_level"));
                result.put("blood_type_exists", scriptExecutor.columnExists("elderly", "blood_type"));
                result.put("adl_score_exists", scriptExecutor.columnExists("elderly", "adl_score"));
                result.put("room_number_exists", scriptExecutor.columnExists("elderly", "room_number"));

                // 获取elderly表结构
                List<String> structure = scriptExecutor.getTableStructure("elderly");
                result.put("elderly_structure", structure);
            }

            return ResponseResult.success(result);
        } catch (Exception e) {
            return ResponseResult.error("数据库检查失败: " + e.getMessage());
        }
    }

    /**
     * 获取表结构信息
     */
    @GetMapping("/structure/{tableName}")
    public ResponseResult<List<String>> getTableStructure(@PathVariable String tableName) {
        try {
            if (!scriptExecutor.tableExists(tableName)) {
                return ResponseResult.error("表 " + tableName + " 不存在");
            }

            List<String> structure = scriptExecutor.getTableStructure(tableName);
            return ResponseResult.success(structure);
        } catch (Exception e) {
            return ResponseResult.error("获取表结构失败: " + e.getMessage());
        }
    }

    /**
     * 执行自定义SQL脚本
     */
    @PostMapping("/execute")
    public ResponseResult<String> executeSql(@RequestBody Map<String, String> request) {
        try {
            String sql = request.get("sql");
            if (sql == null || sql.trim().isEmpty()) {
                return ResponseResult.error("SQL脚本不能为空");
            }

            // 安全检查：只允许CREATE、ALTER、INSERT、UPDATE、SELECT语句
            String upperSql = sql.trim().toUpperCase();
            if (!upperSql.startsWith("CREATE")
                    && !upperSql.startsWith("ALTER")
                    && !upperSql.startsWith("INSERT")
                    && !upperSql.startsWith("UPDATE")
                    && !upperSql.startsWith("SELECT")) {
                return ResponseResult.error("只允许执行CREATE、ALTER、INSERT、UPDATE、SELECT语句");
            }

            scriptExecutor.executeSqlScript(sql);
            return ResponseResult.success("SQL脚本执行完成");
        } catch (Exception e) {
            return ResponseResult.error("SQL脚本执行失败: " + e.getMessage());
        }
    }
}
