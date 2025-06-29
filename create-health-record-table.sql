-- 创建健康记录表，与HealthRecord实体类匹配
USE smartcare_cloud;
-- 删除旧表
DROP TABLE IF EXISTS `health_record`;
-- 创建新的健康记录表
CREATE TABLE `health_record` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',
  `doctor_id` BIGINT(20) NULL COMMENT '医生ID',
  `record_type` INT(4) NOT NULL DEFAULT 1 COMMENT '记录类型 (1:体检记录 2:就诊记录 3:用药记录 4:监测记录)',
  `record_date` VARCHAR(20) NOT NULL COMMENT '记录日期',
  `title` VARCHAR(100) NULL COMMENT '记录标题',
  `doctor_name` VARCHAR(50) NULL COMMENT '医生姓名',
  `blood_pressure_high` INT(6) NULL COMMENT '收缩压',
  `blood_pressure_low` INT(6) NULL COMMENT '舒张压',
  `heart_rate` INT(6) NULL COMMENT '心率',
  `temperature` DECIMAL(4, 1) NULL COMMENT '体温',
  `blood_sugar` DECIMAL(5, 2) NULL COMMENT '血糖',
  `weight` DECIMAL(5, 2) NULL COMMENT '体重',
  `symptoms` TEXT NULL COMMENT '症状描述',
  `diagnosis` TEXT NULL COMMENT '诊断结果',
  `treatment` TEXT NULL COMMENT '治疗方案',
  `medication` TEXT NULL COMMENT '用药信息',
  `description` TEXT NULL COMMENT '详细描述',
  `risk_level` INT(2) NOT NULL DEFAULT 0 COMMENT '风险等级 (0:正常 1:轻度 2:中度 3:重度)',
  `is_abnormal` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否异常',
  `remarks` TEXT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(50) NULL COMMENT '创建者',
  `update_by` VARCHAR(50) NULL COMMENT '更新者',
  `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_doctor_id` (`doctor_id`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_record_type` (`record_type`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '健康记录表';