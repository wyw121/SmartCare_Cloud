-- =====================================================
-- 智慧医养平台 - 数据库迁移脚本 V1.0
-- 创建日期: 2025-10-28
-- 说明: 创建7个新增核心业务表
-- =====================================================

-- 使用数据库
USE smartcare_cloud;

-- =====================================================
-- 1. 医生-老人关联表 (doctor_elderly_relation)
-- 用途: 建立医生与老人的责任关系,实现医生数据隔离
-- =====================================================
DROP TABLE IF EXISTS `doctor_elderly_relation`;
CREATE TABLE `doctor_elderly_relation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
  `elderly_id` BIGINT NOT NULL COMMENT '老人ID',
  `relationship_type` VARCHAR(20) NOT NULL DEFAULT 'primary' COMMENT '关系类型: primary-主治医生, assistant-协助医生, consultant-会诊医生',
  `start_date` DATE NOT NULL COMMENT '开始负责日期',
  `end_date` DATE DEFAULT NULL COMMENT '结束负责日期',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-无效, 1-有效',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` BIGINT DEFAULT NULL COMMENT '创建者ID',
  `update_by` BIGINT DEFAULT NULL COMMENT '更新者ID',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  INDEX `idx_doctor_id` (`doctor_id`),
  INDEX `idx_elderly_id` (`elderly_id`),
  INDEX `idx_status` (`status`),
  INDEX `idx_relationship_type` (`relationship_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生-老人关系表';

-- =====================================================
-- 2. 护工信息表 (nurse_info)
-- 用途: 存储护工基础信息
-- =====================================================
DROP TABLE IF EXISTS `nurse_info`;
CREATE TABLE `nurse_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '护工ID',
  `user_id` BIGINT NOT NULL COMMENT '关联系统用户ID',
  `organization_id` BIGINT DEFAULT NULL COMMENT '所属机构ID',
  `employee_number` VARCHAR(50) NOT NULL COMMENT '工号',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` TINYINT DEFAULT NULL COMMENT '性别: 1-男, 0-女',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `id_card` VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
  `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
  `certificate_type` VARCHAR(50) DEFAULT NULL COMMENT '证书类型: 养老护理员证、护士资格证等',
  `certificate_number` VARCHAR(50) DEFAULT NULL COMMENT '证书编号',
  `work_area` VARCHAR(100) DEFAULT NULL COMMENT '工作区域: 一病区、二病区等',
  `max_care_count` INT DEFAULT 5 COMMENT '最大照护人数',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-离职, 1-在职, 2-休假',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_number` (`employee_number`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  INDEX `idx_organization` (`organization_id`),
  INDEX `idx_name` (`name`),
  INDEX `idx_work_area` (`work_area`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护工信息表';

-- =====================================================
-- 3. 护工-老人关联表 (nurse_elderly_relation)
-- 用途: 建立护工与老人的照护关系
-- =====================================================
DROP TABLE IF EXISTS `nurse_elderly_relation`;
CREATE TABLE `nurse_elderly_relation` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `nurse_id` BIGINT NOT NULL COMMENT '护工ID',
  `elderly_id` BIGINT NOT NULL COMMENT '老人ID',
  `care_level` TINYINT DEFAULT NULL COMMENT '照护等级: 1-一级照护, 2-二级照护, 3-三级照护, 4-特级照护',
  `start_date` DATE NOT NULL COMMENT '开始照护日期',
  `end_date` DATE DEFAULT NULL COMMENT '结束照护日期',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-无效, 1-有效',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  INDEX `idx_nurse_id` (`nurse_id`),
  INDEX `idx_elderly_id` (`elderly_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护工-老人关系表';

-- =====================================================
-- 4. 护理记录表 (nursing_record)
-- 用途: 记录护工的日常护理工作
-- =====================================================
DROP TABLE IF EXISTS `nursing_record`;
CREATE TABLE `nursing_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elderly_id` BIGINT NOT NULL COMMENT '老人ID',
  `nurse_id` BIGINT NOT NULL COMMENT '护工ID',
  `record_date` DATE NOT NULL COMMENT '记录日期',
  `record_time` TIME NOT NULL COMMENT '记录时间',
  `care_type` VARCHAR(50) DEFAULT NULL COMMENT '护理类型: morning_care-晨间护理, evening_care-晚间护理, feeding-喂食, bathing-洗浴, turning-翻身, toileting-如厕协助, medication-用药协助, exercise-活动协助',
  `care_content` TEXT DEFAULT NULL COMMENT '护理内容',
  `elderly_status` VARCHAR(200) DEFAULT NULL COMMENT '老人状态: 精神状态、配合度等',
  `abnormal_situation` VARCHAR(500) DEFAULT NULL COMMENT '异常情况',
  `images` JSON DEFAULT NULL COMMENT '护理照片: ["url1", "url2"]',
  `duration_minutes` INT DEFAULT NULL COMMENT '护理时长(分钟)',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_elderly_date` (`elderly_id`, `record_date`),
  INDEX `idx_nurse_date` (`nurse_id`, `record_date`),
  INDEX `idx_care_type` (`care_type`),
  INDEX `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护理记录表';

-- =====================================================
-- 5. 用药记录表 (medication_record)
-- 用途: 管理老人用药信息和实际服药记录
-- =====================================================
DROP TABLE IF EXISTS `medication_record`;
CREATE TABLE `medication_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elderly_id` BIGINT NOT NULL COMMENT '老人ID',
  `doctor_id` BIGINT DEFAULT NULL COMMENT '开具医生ID',
  `medication_name` VARCHAR(100) NOT NULL COMMENT '药品名称',
  `medication_type` VARCHAR(50) DEFAULT NULL COMMENT '药品类型: 中成药、西药、中药饮片等',
  `dosage` VARCHAR(50) DEFAULT NULL COMMENT '剂量: 如 1片、10ml、2粒',
  `frequency` VARCHAR(50) DEFAULT NULL COMMENT '服用频率: 每日3次、每日1次、按需等',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE DEFAULT NULL COMMENT '结束日期',
  `administration_route` VARCHAR(50) DEFAULT NULL COMMENT '给药途径: oral-口服, injection-注射, external-外用, inhalation-吸入',
  `purpose` VARCHAR(200) DEFAULT NULL COMMENT '用药目的',
  `side_effects` VARCHAR(500) DEFAULT NULL COMMENT '可能副作用',
  `precautions` VARCHAR(500) DEFAULT NULL COMMENT '注意事项',
  `actual_records` JSON DEFAULT NULL COMMENT '实际服药记录: [{"date":"2024-10-28","time":"08:00","operator_id":5,"operator_name":"王护工","status":"completed","remark":"按时服用"}]',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-已停用, 1-使用中',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  INDEX `idx_elderly_id` (`elderly_id`),
  INDEX `idx_doctor_id` (`doctor_id`),
  INDEX `idx_status_date` (`status`, `start_date`),
  INDEX `idx_medication_name` (`medication_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用药记录表';

-- =====================================================
-- 6. 巡诊记录表 (visit_record)
-- 用途: 记录医生巡诊工作
-- =====================================================
DROP TABLE IF EXISTS `visit_record`;
CREATE TABLE `visit_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `elderly_id` BIGINT NOT NULL COMMENT '老人ID',
  `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
  `visit_date` DATE NOT NULL COMMENT '巡诊日期',
  `visit_time` TIME NOT NULL COMMENT '巡诊时间',
  `chief_complaint` VARCHAR(500) DEFAULT NULL COMMENT '主诉',
  `symptoms` TEXT DEFAULT NULL COMMENT '症状描述',
  `physical_exam` TEXT DEFAULT NULL COMMENT '体格检查',
  `diagnosis` VARCHAR(500) DEFAULT NULL COMMENT '诊断',
  `treatment_plan` TEXT DEFAULT NULL COMMENT '治疗方案',
  `prescription` JSON DEFAULT NULL COMMENT '处方: [{"drug_name":"阿司匹林","dosage":"100mg","frequency":"每日1次","duration":"30天"}]',
  `next_visit_date` DATE DEFAULT NULL COMMENT '下次巡诊日期',
  `remarks` TEXT DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_elderly_date` (`elderly_id`, `visit_date`),
  INDEX `idx_doctor_date` (`doctor_id`, `visit_date`),
  INDEX `idx_visit_date` (`visit_date`),
  INDEX `idx_next_visit_date` (`next_visit_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡诊记录表';

-- =====================================================
-- 7. 操作审计日志表 (operation_log)
-- 用途: 记录所有用户操作,防止越权访问
-- =====================================================
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` BIGINT NOT NULL COMMENT '操作用户ID',
  `user_role` VARCHAR(20) DEFAULT NULL COMMENT '用户角色: admin, doctor, nurse, family等',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '用户名',
  `operation_type` VARCHAR(50) DEFAULT NULL COMMENT '操作类型: view-查看, add-新增, edit-编辑, delete-删除, export-导出, login-登录, logout-登出',
  `operation_module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块: elderly-老人管理, health-健康管理, system-系统管理等',
  `target_type` VARCHAR(50) DEFAULT NULL COMMENT '目标类型: elderly, doctor, health_record, health_warning等',
  `target_id` BIGINT DEFAULT NULL COMMENT '目标ID',
  `operation_detail` TEXT DEFAULT NULL COMMENT '操作详情: JSON格式',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` VARCHAR(500) DEFAULT NULL COMMENT '用户代理(浏览器信息)',
  `operation_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `execution_time` INT DEFAULT NULL COMMENT '执行时长(毫秒)',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-失败, 1-成功',
  `error_message` TEXT DEFAULT NULL COMMENT '错误信息',
  PRIMARY KEY (`id`),
  INDEX `idx_user_time` (`user_id`, `operation_time`),
  INDEX `idx_target` (`target_type`, `target_id`),
  INDEX `idx_operation_time` (`operation_time`),
  INDEX `idx_operation_type` (`operation_type`),
  INDEX `idx_user_role` (`user_role`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作审计日志表';

-- =====================================================
-- 脚本执行完成
-- =====================================================
SELECT '7个新增表创建完成!' AS Message;
