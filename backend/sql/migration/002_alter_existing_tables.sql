-- =====================================================
-- 智慧医养平台 - 数据库迁移脚本 V1.0
-- 创建日期: 2025-10-28
-- 说明: 为现有表添加缺失字段
-- =====================================================

-- 使用数据库
USE smartcare_cloud;

-- =====================================================
-- 定义辅助存储过程 (避免重复添加字段或索引)
-- =====================================================

DROP PROCEDURE IF EXISTS add_column_if_not_exists;
DROP PROCEDURE IF EXISTS add_index_if_not_exists;

DELIMITER $$

CREATE PROCEDURE add_column_if_not_exists(
  IN dbName VARCHAR(64),
  IN tblName VARCHAR(64),
  IN colName VARCHAR(64),
  IN colDefinition TEXT
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.COLUMNS
    WHERE TABLE_SCHEMA = dbName
      AND TABLE_NAME = tblName
      AND COLUMN_NAME = colName
  ) THEN
    SET @ddl = CONCAT('ALTER TABLE `', dbName, '`.`', tblName, '` ADD COLUMN ', colDefinition);
    PREPARE stmt FROM @ddl;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$

CREATE PROCEDURE add_index_if_not_exists(
  IN dbName VARCHAR(64),
  IN tblName VARCHAR(64),
  IN idxName VARCHAR(64),
  IN idxDefinition TEXT
)
BEGIN
  IF NOT EXISTS (
    SELECT 1
    FROM INFORMATION_SCHEMA.STATISTICS
    WHERE TABLE_SCHEMA = dbName
      AND TABLE_NAME = tblName
      AND INDEX_NAME = idxName
  ) THEN
    SET @ddl = CONCAT('ALTER TABLE `', dbName, '`.`', tblName, '` ADD ', idxDefinition);
    PREPARE stmt FROM @ddl;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END$$

DELIMITER ;

SET @current_db = DATABASE();

-- =====================================================
-- 1. 老人档案表 (elderly) 新增字段
-- 说明: status, allergy_history, create_by, update_by 已存在,跳过
-- =====================================================

