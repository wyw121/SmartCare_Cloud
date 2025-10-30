-- =====================================================
-- 智慧医养平台 - 权限改造数据库脚本
-- 创建日期: 2025-10-29
-- 说明: 创建新增实体表,用于实现完整的权限控制体系
-- =====================================================

USE smartcare_cloud;

-- =====================================================
-- 1. 机构信息表
-- =====================================================
CREATE TABLE IF NOT EXISTS `organization` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '机构ID',
    `org_code` VARCHAR(50) NOT NULL COMMENT '机构编码(唯一)',
    `org_name` VARCHAR(200) NOT NULL COMMENT '机构名称',
    `org_type` VARCHAR(50) DEFAULT 'nursing_home' COMMENT '机构类型: nursing_home-养老院, hospital-医院, community-社区服务中心',
    `org_level` TINYINT DEFAULT 2 COMMENT '机构级别: 1-一级, 2-二级, 3-三级',
    `director_name` VARCHAR(50) COMMENT '负责人姓名',
    `director_phone` VARCHAR(20) COMMENT '负责人电话',
    `contact_phone` VARCHAR(20) COMMENT '联系电话',
    `contact_email` VARCHAR(100) COMMENT '联系邮箱',
    `address` VARCHAR(500) COMMENT '详细地址',
    `province` VARCHAR(50) COMMENT '省份',
    `city` VARCHAR(50) COMMENT '城市',
    `district` VARCHAR(50) COMMENT '区县',
    `total_beds` INT DEFAULT 0 COMMENT '床位总数',
    `occupied_beds` INT DEFAULT 0 COMMENT '已使用床位数',
    `business_license` VARCHAR(100) COMMENT '营业执照号',
    `service_scope` VARCHAR(500) COMMENT '服务范围',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-停业, 1-营业, 2-装修',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建者ID',
    `update_by` BIGINT COMMENT '更新者ID',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_org_code` (`org_code`),
    KEY `idx_org_type` (`org_type`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构信息表';

-- =====================================================
-- 2. 护工信息表
-- =====================================================
CREATE TABLE IF NOT EXISTS `nurse_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '护工ID',
    `user_id` BIGINT NOT NULL COMMENT '关联系统用户ID',
    `organization_id` BIGINT NOT NULL COMMENT '所属机构ID',
    `employee_number` VARCHAR(50) NOT NULL COMMENT '工号(唯一)',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT COMMENT '性别: 1-男, 0-女',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `id_card` VARCHAR(18) COMMENT '身份证号',
    `entry_date` DATE COMMENT '入职日期',
    `certificate_type` VARCHAR(50) COMMENT '证书类型: 护理员证、养老护理员证等',
    `certificate_number` VARCHAR(50) COMMENT '证书编号',
    `work_area` VARCHAR(100) COMMENT '工作区域',
    `max_care_count` INT DEFAULT 5 COMMENT '最大照护人数',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-离职, 1-在职, 2-休假',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_employee_number` (`employee_number`),
    KEY `idx_organization_id` (`organization_id`),
    KEY `idx_name` (`name`),
    KEY `idx_work_area` (`work_area`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护工信息表';

-- =====================================================
-- 3. 社工信息表
-- =====================================================
CREATE TABLE IF NOT EXISTS `social_worker_info` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '社工ID',
    `user_id` BIGINT NOT NULL COMMENT '关联系统用户ID',
    `organization_id` BIGINT NOT NULL COMMENT '所属机构ID',
    `employee_number` VARCHAR(50) NOT NULL COMMENT '工号(唯一)',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` TINYINT COMMENT '性别: 1-男, 0-女',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `specialization` VARCHAR(100) COMMENT '专业领域: 心理咨询、康复指导、生活服务等',
    `certificate_type` VARCHAR(50) COMMENT '证书类型: 社会工作师证、心理咨询师证等',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-离职, 1-在职, 2-休假',
    `entry_date` DATE COMMENT '入职日期',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`),
    UNIQUE KEY `uk_employee_number` (`employee_number`),
    KEY `idx_organization_id` (`organization_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='社工信息表';

-- =====================================================
-- 4. 家属用户表
-- =====================================================
CREATE TABLE IF NOT EXISTS `family_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '家属ID',
    `sys_user_id` BIGINT NOT NULL COMMENT '关联系统用户ID',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `gender` TINYINT COMMENT '性别: 1-男, 0-女',
    `phone` VARCHAR(20) COMMENT '联系电话',
    `email` VARCHAR(100) COMMENT '电子邮箱',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1-启用, 0-禁用',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建者ID',
    `update_by` BIGINT COMMENT '更新者ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_id` (`sys_user_id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属用户表';

-- =====================================================
-- 5. 医生-老人关联表 (数据权限核心)
-- =====================================================
CREATE TABLE IF NOT EXISTS `doctor_elderly_relation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
    `elderly_id` BIGINT NOT NULL COMMENT '老人ID',
    `relationship_type` VARCHAR(20) NOT NULL DEFAULT 'assistant' COMMENT '关系类型: primary-主治医生, assistant-协助医生, consultant-会诊医生',
    `start_date` DATE NOT NULL COMMENT '开始负责日期',
    `end_date` DATE COMMENT '结束负责日期(null表示仍在负责中)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-无效, 1-有效',
    `remark` VARCHAR(500) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT COMMENT '创建者ID',
    `update_by` BIGINT COMMENT '更新者ID',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_doctor_id` (`doctor_id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_relationship_type` (`relationship_type`),
    KEY `idx_status` (`status`),
    KEY `idx_doctor_elderly` (`doctor_id`, `elderly_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='医生-老人关联表(用于数据权限隔离)';

-- =====================================================
-- 6. 护工-老人关联表
-- =====================================================
CREATE TABLE IF NOT EXISTS `nurse_elderly_relation` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
    `nurse_id` BIGINT NOT NULL COMMENT '护工ID',
    `elderly_id` BIGINT NOT NULL COMMENT '老人ID',
    `care_level` TINYINT COMMENT '照护等级: 1-一级照护, 2-二级照护, 3-三级照护',
    `start_date` DATE NOT NULL COMMENT '开始照护日期',
    `end_date` DATE COMMENT '结束照护日期(null表示仍在照护中)',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-无效, 1-有效',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_nurse_id` (`nurse_id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_status` (`status`),
    KEY `idx_nurse_elderly` (`nurse_id`, `elderly_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='护工-老人关联表';

-- =====================================================
-- 7. 为现有elderly表添加新字段
-- =====================================================
ALTER TABLE `elderly` 
    ADD COLUMN IF NOT EXISTS `organization_id` BIGINT COMMENT '所属机构ID' AFTER `id`,
    ADD COLUMN IF NOT EXISTS `responsible_doctor_id` BIGINT COMMENT '责任医生ID' AFTER `chronic_diseases`,
    ADD COLUMN IF NOT EXISTS `responsible_nurse_id` BIGINT COMMENT '责任护工ID' AFTER `responsible_doctor_id`,
    ADD COLUMN IF NOT EXISTS `responsible_social_worker_id` BIGINT COMMENT '责任社工ID' AFTER `responsible_nurse_id`;

-- 添加索引
ALTER TABLE `elderly`
    ADD INDEX IF NOT EXISTS `idx_organization_id` (`organization_id`),
    ADD INDEX IF NOT EXISTS `idx_responsible_doctor_id` (`responsible_doctor_id`),
    ADD INDEX IF NOT EXISTS `idx_responsible_nurse_id` (`responsible_nurse_id`),
    ADD INDEX IF NOT EXISTS `idx_responsible_social_worker_id` (`responsible_social_worker_id`);

-- =====================================================
-- 8. 插入测试数据
-- =====================================================

-- 插入默认机构
INSERT INTO `organization` (`org_code`, `org_name`, `org_type`, `org_level`, `director_name`, `director_phone`, 
                             `contact_phone`, `address`, `province`, `city`, `district`, 
                             `total_beds`, `occupied_beds`, `status`)
VALUES ('ORG001', '智慧医养示范中心', 'nursing_home', 2, '李院长', '13800138000', 
        '010-12345678', '北京市朝阳区XX路XX号', '北京', '北京市', '朝阳区', 
        200, 0, 1)
ON DUPLICATE KEY UPDATE `org_name` = '智慧医养示范中心';

-- =====================================================
-- 完成
-- =====================================================
SELECT '权限改造数据库脚本执行完成!' AS message;
