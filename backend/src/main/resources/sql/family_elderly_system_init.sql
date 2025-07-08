-- 家属老人关联系统数据库脚本
-- 创建时间：2025-07-09
-- 用途：为智慧医养平台建立家属与老人的关联关系

-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ========================================
-- 1. 创建家属用户表
-- ========================================
DROP TABLE IF EXISTS `family_user`;
CREATE TABLE `family_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '家属用户ID',
    `sys_user_id` BIGINT(20) NOT NULL COMMENT '关联系统用户表ID',
    `real_name` VARCHAR(50) NOT NULL COMMENT '家属真实姓名',
    `gender` TINYINT(1) NULL COMMENT '性别：1-男，0-女',
    `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
    `email` VARCHAR(100) NULL COMMENT '邮箱地址',
    `id_card` VARCHAR(18) NULL COMMENT '身份证号',
    `address` VARCHAR(255) NULL COMMENT '家庭住址',
    `occupation` VARCHAR(100) NULL COMMENT '职业',
    `company` VARCHAR(200) NULL COMMENT '工作单位',
    `work_phone` VARCHAR(20) NULL COMMENT '工作电话',
    `emergency_contact` VARCHAR(50) NULL COMMENT '紧急联系人',
    `emergency_phone` VARCHAR(20) NULL COMMENT '紧急联系人电话',
    `remarks` TEXT NULL COMMENT '备注信息',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT(20) NULL COMMENT '创建者ID',
    `update_by` BIGINT(20) NULL COMMENT '更新者ID',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_id` (`sys_user_id`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_real_name` (`real_name`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家属用户扩展信息表';

-- ========================================
-- 2. 创建家属老人关联表
-- ========================================
DROP TABLE IF EXISTS `family_elderly_relation`;
CREATE TABLE `family_elderly_relation` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '关联关系ID',
    `family_user_id` BIGINT(20) NOT NULL COMMENT '家属用户ID',
    `elderly_id` BIGINT(20) NOT NULL COMMENT '老人ID',
    `relationship_type` VARCHAR(20) NOT NULL COMMENT '关系类型：child-子女，spouse-配偶，grandchild-孙子女，other-其他',
    `relationship_name` VARCHAR(50) NOT NULL COMMENT '关系名称：儿子、女儿、老伴、孙子、孙女等',
    `is_primary` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否主要联系人：1-是，0-否',
    `is_emergency` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否紧急联系人：1-是，0-否',
    `contact_priority` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '联系优先级：1-最高，2-高，3-中，4-低',
    `care_responsibility` VARCHAR(200) NULL COMMENT '照护责任描述',
    `visit_frequency` VARCHAR(50) NULL COMMENT '探视频率：daily-每日，weekly-每周，monthly-每月，seasonal-季度',
    `authorized_operations` VARCHAR(500) NULL COMMENT '授权操作权限，JSON格式',
    `start_date` DATE NOT NULL COMMENT '关联开始日期',
    `end_date` DATE NULL COMMENT '关联结束日期',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '关联状态：1-有效，0-失效',
    `remarks` TEXT NULL COMMENT '备注信息',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT(20) NULL COMMENT '创建者ID',
    `update_by` BIGINT(20) NULL COMMENT '更新者ID',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_family_elderly` (`family_user_id`, `elderly_id`, `is_deleted`),
    KEY `idx_family_user_id` (`family_user_id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_relationship_type` (`relationship_type`),
    KEY `idx_is_primary` (`is_primary`),
    KEY `idx_status` (`status`),
    KEY `idx_start_date` (`start_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家属老人关联关系表';

-- ========================================
-- 3. 创建家属访问日志表
-- ========================================
DROP TABLE IF EXISTS `family_access_log`;
CREATE TABLE `family_access_log` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `family_user_id` BIGINT(20) NOT NULL COMMENT '家属用户ID',
    `elderly_id` BIGINT(20) NOT NULL COMMENT '访问的老人ID',
    `access_type` VARCHAR(50) NOT NULL COMMENT '访问类型：view-查看，health-健康档案，warning-预警信息，contact-联系医护',
    `access_module` VARCHAR(100) NOT NULL COMMENT '访问模块',
    `access_content` TEXT NULL COMMENT '访问内容描述',
    `ip_address` VARCHAR(50) NULL COMMENT '访问IP地址',
    `user_agent` VARCHAR(500) NULL COMMENT '用户代理信息',
    `access_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '访问时间',
    `session_id` VARCHAR(100) NULL COMMENT '会话ID',
    `is_sensitive` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否敏感操作：1-是，0-否',
    PRIMARY KEY (`id`),
    KEY `idx_family_user_id` (`family_user_id`),
    KEY `idx_elderly_id` (`elderly_id`),
    KEY `idx_access_type` (`access_type`),
    KEY `idx_access_time` (`access_time`),
    KEY `idx_is_sensitive` (`is_sensitive`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家属访问日志表';

-- ========================================
-- 4. 添加外键约束
-- ========================================
ALTER TABLE `family_user` 
ADD CONSTRAINT `fk_family_user_sys_user` FOREIGN KEY (`sys_user_id`) 
REFERENCES `sys_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `family_elderly_relation` 
ADD CONSTRAINT `fk_family_elderly_family` FOREIGN KEY (`family_user_id`) 
REFERENCES `family_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_family_elderly_elderly` FOREIGN KEY (`elderly_id`) 
REFERENCES `elderly` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

ALTER TABLE `family_access_log` 
ADD CONSTRAINT `fk_access_log_family` FOREIGN KEY (`family_user_id`) 
REFERENCES `family_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
ADD CONSTRAINT `fk_access_log_elderly` FOREIGN KEY (`elderly_id`) 
REFERENCES `elderly` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

-- ========================================
-- 5. 插入测试数据
-- ========================================

-- 5.1 插入家属用户到系统用户表
INSERT INTO `sys_user` (
    `username`, `password`, `real_name`, `gender`, `phone`, `email`, 
    `role_code`, `role_name`, `family_relationship`, `family_elderly_ids`,
    `status`, `description`, `create_by`
) VALUES 
('family_li', '$2a$10$abc123def456ghi789jkl', '李明华', 1, '13800138001', 'li.minghua@email.com', 
 'family', '家属', '儿子', '1,2', 1, '李爷爷和王奶奶的儿子', 1),
('family_wang', '$2a$10$xyz789uvw456rst123opq', '王秀英', 0, '13800138002', 'wang.xiuying@email.com', 
 'family', '家属', '女儿', '2', 1, '王奶奶的女儿', 1),
('family_zhang', '$2a$10$pqr456stu789vwx123yz0', '张建国', 1, '13800138003', 'zhang.jianguo@email.com', 
 'family', '家属', '孙子', '1', 1, '李爷爷的孙子', 1);

-- 5.2 插入家属用户扩展信息
INSERT INTO `family_user` (
    `sys_user_id`, `real_name`, `gender`, `phone`, `email`, `id_card`, 
    `address`, `occupation`, `company`, `work_phone`, 
    `emergency_contact`, `emergency_phone`, `remarks`, `create_by`
) VALUES 
((SELECT id FROM sys_user WHERE username = 'family_li'), '李明华', 1, '13800138001', 'li.minghua@email.com', '110101197001011234',
 '北京市朝阳区建国路88号', '软件工程师', '北京科技有限公司', '010-12345678',
 '李明华妻子', '13900139001', '工作稳定，收入良好，能够承担老人照护费用', 1),
((SELECT id FROM sys_user WHERE username = 'family_wang'), '王秀英', 0, '13800138002', 'wang.xiuying@email.com', '110101197505152345',
 '北京市海淀区中关村大街99号', '教师', '北京市第一中学', '010-87654321',
 '王秀英丈夫', '13900139002', '教师职业，时间相对灵活，可以经常照顾老人', 1),
((SELECT id FROM sys_user WHERE username = 'family_zhang'), '张建国', 1, '13800138003', 'zhang.jianguo@email.com', '110101199001013456',
 '北京市西城区西单大街66号', '医生', '北京协和医院', '010-11223344',
 '张建国妻子', '13900139003', '医生职业，医学专业背景，能够更好地关注老人健康', 1);

-- 5.3 插入家属老人关联关系
INSERT INTO `family_elderly_relation` (
    `family_user_id`, `elderly_id`, `relationship_type`, `relationship_name`, 
    `is_primary`, `is_emergency`, `contact_priority`, `care_responsibility`, 
    `visit_frequency`, `authorized_operations`, `start_date`, `status`, `create_by`
) VALUES 
-- 李明华与李爷爷的关系（假设elderly表中id=1是李爷爷）
((SELECT id FROM family_user WHERE phone = '13800138001'), 1, 'child', '儿子', 1, 1, 1, 
 '主要照护责任人，负责医疗决策和费用支付', 'weekly', 
 '{"view_health": true, "contact_doctor": true, "emergency_auth": true, "medical_decision": true}', 
 '2024-01-01', 1, 1),
-- 李明华与王奶奶的关系（假设elderly表中id=2是王奶奶）
((SELECT id FROM family_user WHERE phone = '13800138001'), 2, 'child', '儿子', 1, 1, 1, 
 '主要照护责任人，负责日常照护和医疗决策', 'weekly', 
 '{"view_health": true, "contact_doctor": true, "emergency_auth": true, "medical_decision": true}', 
 '2024-01-01', 1, 1),
-- 王秀英与王奶奶的关系
((SELECT id FROM family_user WHERE phone = '13800138002'), 2, 'child', '女儿', 0, 1, 2, 
 '次要照护责任人，协助日常照护', 'daily', 
 '{"view_health": true, "contact_doctor": true, "emergency_auth": false, "medical_decision": false}', 
 '2024-01-01', 1, 1),
-- 张建国与李爷爷的关系
((SELECT id FROM family_user WHERE phone = '13800138003'), 1, 'grandchild', '孙子', 0, 0, 3, 
 '定期探视，提供医学建议', 'monthly', 
 '{"view_health": true, "contact_doctor": false, "emergency_auth": false, "medical_decision": false}', 
 '2024-01-01', 1, 1);

-- ========================================
-- 6. 创建视图和查询函数
-- ========================================

-- 6.1 创建家属老人关联视图
CREATE OR REPLACE VIEW `v_family_elderly_info` AS
SELECT 
    fer.id as relation_id,
    fu.id as family_user_id,
    fu.real_name as family_name,
    fu.phone as family_phone,
    fu.email as family_email,
    e.id as elderly_id,
    e.name as elderly_name,
    e.gender as elderly_gender,
    e.age as elderly_age,
    e.phone as elderly_phone,
    e.health_status,
    e.care_level,
    e.room_number,
    fer.relationship_type,
    fer.relationship_name,
    fer.is_primary,
    fer.is_emergency,
    fer.contact_priority,
    fer.visit_frequency,
    fer.authorized_operations,
    fer.status as relation_status,
    e.status as elderly_status
FROM family_elderly_relation fer
JOIN family_user fu ON fer.family_user_id = fu.id
JOIN elderly e ON fer.elderly_id = e.id
WHERE fer.is_deleted = 0 
AND fu.is_deleted = 0 
AND e.is_deleted = 0
AND fer.status = 1
AND fu.status = 1
AND e.status = 1;

-- 6.2 创建查询家属关联老人的存储过程
DELIMITER $$
CREATE PROCEDURE `GetElderlyByFamilyUserId`(IN family_user_id BIGINT)
BEGIN
    SELECT 
        e.id,
        e.name,
        e.gender,
        e.id_card,
        e.birth_date,
        e.age,
        e.phone,
        e.address,
        e.emergency_contact_name,
        e.emergency_contact_phone,
        e.health_status,
        e.care_level,
        e.insurance_type,
        e.medical_history,
        e.allergy_history,
        e.room_number,
        e.guardian_name,
        e.remarks,
        e.create_time,
        e.update_time,
        fer.relationship_type,
        fer.relationship_name,
        fer.is_primary,
        fer.is_emergency,
        fer.contact_priority,
        fer.authorized_operations
    FROM elderly e
    INNER JOIN family_elderly_relation fer ON e.id = fer.elderly_id
    WHERE fer.family_user_id = family_user_id
    AND fer.status = 1
    AND fer.is_deleted = 0
    AND e.status = 1
    AND e.is_deleted = 0
    ORDER BY fer.contact_priority ASC, fer.is_primary DESC;
END$$
DELIMITER ;

-- ========================================
-- 7. 创建权限控制函数
-- ========================================

-- 7.1 检查家属是否有权限访问某个老人
DELIMITER $$
CREATE FUNCTION `CheckFamilyElderlyAccess`(
    family_user_id BIGINT, 
    elderly_id BIGINT
) RETURNS BOOLEAN
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE access_count INT DEFAULT 0;
    
    SELECT COUNT(*) INTO access_count
    FROM family_elderly_relation
    WHERE family_user_id = family_user_id
    AND elderly_id = elderly_id
    AND status = 1
    AND is_deleted = 0;
    
    RETURN access_count > 0;
END$$
DELIMITER ;

SET FOREIGN_KEY_CHECKS = 1;

-- ========================================
-- 8. 插入权限数据
-- ========================================

-- 插入家属权限到权限表
INSERT INTO `sys_permission` (
    `permission_name`, `permission_code`, `permission_type`, `parent_id`, 
    `permission_path`, `icon`, `description`, `status`, `sort_order`, `create_by`
) VALUES 
('家属端首页', 'family:dashboard:view', 'menu', 0, '/dashboard', 'dashboard', '家属用户首页权限', 1, 1, 1),
('我的关联长辈', 'family:elderly:view', 'menu', 0, '/elderly/family-view', 'user', '查看关联老人信息', 1, 2, 1),
('老人详情查看', 'family:elderly:detail', 'button', 0, '', 'view', '查看老人详细信息', 1, 3, 1),
('健康档案查看', 'family:health:view', 'button', 0, '', 'health', '查看老人健康档案', 1, 4, 1),
('健康预警查看', 'family:warning:view', 'button', 0, '', 'warning', '查看健康预警信息', 1, 5, 1),
('联系医护人员', 'family:contact:send', 'button', 0, '', 'message', '发送联系医护请求', 1, 6, 1),
('个人中心', 'family:profile:view', 'menu', 0, '/profile', 'user-setting', '个人信息管理', 1, 7, 1);

-- 创建家属角色
INSERT INTO `sys_role` (
    `role_name`, `role_code`, `description`, `status`, `sort_order`, `create_by`
) VALUES 
('家属用户', 'family', '老人家属，可查看关联老人信息和健康状况', 1, 3, 1);

-- 为家属角色分配权限
INSERT INTO `sys_role_permission` (
    `role_id`, `permission_id`, `create_by`
) SELECT 
    (SELECT id FROM sys_role WHERE role_code = 'family'),
    sp.id,
    1
FROM sys_permission sp 
WHERE sp.permission_code LIKE 'family:%';

-- 为家属用户分配角色
INSERT INTO `sys_user_role` (
    `user_id`, `role_id`, `create_by`
) SELECT 
    su.id,
    (SELECT id FROM sys_role WHERE role_code = 'family'),
    1
FROM sys_user su 
WHERE su.role_code = 'family';

COMMIT;

-- 完成提示
SELECT '家属关联系统初始化完成！' as message;
SELECT COUNT(*) as family_users_count FROM family_user;
SELECT COUNT(*) as relations_count FROM family_elderly_relation;
SELECT COUNT(*) as family_permissions_count FROM sys_permission WHERE permission_code LIKE 'family:%';
