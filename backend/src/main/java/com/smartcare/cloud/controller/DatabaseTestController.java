package com.smartcare.cloud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.vo.ResponseResult;

/**
 * 数据库测试控制器
 */
@RestController
@RequestMapping("/test")
public class DatabaseTestController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/create-health-record-table")
    public ResponseResult<String> createHealthRecordTable() {
        try {
            // 删除旧表
            jdbcTemplate.execute("DROP TABLE IF EXISTS health_record");

            // 创建新表
            String createTableSql = "CREATE TABLE `health_record` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT,"
                    + "`elderly_id` BIGINT(20) NOT NULL,"
                    + "`doctor_id` BIGINT(20) NULL,"
                    + "`record_type` INT(4) NOT NULL DEFAULT 1,"
                    + "`record_date` VARCHAR(20) NOT NULL,"
                    + "`title` VARCHAR(100) NULL,"
                    + "`doctor_name` VARCHAR(50) NULL,"
                    + "`blood_pressure_high` INT(6) NULL,"
                    + "`blood_pressure_low` INT(6) NULL,"
                    + "`heart_rate` INT(6) NULL,"
                    + "`temperature` DECIMAL(4,1) NULL,"
                    + "`blood_sugar` DECIMAL(5,2) NULL,"
                    + "`weight` DECIMAL(5,2) NULL,"
                    + "`symptoms` TEXT NULL,"
                    + "`diagnosis` TEXT NULL,"
                    + "`treatment` TEXT NULL,"
                    + "`medication` TEXT NULL,"
                    + "`description` TEXT NULL,"
                    + "`risk_level` INT(2) NOT NULL DEFAULT 0,"
                    + "`is_abnormal` TINYINT(1) NOT NULL DEFAULT 0,"
                    + "`remarks` TEXT NULL,"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,"
                    + "`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,"
                    + "`create_by` VARCHAR(50) NULL,"
                    + "`update_by` VARCHAR(50) NULL,"
                    + "`is_deleted` TINYINT(1) NOT NULL DEFAULT 0,"
                    + "PRIMARY KEY (`id`),"
                    + "KEY `idx_elderly_id` (`elderly_id`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";

            jdbcTemplate.execute(createTableSql);

            // 插入测试数据
            String insertSql = "INSERT INTO `health_record` ("
                    + "`elderly_id`, `record_type`, `record_date`, `title`, `doctor_name`, "
                    + "`blood_pressure_high`, `blood_pressure_low`, `heart_rate`, `temperature`, "
                    + "`blood_sugar`, `weight`, `symptoms`, `diagnosis`, `treatment`, `medication`, "
                    + "`description`, `risk_level`, `is_abnormal`, `remarks`"
                    + ") VALUES "
                    + "(1, 1, '2024-01-15', '常规体检', '李医生', 120, 80, 75, 36.5, 5.2, 65.5, '无明显症状', '身体健康', '继续保持良好生活习惯', '无', '年度常规体检，各项指标正常', 0, 0, '建议每半年复查一次'), "
                    + "(1, 2, '2024-01-20', '感冒就诊', '王医生', 125, 82, 78, 37.2, NULL, NULL, '咳嗽、鼻塞、轻微发热', '普通感冒', '多休息，多喝水', '感冒清热颗粒，一日三次', '普通感冒症状，建议观察', 1, 1, '如症状加重请及时就医')";

            jdbcTemplate.execute(insertSql);

            return ResponseResult.success("健康记录表创建并初始化完成");
        } catch (Exception e) {
            return ResponseResult.error("创建表失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-tables")
    public ResponseResult<String> checkTables() {
        try {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()";
            java.util.List<String> tables = jdbcTemplate.queryForList(sql, String.class);
            return ResponseResult.success("数据库表：" + String.join(", ", tables));
        } catch (Exception e) {
            return ResponseResult.error("查询表失败：" + e.getMessage());
        }
    }
}
