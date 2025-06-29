package com.smartcare.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.smartcare.cloud.util.DatabaseScriptExecutor;

/**
 * 数据库初始化组件 在应用启动时自动执行数据库结构优化
 *
 * @author SmartCare
 */
@Component
@Order(1)
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private DatabaseScriptExecutor scriptExecutor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("=== 开始数据库结构检查和优化 ===");

        try {
            // 检查elderly表是否存在
            boolean elderlyExists = scriptExecutor.tableExists("elderly");
            System.out.println("elderly表存在: " + elderlyExists);

            if (elderlyExists) {
                // 检查是否需要添加扩展字段
                boolean emergencyContactExists = scriptExecutor.columnExists("elderly", "emergency_contact");
                boolean careLevelExists = scriptExecutor.columnExists("elderly", "care_level");
                boolean bloodTypeExists = scriptExecutor.columnExists("elderly", "blood_type");
                boolean adlScoreExists = scriptExecutor.columnExists("elderly", "adl_score");

                System.out.println("emergency_contact字段存在: " + emergencyContactExists);
                System.out.println("care_level字段存在: " + careLevelExists);
                System.out.println("blood_type字段存在: " + bloodTypeExists);
                System.out.println("adl_score字段存在: " + adlScoreExists);

                if (!emergencyContactExists || !careLevelExists || !bloodTypeExists || !adlScoreExists) {
                    System.out.println("检测到需要优化数据库结构，开始执行...");
                    scriptExecutor.executeScript("sql/optimize-elderly-database.sql");
                    System.out.println("数据库结构优化完成！");
                } else {
                    System.out.println("数据库结构已是最新，无需优化。");
                }
            } else {
                System.out.println("elderly表不存在，请先运行初始化脚本创建基础表结构。");
            }

            // 检查其他表是否存在
            boolean healthRecordExists = scriptExecutor.tableExists("health_record");
            boolean elderlyHealthRecordExists = scriptExecutor.tableExists("elderly_health_record");
            boolean careAssessmentExists = scriptExecutor.tableExists("elderly_care_assessment");
            boolean serviceRecordExists = scriptExecutor.tableExists("elderly_service_record");

            System.out.println("health_record表存在: " + healthRecordExists);
            System.out.println("elderly_health_record表存在: " + elderlyHealthRecordExists);
            System.out.println("elderly_care_assessment表存在: " + careAssessmentExists);
            System.out.println("elderly_service_record表存在: " + serviceRecordExists);

            // 创建health_record表（优先）
            if (!healthRecordExists) {
                System.out.println("创建health_record表...");
                createHealthRecordTable();
            }

            if (!elderlyHealthRecordExists || !careAssessmentExists || !serviceRecordExists) {
                System.out.println("检测到缺少相关表，开始创建...");
                scriptExecutor.executeScript("sql/optimize-elderly-database.sql");
                System.out.println("相关表创建完成！");
            }

        } catch (Exception e) {
            System.err.println("数据库结构检查失败: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== 数据库结构检查和优化完成 ===");
    }

    /**
     * 创建健康记录表
     */
    private void createHealthRecordTable() {
        try {
            String createTableSql = "CREATE TABLE IF NOT EXISTS `health_record` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',"
                    + "`elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',"
                    + "`doctor_id` BIGINT(20) NULL COMMENT '医生ID',"
                    + "`record_type` INT(4) NOT NULL DEFAULT 1 COMMENT '记录类型',"
                    + "`record_date` VARCHAR(20) NOT NULL COMMENT '记录日期',"
                    + "`title` VARCHAR(100) NULL COMMENT '记录标题',"
                    + "`doctor_name` VARCHAR(50) NULL COMMENT '医生姓名',"
                    + "`blood_pressure_high` INT(6) NULL COMMENT '收缩压',"
                    + "`blood_pressure_low` INT(6) NULL COMMENT '舒张压',"
                    + "`heart_rate` INT(6) NULL COMMENT '心率',"
                    + "`temperature` DECIMAL(4,1) NULL COMMENT '体温',"
                    + "`blood_sugar` DECIMAL(5,2) NULL COMMENT '血糖',"
                    + "`weight` DECIMAL(5,2) NULL COMMENT '体重',"
                    + "`symptoms` TEXT NULL COMMENT '症状描述',"
                    + "`diagnosis` TEXT NULL COMMENT '诊断结果',"
                    + "`treatment` TEXT NULL COMMENT '治疗方案',"
                    + "`medication` TEXT NULL COMMENT '用药信息',"
                    + "`description` TEXT NULL COMMENT '详细描述',"
                    + "`risk_level` INT(2) NOT NULL DEFAULT 0 COMMENT '风险等级',"
                    + "`is_abnormal` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否异常',"
                    + "`remarks` TEXT NULL COMMENT '备注',"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "`create_by` VARCHAR(50) NULL COMMENT '创建者',"
                    + "`update_by` VARCHAR(50) NULL COMMENT '更新者',"
                    + "`is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',"
                    + "PRIMARY KEY (`id`),"
                    + "KEY `idx_elderly_id` (`elderly_id`),"
                    + "KEY `idx_record_date` (`record_date`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康记录表'";

            scriptExecutor.executeSqlScript(createTableSql);
            System.out.println("health_record表创建成功");

            // 插入测试数据
            insertHealthRecordTestData();
        } catch (Exception e) {
            System.err.println("创建health_record表失败: " + e.getMessage());
        }
    }

    /**
     * 插入健康记录测试数据
     */
    private void insertHealthRecordTestData() {
        try {
            // 检查是否已有数据
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM health_record", Integer.class);
            if (count != null && count > 0) {
                System.out.println("health_record表已有数据，跳过初始化");
                return;
            }

            String insertSql = "INSERT INTO `health_record` ("
                    + "`elderly_id`, `record_type`, `record_date`, `title`, `doctor_name`, "
                    + "`blood_pressure_high`, `blood_pressure_low`, `heart_rate`, `temperature`, "
                    + "`blood_sugar`, `weight`, `symptoms`, `diagnosis`, `treatment`, `medication`, "
                    + "`description`, `risk_level`, `is_abnormal`, `remarks`"
                    + ") VALUES "
                    + "(1, 1, '2024-01-15', '常规体检', '李医生', 120, 80, 75, 36.5, 5.2, 65.5, '无明显症状', '身体健康', '继续保持良好生活习惯', '无', '年度常规体检，各项指标正常', 0, 0, '建议每半年复查一次'), "
                    + "(1, 2, '2024-01-20', '感冒就诊', '王医生', 125, 82, 78, 37.2, NULL, NULL, '咳嗽、鼻塞、轻微发热', '普通感冒', '多休息，多喝水', '感冒清热颗粒，一日三次', '普通感冒症状，建议观察', 1, 1, '如症状加重请及时就医')";

            scriptExecutor.executeSqlScript(insertSql);
            System.out.println("health_record测试数据插入成功");
        } catch (Exception e) {
            System.err.println("插入health_record测试数据失败: " + e.getMessage());
        }
    }
}