CALL add_column_if_not_exists(@current_db, 'elderly', 'organization_id', '`organization_id` BIGINT DEFAULT NULL COMMENT ''所属机构ID'' AFTER `id`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'admission_date', '`admission_date` DATE DEFAULT NULL COMMENT ''入住日期'' AFTER `room_number`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'admission_type', '`admission_type` VARCHAR(20) DEFAULT NULL COMMENT ''入住类型'' AFTER `admission_date`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'responsible_doctor_id', '`responsible_doctor_id` BIGINT DEFAULT NULL COMMENT ''责任医生ID'' AFTER `admission_type`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'responsible_nurse_id', '`responsible_nurse_id` BIGINT DEFAULT NULL COMMENT ''责任护工ID'' AFTER `responsible_doctor_id`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'responsible_social_worker_id', '`responsible_social_worker_id` BIGINT DEFAULT NULL COMMENT ''责任社工ID'' AFTER `responsible_nurse_id`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'family_medical_history', '`family_medical_history` TEXT DEFAULT NULL COMMENT ''家族病史'' AFTER `medical_history`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'chronic_diseases', '`chronic_diseases` JSON DEFAULT NULL COMMENT ''慢性病列表'' AFTER `family_medical_history`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'disability_level', '`disability_level` TINYINT DEFAULT NULL COMMENT ''失能等级'' AFTER `care_level`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'mental_status', '`mental_status` VARCHAR(50) DEFAULT NULL COMMENT ''精神状态'' AFTER `disability_level`');
CALL add_column_if_not_exists(@current_db, 'elderly', 'payment_method', '`payment_method` VARCHAR(50) DEFAULT NULL COMMENT ''支付方式'' AFTER `mental_status`');

CALL add_index_if_not_exists(@current_db, 'elderly', 'idx_elderly_org', 'INDEX `idx_elderly_org` (`organization_id`)');
CALL add_index_if_not_exists(@current_db, 'elderly', 'idx_elderly_doctor', 'INDEX `idx_elderly_doctor` (`responsible_doctor_id`)');
CALL add_index_if_not_exists(@current_db, 'elderly', 'idx_elderly_nurse', 'INDEX `idx_elderly_nurse` (`responsible_nurse_id`)');
CALL add_index_if_not_exists(@current_db, 'elderly', 'idx_elderly_admission_date', 'INDEX `idx_elderly_admission_date` (`admission_date`)');

-- =====================================================
-- 2. 医生表 (t_doctor) 新增字段
-- =====================================================
CALL add_column_if_not_exists(@current_db, 't_doctor', 'organization_id', '`organization_id` BIGINT DEFAULT NULL COMMENT ''所属机构ID'' AFTER `id`');
CALL add_column_if_not_exists(@current_db, 't_doctor', 'user_id', '`user_id` BIGINT DEFAULT NULL COMMENT ''关联系统用户ID'' AFTER `organization_id`');
CALL add_column_if_not_exists(@current_db, 't_doctor', 'work_schedule', '`work_schedule` JSON DEFAULT NULL COMMENT ''排班信息'' AFTER `specialization`');
CALL add_column_if_not_exists(@current_db, 't_doctor', 'service_scope', '`service_scope` VARCHAR(200) DEFAULT NULL COMMENT ''服务范围'' AFTER `work_schedule`');
CALL add_column_if_not_exists(@current_db, 't_doctor', 'max_patients', '`max_patients` INT DEFAULT 50 COMMENT ''最大负责老人数'' AFTER `service_scope`');
CALL add_column_if_not_exists(@current_db, 't_doctor', 'entry_date', '`entry_date` DATE DEFAULT NULL COMMENT ''入职日期'' AFTER `max_patients`');
CALL add_column_if_not_exists(@current_db, 't_doctor', 'qualification', '`qualification` VARCHAR(100) DEFAULT NULL COMMENT ''学历/资质'' AFTER `license_number`');

CALL add_index_if_not_exists(@current_db, 't_doctor', 'idx_doctor_org', 'INDEX `idx_doctor_org` (`organization_id`)');
CALL add_index_if_not_exists(@current_db, 't_doctor', 'idx_doctor_user', 'INDEX `idx_doctor_user` (`user_id`)');

-- =====================================================
-- 3. 健康预警表 (t_health_warning) 新增字段
-- =====================================================
CALL add_column_if_not_exists(@current_db, 't_health_warning', 'assigned_doctor_id', '`assigned_doctor_id` BIGINT DEFAULT NULL COMMENT ''分配处理医生ID'' AFTER `status`');
CALL add_column_if_not_exists(@current_db, 't_health_warning', 'assigned_time', '`assigned_time` DATETIME DEFAULT NULL COMMENT ''分配时间'' AFTER `assigned_doctor_id`');
CALL add_column_if_not_exists(@current_db, 't_health_warning', 'handle_deadline', '`handle_deadline` DATETIME DEFAULT NULL COMMENT ''处理时限'' AFTER `assigned_time`');
CALL add_column_if_not_exists(@current_db, 't_health_warning', 'handle_result', '`handle_result` TEXT DEFAULT NULL COMMENT ''处理结果'' AFTER `handle_time`');
CALL add_column_if_not_exists(@current_db, 't_health_warning', 'handle_evaluation', '`handle_evaluation` VARCHAR(500) DEFAULT NULL COMMENT ''处理评价'' AFTER `handle_result`');

CALL add_index_if_not_exists(@current_db, 't_health_warning', 'idx_warning_assigned_doctor', 'INDEX `idx_warning_assigned_doctor` (`assigned_doctor_id`)');
CALL add_index_if_not_exists(@current_db, 't_health_warning', 'idx_warning_deadline', 'INDEX `idx_warning_deadline` (`handle_deadline`)');

-- =====================================================
-- 4. 系统用户表 (sys_user) 新增字段
-- =====================================================
CALL add_column_if_not_exists(@current_db, 'sys_user', 'organization_id', '`organization_id` BIGINT DEFAULT NULL COMMENT ''所属机构ID'' AFTER `role_code`');
CALL add_column_if_not_exists(@current_db, 'sys_user', 'avatar', '`avatar` VARCHAR(200) DEFAULT NULL COMMENT ''头像URL'' AFTER `organization_id`');

CALL add_index_if_not_exists(@current_db, 'sys_user', 'idx_user_org', 'INDEX `idx_user_org` (`organization_id`)');

-- =====================================================
-- 清理辅助存储过程
-- =====================================================

DROP PROCEDURE IF EXISTS add_column_if_not_exists;
DROP PROCEDURE IF EXISTS add_index_if_not_exists;

-- =====================================================
-- 5. 创建机构信息表 (organization_info)
-- =====================================================
DROP TABLE IF EXISTS `organization_info`;
CREATE TABLE `organization_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '机构ID',
  `org_code` VARCHAR(50) NOT NULL COMMENT '机构编码',
  `org_name` VARCHAR(100) NOT NULL COMMENT '机构名称',
  `org_type` VARCHAR(20) DEFAULT NULL COMMENT '机构类型: hospital-医院, nursing_home-养老院, community-社区服务中心',
  `parent_org_id` BIGINT DEFAULT NULL COMMENT '上级机构ID',
  `contact_person` VARCHAR(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `address` VARCHAR(200) DEFAULT NULL COMMENT '地址',
  `capacity` INT DEFAULT NULL COMMENT '床位数/容量',
  `current_occupancy` INT DEFAULT 0 COMMENT '当前入住数',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-停用, 1-启用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_code` (`org_code`),
  INDEX `idx_org_type` (`org_type`),
  INDEX `idx_parent_org` (`parent_org_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构信息表';

-- =====================================================
-- 6. 创建社工信息表 (social_worker_info)
-- =====================================================
DROP TABLE IF EXISTS `social_worker_info`;
CREATE TABLE `social_worker_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '社工ID',
  `user_id` BIGINT NOT NULL COMMENT '关联系统用户ID',
  `organization_id` BIGINT DEFAULT NULL COMMENT '所属机构ID',
  `employee_number` VARCHAR(50) NOT NULL COMMENT '工号',
  `name` VARCHAR(50) NOT NULL COMMENT '姓名',
  `gender` TINYINT DEFAULT NULL COMMENT '性别: 1-男, 0-女',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '联系电话',
  `specialization` VARCHAR(100) DEFAULT NULL COMMENT '专业领域: 心理咨询、活动组织、康复辅导等',
  `certificate_type` VARCHAR(50) DEFAULT NULL COMMENT '证书类型: 社会工作师、心理咨询师等',
  `certificate_number` VARCHAR(50) DEFAULT NULL COMMENT '证书编号',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 0-离职, 1-在职, 2-休假',
  `entry_date` DATE DEFAULT NULL COMMENT '入职日期',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_employee_number` (`employee_number`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  INDEX `idx_organization` (`organization_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社工信息表';

-- =====================================================
-- 脚本执行完成
-- =====================================================
SELECT '现有表字段补充完成,新增2个补充表创建完成!' AS Message;
