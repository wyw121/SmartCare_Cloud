package com.smartcare.cloud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartcare.cloud.vo.ResponseResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 数据库管理控制器（开发环境专用）
 * 
 * 整合了以下功能：
 * 1. 数据库表初始化
 * 2. 测试数据插入
 * 3. 表结构维护修复
 * 4. 数据库状态检查
 * 
 * 原控制器：
 * - InitController
 * - DatabaseInitController  
 * - DatabaseMaintenanceController
 * - TempDbInitController
 * - DatabaseTestController
 *
 * @author SmartCare Team
 * @date 2024-01-27
 */
@Profile("dev")
@Tag(name = "数据库管理", description = "数据库初始化、维护、测试相关接口（仅开发环境）")
@RestController
@RequestMapping("/api/database-management")
public class DatabaseManagementController {

    private static final Logger log = LoggerFactory.getLogger(DatabaseManagementController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // ========================================
    // 系统用户表管理
    // ========================================

    /**
     * 初始化用户表
     */
    @Operation(summary = "初始化系统用户表")
    @PostMapping("/init/user-tables")
    public ResponseResult<Map<String, Object>> initUserTables() {
        try {
            log.info("开始初始化用户表");
            StringBuilder logBuilder = new StringBuilder();
            int executedCount = 0;

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
                    logBuilder.append("✓ ").append(sql.substring(0, Math.min(50, sql.length()))).append("...\n");
                } catch (Exception e) {
                    logBuilder.append("✗ ").append(sql.substring(0, Math.min(50, sql.length()))).append("... 错误: ").append(e.getMessage()).append("\n");
                }
            }

            // 验证
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM sys_user", Integer.class);
            logBuilder.append("\n验证完成，sys_user表当前记录数: ").append(count);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("executedCount", executedCount);
            result.put("totalCount", sqlStatements.length);
            result.put("details", logBuilder.toString());

            log.info("用户表初始化完成");
            return ResponseResult.success(result);

        } catch (Exception e) {
            log.error("用户表初始化失败", e);
            return ResponseResult.error("初始化失败: " + e.getMessage());
        }
    }

    // ========================================
    // 医生表管理
    // ========================================

    /**
     * 创建医生表
     */
    @Operation(summary = "创建医生表")
    @PostMapping("/init/doctor-table")
    public ResponseResult<String> createDoctorTable() {
        try {
            log.info("开始创建医生表");
            String createTableSql = "CREATE TABLE IF NOT EXISTS `t_doctor` ("
                    + "`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',"
                    + "`employee_number` VARCHAR(50) NOT NULL COMMENT '医生工号',"
                    + "`name` VARCHAR(50) NOT NULL COMMENT '医生姓名',"
                    + "`gender` VARCHAR(10) NOT NULL COMMENT '性别',"
                    + "`age` INT(3) NULL COMMENT '年龄',"
                    + "`phone` VARCHAR(20) NOT NULL COMMENT '联系电话',"
                    + "`email` VARCHAR(100) NULL COMMENT '邮箱地址',"
                    + "`department` VARCHAR(100) NOT NULL COMMENT '科室',"
                    + "`title` VARCHAR(50) NOT NULL COMMENT '职称',"
                    + "`specialization` TEXT NULL COMMENT '专业特长',"
                    + "`experience_years` INT(2) NULL COMMENT '从业年限',"
                    + "`hospital_id` BIGINT(20) NULL COMMENT '所属医院ID',"
                    + "`hospital_name` VARCHAR(100) NULL COMMENT '所属医院名称',"
                    + "`license_number` VARCHAR(50) NULL COMMENT '执业证书号',"
                    + "`status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态 (1-在职, 0-离职)',"
                    + "`avatar` VARCHAR(255) NULL COMMENT '头像URL',"
                    + "`remark` TEXT NULL COMMENT '备注',"
                    + "`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "PRIMARY KEY (`id`),"
                    + "UNIQUE KEY `uk_employee_number` (`employee_number`),"
                    + "KEY `idx_name` (`name`),"
                    + "KEY `idx_department` (`department`),"
                    + "KEY `idx_title` (`title`),"
                    + "KEY `idx_phone` (`phone`)"
                    + ") ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '医生信息表'";

            jdbcTemplate.execute(createTableSql);
            log.info("医生表创建成功");
            return ResponseResult.success("医生表创建成功");
        } catch (Exception e) {
            log.error("医生表创建失败", e);
            return ResponseResult.error("医生表创建失败: " + e.getMessage());
        }
    }

    /**
     * 插入医生测试数据
     */
    @Operation(summary = "插入医生测试数据")
    @PostMapping("/init/doctor-data")
    public ResponseResult<String> insertDoctorData() {
        try {
            log.info("开始插入医生测试数据");
            String insertSql = "INSERT IGNORE INTO `t_doctor` ("
                    + "`employee_number`, `name`, `gender`, `age`, `phone`, `email`, `department`, "
                    + "`title`, `specialization`, `experience_years`, `hospital_name`"
                    + ") VALUES "
                    + "('D001', '刘医生', '男', 45, '13700137001', 'dr.liu@hospital.com', '内科', '主任医师', '心血管疾病诊疗', 20, '北京市第一人民医院'),"
                    + "('D002', '张医生', '女', 38, '13700137002', 'dr.zhang@hospital.com', '妇科', '副主任医师', '妇科常见病诊疗', 15, '北京市第一人民医院'),"
                    + "('D003', '王医生', '男', 42, '13700137003', 'dr.wang@hospital.com', '外科', '主治医师', '普外科手术', 18, '北京市第二人民医院'),"
                    + "('D004', '李医生', '女', 35, '13700137004', 'dr.li@hospital.com', '儿科', '主治医师', '儿科常见病', 12, '北京市儿童医院'),"
                    + "('D005', '陈医生', '男', 50, '13700137005', 'dr.chen@hospital.com', '骨科', '主任医师', '骨科创伤与修复', 25, '北京市骨科医院')";

            int rows = jdbcTemplate.update(insertSql);
            log.info("医生测试数据插入完成，共{}条", rows);
            return ResponseResult.success("成功插入 " + rows + " 条医生数据");
        } catch (Exception e) {
            log.error("插入医生数据失败", e);
            return ResponseResult.error("插入医生数据失败: " + e.getMessage());
        }
    }

    // ========================================
    // 健康预警表管理
    // ========================================

    /**
     * 初始化健康预警表
     */
    @Operation(summary = "初始化健康预警表和测试数据")
    @PostMapping("/init/health-warning")
    public ResponseResult<String> initHealthWarningData() {
        try {
            log.info("开始初始化健康预警表");
            
            // 创建健康预警表
            String createTableSql = "CREATE TABLE IF NOT EXISTS `t_health_warning` ("
                    + "  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',"
                    + "  `elderly_id` bigint NOT NULL COMMENT '老人ID',"
                    + "  `elderly_name` varchar(50) NOT NULL COMMENT '老人姓名',"
                    + "  `warning_type` varchar(50) NOT NULL COMMENT '预警类型',"
                    + "  `warning_level` int NOT NULL COMMENT '预警级别(1-低风险,2-中风险,3-高风险,4-紧急)',"
                    + "  `title` varchar(200) NOT NULL COMMENT '预警标题',"
                    + "  `content` text COMMENT '预警内容描述',"
                    + "  `trigger_data` varchar(500) COMMENT '触发数据',"
                    + "  `data_source` varchar(100) COMMENT '数据来源',"
                    + "  `status` int NOT NULL DEFAULT '0' COMMENT '预警状态(0-未处理,1-已查看,2-处理中,3-已处理,4-已忽略)',"
                    + "  `handler_id` bigint DEFAULT NULL COMMENT '处理人ID',"
                    + "  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',"
                    + "  `handle_time` datetime DEFAULT NULL COMMENT '处理时间',"
                    + "  `handle_remark` text COMMENT '处理备注',"
                    + "  `is_notified_family` tinyint DEFAULT '0' COMMENT '是否已通知家属(0-否,1-是)',"
                    + "  `is_notified_doctor` tinyint DEFAULT '0' COMMENT '是否已通知医生(0-否,1-是)',"
                    + "  `trigger_time` datetime NOT NULL COMMENT '预警触发时间',"
                    + "  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',"
                    + "  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',"
                    + "  `remark` varchar(500) DEFAULT NULL COMMENT '备注',"
                    + "  PRIMARY KEY (`id`),"
                    + "  KEY `idx_elderly_id` (`elderly_id`),"
                    + "  KEY `idx_warning_type` (`warning_type`),"
                    + "  KEY `idx_warning_level` (`warning_level`),"
                    + "  KEY `idx_status` (`status`),"
                    + "  KEY `idx_trigger_time` (`trigger_time`)"
                    + ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='健康预警表'";

            jdbcTemplate.execute(createTableSql);
            log.info("健康预警表创建成功");

            // 清理并重置
            jdbcTemplate.execute("DELETE FROM t_health_warning WHERE id > 0");
            jdbcTemplate.execute("ALTER TABLE t_health_warning AUTO_INCREMENT = 1");

            // 插入测试数据
            String[] insertSqls = {
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES "
                + "(1, '张老太', '血压异常', 3, '高血压紧急预警', '检测到张老太血压持续偏高，收缩压186mmHg，舒张压102mmHg，已超过安全范围', '收缩压:186mmHg,舒张压:102mmHg', '智能血压计', 0, '2024-01-27 09:15:32')",
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES "
                + "(2, '李大爷', '心率异常', 2, '心率偏快预警', '李大爷心率持续偏快，当前心率105次/分钟，建议关注', '心率:105次/分钟', '智能手环', 1, '2024-01-27 08:30:15')",
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES "
                + "(3, '王奶奶', '血糖异常', 4, '血糖危险预警', '王奶奶空腹血糖严重偏高，当前值12.8mmol/L，需要立即就医', '空腹血糖:12.8mmol/L', '血糖仪', 0, '2024-01-27 07:45:20')",
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, handler_id, handler_name, handle_time, trigger_time) VALUES "
                + "(4, '赵老伯', '长期不活动', 2, '活动量不足预警', '赵老伯今日活动量明显不足，步数仅88步，建议增加活动', '步数:88步,活动时长:15分钟', '智能手环', 3, 101, '护士小李', '2024-01-27 10:20:00', '2024-01-27 06:00:00')",
                "INSERT INTO t_health_warning (elderly_id, elderly_name, warning_type, warning_level, title, content, trigger_data, data_source, status, trigger_time) VALUES "
                + "(5, '陈老师', '体温异常', 3, '发热预警', '陈老师体温偏高，当前体温38.2°C，持续监测中', '体温:38.2°C', '智能体温计', 2, '2024-01-27 05:30:45')"
            };

            for (String sql : insertSqls) {
                jdbcTemplate.execute(sql);
            }

            log.info("健康预警测试数据插入完成，共5条记录");
            return ResponseResult.success("健康预警表和测试数据初始化成功！");
        } catch (Exception e) {
            log.error("初始化健康预警数据失败", e);
            return ResponseResult.error("初始化失败：" + e.getMessage());
        }
    }

    // ========================================
    // 健康记录表管理
    // ========================================

    /**
     * 创建健康记录表
     */
    @Operation(summary = "创建健康记录表并插入测试数据")
    @PostMapping("/init/health-record")
    public ResponseResult<String> createHealthRecordTable() {
        try {
            log.info("开始创建健康记录表");
            
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

            log.info("健康记录表创建并初始化完成");
            return ResponseResult.success("健康记录表创建并初始化完成");
        } catch (Exception e) {
            log.error("创建健康记录表失败", e);
            return ResponseResult.error("创建表失败：" + e.getMessage());
        }
    }

    // ========================================
    // 数据库维护功能
    // ========================================

    /**
     * 修复健康记录表结构
     */
    @Operation(summary = "修复健康记录表结构（添加缺失字段）")
    @PostMapping("/maintenance/fix-health-record")
    public ResponseResult<String> fixHealthRecordTable() {
        try {
            log.info("开始修复健康记录表结构");
            
            // 添加收缩压字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN systolic_pressure INT COMMENT '收缩压(mmHg)'");
            } catch (Exception e) {
                // 字段可能已存在
            }

            // 添加舒张压字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN diastolic_pressure INT COMMENT '舒张压(mmHg)'");
            } catch (Exception e) {
                // 字段可能已存在
            }

            // 添加心率字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN heart_rate INT COMMENT '心率(次/分钟)'");
            } catch (Exception e) {
                // 字段可能已存在
            }

            // 添加体温字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN body_temperature DECIMAL(4,2) COMMENT '体温(°C)'");
            } catch (Exception e) {
                // 字段可能已存在
            }

            // 添加血糖字段
            try {
                jdbcTemplate.execute("ALTER TABLE health_record ADD COLUMN blood_glucose DECIMAL(5,2) COMMENT '血糖(mmol/L)'");
            } catch (Exception e) {
                // 字段可能已存在
            }

            log.info("健康记录表结构修复完成");
            return ResponseResult.success("健康记录表结构修复完成！");
        } catch (Exception e) {
            log.error("修复健康记录表失败", e);
            return ResponseResult.error("修复失败：" + e.getMessage());
        }
    }

    // ========================================
    // 数据库状态检查
    // ========================================

    /**
     * 检查所有表
     */
    @Operation(summary = "检查数据库中的所有表")
    @GetMapping("/check/tables")
    public ResponseResult<List<String>> checkTables() {
        try {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = DATABASE()";
            List<String> tables = jdbcTemplate.queryForList(sql, String.class);
            log.info("数据库中共有{}个表", tables.size());
            return ResponseResult.success(tables);
        } catch (Exception e) {
            log.error("查询表失败", e);
            return ResponseResult.error("查询表失败：" + e.getMessage());
        }
    }

    /**
     * 检查医生表状态
     */
    @Operation(summary = "检查医生表是否存在及数据量")
    @GetMapping("/check/doctor-table")
    public ResponseResult<String> checkDoctorTable() {
        try {
            String checkSql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = 't_doctor'";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class);

            if (count != null && count > 0) {
                String dataSql = "SELECT COUNT(*) FROM t_doctor";
                Integer dataCount = jdbcTemplate.queryForObject(dataSql, Integer.class);
                return ResponseResult.success("医生表已存在，当前有 " + dataCount + " 条数据");
            } else {
                return ResponseResult.success("医生表不存在");
            }
        } catch (Exception e) {
            log.error("检查医生表失败", e);
            return ResponseResult.error("检查医生表失败: " + e.getMessage());
        }
    }

    /**
     * 健康检查
     */
    @Operation(summary = "数据库连接健康检查")
    @GetMapping("/health")
    public ResponseResult<String> healthCheck() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return ResponseResult.success("数据库连接正常");
        } catch (Exception e) {
            log.error("数据库连接异常", e);
            return ResponseResult.error("数据库连接异常: " + e.getMessage());
        }
    }
}
