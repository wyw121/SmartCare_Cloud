-- 家属权限控制相关表结构
-- 符合医疗信息隐私保护标准的家属权限管理

-- 1. 家属与老人关联关系表
CREATE TABLE IF NOT EXISTS `family_elderly_relation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `family_id` bigint(20) NOT NULL COMMENT '家属用户ID',
  `elderly_id` bigint(20) NOT NULL COMMENT '老人ID',
  `relationship` varchar(50) NOT NULL COMMENT '关系类型：儿子、女儿、儿媳、女婿等',
  `access_level` varchar(50) NOT NULL DEFAULT 'basic' COMMENT '访问权限级别：basic-基础信息, health-健康概要, emergency-紧急联系',
  `status` int(1) NOT NULL DEFAULT 1 COMMENT '关联状态：0-无效, 1-有效',
  `start_date` datetime DEFAULT NULL COMMENT '关联开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '关联结束时间',
  `is_primary_contact` int(1) NOT NULL DEFAULT 0 COMMENT '是否为主要联系人：0-否, 1-是',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新者ID',
  `is_deleted` int(1) NOT NULL DEFAULT 0 COMMENT '删除标记：0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_family_elderly` (`family_id`, `elderly_id`, `is_deleted`),
  KEY `idx_family_id` (`family_id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家属老人关联关系表';

-- 2. 家属操作日志表
CREATE TABLE IF NOT EXISTS `family_action_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `family_id` bigint(20) NOT NULL COMMENT '家属用户ID',
  `elderly_id` bigint(20) DEFAULT NULL COMMENT '相关老人ID',
  `action_type` varchar(100) NOT NULL COMMENT '操作类型',
  `action_description` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `action_details` text DEFAULT NULL COMMENT '操作详情（JSON格式）',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理信息',
  `result_status` varchar(20) DEFAULT 'success' COMMENT '操作结果：success-成功, failed-失败, error-错误',
  `error_message` varchar(1000) DEFAULT NULL COMMENT '错误信息',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_family_id` (`family_id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_action_type` (`action_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_result_status` (`result_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家属操作日志表';

-- 3. 家属紧急联系记录表
CREATE TABLE IF NOT EXISTS `family_emergency_contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '联系记录ID',
  `family_id` bigint(20) NOT NULL COMMENT '家属用户ID',
  `elderly_id` bigint(20) NOT NULL COMMENT '老人ID',
  `contact_type` varchar(50) NOT NULL DEFAULT 'emergency' COMMENT '联系类型：emergency-紧急联系, inquiry-咨询',
  `message` text NOT NULL COMMENT '联系信息',
  `urgency_level` varchar(20) NOT NULL DEFAULT 'normal' COMMENT '紧急程度：urgent-紧急, normal-普通',
  `contact_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '处理状态：pending-待处理, processing-处理中, completed-已完成',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人员ID',
  `response_message` text DEFAULT NULL COMMENT '回复信息',
  `response_time` datetime DEFAULT NULL COMMENT '回复时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '联系时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_family_id` (`family_id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_contact_status` (`contact_status`),
  KEY `idx_urgency_level` (`urgency_level`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家属紧急联系记录表';

-- 4. 家属探视预约表
CREATE TABLE IF NOT EXISTS `family_visit_schedule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '预约ID',
  `family_id` bigint(20) NOT NULL COMMENT '家属用户ID',
  `elderly_id` bigint(20) NOT NULL COMMENT '老人ID',
  `visit_date` date NOT NULL COMMENT '探视日期',
  `visit_time` time NOT NULL COMMENT '探视时间',
  `visit_duration` int(3) DEFAULT 60 COMMENT '探视时长（分钟）',
  `visitor_count` int(2) DEFAULT 1 COMMENT '探视人数',
  `visitor_names` varchar(200) DEFAULT NULL COMMENT '探视人员姓名',
  `visit_purpose` varchar(500) DEFAULT NULL COMMENT '探视目的',
  `special_requirements` varchar(500) DEFAULT NULL COMMENT '特殊要求',
  `approval_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '审批状态：pending-待审批, approved-已批准, rejected-已拒绝, cancelled-已取消',
  `approver_id` bigint(20) DEFAULT NULL COMMENT '审批人员ID',
  `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approval_remark` varchar(500) DEFAULT NULL COMMENT '审批备注',
  `confirmation_number` varchar(50) DEFAULT NULL COMMENT '确认编号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '预约时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_confirmation` (`confirmation_number`),
  KEY `idx_family_id` (`family_id`),
  KEY `idx_elderly_id` (`elderly_id`),
  KEY `idx_visit_date` (`visit_date`),
  KEY `idx_approval_status` (`approval_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家属探视预约表';

-- 5. 插入示例数据
-- 家属与老人关联关系示例数据
INSERT IGNORE INTO `family_elderly_relation` 
(`family_id`, `elderly_id`, `relationship`, `access_level`, `status`, `is_primary_contact`, `remark`, `create_by`) 
VALUES
(3, 1, '儿子', 'basic', 1, 1, '主要联系人', 1),
(3, 2, '父亲', 'basic', 1, 0, '', 1);

-- 6. 创建索引以优化查询性能
-- 家属关联关系表索引
ALTER TABLE `family_elderly_relation` 
ADD INDEX `idx_family_elderly_status` (`family_id`, `elderly_id`, `status`),
ADD INDEX `idx_access_level` (`access_level`),
ADD INDEX `idx_primary_contact` (`is_primary_contact`);

-- 家属操作日志表索引
ALTER TABLE `family_action_log` 
ADD INDEX `idx_family_action_time` (`family_id`, `action_type`, `create_time`),
ADD INDEX `idx_elderly_time` (`elderly_id`, `create_time`);

-- 家属紧急联系记录表索引
ALTER TABLE `family_emergency_contact` 
ADD INDEX `idx_family_elderly_status` (`family_id`, `elderly_id`, `contact_status`),
ADD INDEX `idx_urgency_time` (`urgency_level`, `create_time`);

-- 家属探视预约表索引
ALTER TABLE `family_visit_schedule` 
ADD INDEX `idx_family_elderly_date` (`family_id`, `elderly_id`, `visit_date`),
ADD INDEX `idx_approval_date` (`approval_status`, `visit_date`);

-- 7. 添加必要的约束
-- 确保同一家属和老人之间只能有一个有效的关联关系
-- ALTER TABLE `family_elderly_relation` 
-- ADD CONSTRAINT `chk_valid_relation` CHECK (
--   `status` IN (0, 1) AND 
--   `is_primary_contact` IN (0, 1) AND 
--   (`end_date` IS NULL OR `end_date` > `start_date`)
-- );

-- 8. 创建视图以简化查询
-- 家属可查看的老人信息视图
CREATE OR REPLACE VIEW `v_family_elderly_access` AS
SELECT 
    fer.family_id,
    fer.elderly_id,
    fer.relationship,
    fer.access_level,
    e.name AS elderly_name,
    e.age,
    CASE e.gender WHEN 1 THEN '男' WHEN 2 THEN '女' ELSE '未知' END AS gender_text,
    e.room,
    CASE e.status WHEN 1 THEN '在住' WHEN 2 THEN '临时外出' WHEN 3 THEN '已离开' ELSE '未知' END AS status_text,
    e.phone,
    fer.is_primary_contact,
    fer.status AS relation_status
FROM family_elderly_relation fer
INNER JOIN elderly e ON fer.elderly_id = e.id
WHERE fer.status = 1 AND fer.is_deleted = 0 AND e.is_deleted = 0;

-- 家属操作统计视图
CREATE OR REPLACE VIEW `v_family_action_stats` AS
SELECT 
    family_id,
    DATE(create_time) AS action_date,
    action_type,
    COUNT(*) AS action_count,
    COUNT(CASE WHEN result_status = 'success' THEN 1 END) AS success_count,
    COUNT(CASE WHEN result_status = 'failed' THEN 1 END) AS failed_count
FROM family_action_log
WHERE create_time >= DATE_SUB(NOW(), INTERVAL 30 DAY)
GROUP BY family_id, DATE(create_time), action_type;

-- 添加注释说明
ALTER TABLE `family_elderly_relation` COMMENT = '家属老人关联关系表：用于控制家属对老人信息的访问权限，符合医疗信息隐私保护标准';
ALTER TABLE `family_action_log` COMMENT = '家属操作日志表：记录家属的所有操作行为，用于审计和安全监控';
ALTER TABLE `family_emergency_contact` COMMENT = '家属紧急联系记录表：记录家属的紧急联系请求和处理结果';
ALTER TABLE `family_visit_schedule` COMMENT = '家属探视预约表：管理家属的探视预约申请和审批流程';
