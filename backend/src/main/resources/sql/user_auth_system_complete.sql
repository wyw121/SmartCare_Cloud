-- 智慧医养大数据平台用户权限系统完整初始化脚本
-- 清理现有数据（开发环境使用）
SET FOREIGN_KEY_CHECKS = 0;

-- 清理用户相关表
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_role_permission`;
DROP TABLE IF EXISTS `sys_user`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_permission`;

SET FOREIGN_KEY_CHECKS = 1;

-- 创建用户表
CREATE TABLE `sys_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    `real_name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `gender` TINYINT(1) NULL COMMENT '性别：1-男，0-女',
    `phone` VARCHAR(20) NULL COMMENT '手机号',
    `email` VARCHAR(100) NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) NULL COMMENT '头像URL',
    `department` VARCHAR(100) NULL COMMENT '部门',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码：admin/doctor/family',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    
    -- 医生专用字段
    `doctor_title` VARCHAR(50) NULL COMMENT '医生职称',
    `doctor_speciality` VARCHAR(200) NULL COMMENT '医生专业特长',
    `doctor_license_number` VARCHAR(50) NULL COMMENT '医生执业证书号',
    `doctor_experience_years` INT(2) NULL COMMENT '从业年限',
    
    -- 家属专用字段
    `family_relationship` VARCHAR(50) NULL COMMENT '与老人关系',
    `family_elderly_ids` VARCHAR(255) NULL COMMENT '关联的老人ID列表，逗号分隔',
    
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `login_count` INT(11) DEFAULT 0 COMMENT '登录次数',
    `last_login_time` DATETIME NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(50) NULL COMMENT '最后登录IP',
    `description` TEXT NULL COMMENT '备注描述',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT(20) NULL COMMENT '创建者ID',
    `update_by` BIGINT(20) NULL COMMENT '更新者ID',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_phone` (`phone`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_role_code` (`role_code`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统用户表';

-- 创建角色表
CREATE TABLE `sys_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `description` TEXT NULL COMMENT '角色描述',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序',
    `remark` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT(20) NULL COMMENT '创建者ID',
    `update_by` BIGINT(20) NULL COMMENT '更新者ID',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code` (`role_code`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统角色表';

-- 创建权限表
CREATE TABLE `sys_permission` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `permission_type` VARCHAR(20) NOT NULL COMMENT '权限类型：menu-菜单，button-按钮，api-接口',
    `parent_id` BIGINT(20) DEFAULT 0 COMMENT '父权限ID',
    `permission_path` VARCHAR(255) NULL COMMENT '权限路径',
    `icon` VARCHAR(50) NULL COMMENT '图标',
    `description` TEXT NULL COMMENT '权限描述',
    `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    `sort_order` INT(11) DEFAULT 0 COMMENT '排序',
    `remark` TEXT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT(20) NULL COMMENT '创建者ID',
    `update_by` BIGINT(20) NULL COMMENT '更新者ID',
    `is_deleted` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_permission_code` (`permission_code`),
    KEY `idx_parent_id` (`parent_id`),
    KEY `idx_permission_type` (`permission_type`),
    KEY `idx_status` (`status`),
    KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统权限表';

-- 创建角色权限关联表
CREATE TABLE `sys_role_permission` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT(20) NOT NULL COMMENT '权限ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT(20) NULL COMMENT '创建者ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_permission_id` (`permission_id`),
    CONSTRAINT `fk_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 创建用户角色关联表
CREATE TABLE `sys_user_role` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT(20) NULL COMMENT '创建者ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_role_id` (`role_id`),
    CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 初始化角色数据
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `status`, `sort_order`, `remark`) VALUES
(1, '系统管理员', 'admin', '拥有系统全部管理权限，可以进行用户管理、系统配置、数据分析等操作', 1, 1, '超级管理员角色'),
(2, '医生', 'doctor', '拥有医疗管理相关权限，可以进行健康管理、预警处理、评估报告等操作', 1, 2, '医生角色'),
(3, '家属', 'family', '拥有查看权限，可以查看关联老人的健康信息、报告等', 1, 3, '家属角色');

-- 初始化权限数据
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `permission_path`, `icon`, `description`, `status`, `sort_order`) VALUES
-- 一级菜单
(1, '首页仪表板', 'dashboard', 'menu', 0, '/dashboard', 'el-icon-monitor', '系统首页仪表板', 1, 1),
(2, '老人档案', 'elderly', 'menu', 0, '/elderly', 'el-icon-user', '老人档案管理', 1, 2),
(3, '健康预警', 'health-warning', 'menu', 0, '/health-warning', 'el-icon-warning', '健康预警管理', 1, 3),
(4, '评估报告', 'assessment', 'menu', 0, '/assessment', 'el-icon-document', '评估报告管理', 1, 4),
(5, '重点人群', 'key-population', 'menu', 0, '/key-population', 'el-icon-user-solid', '重点人群管理', 1, 5),
(6, '报表统计', 'statistics', 'menu', 0, '/statistics', 'el-icon-data-analysis', '报表统计', 1, 6),
(7, '设备管理', 'equipment', 'menu', 0, '/equipment', 'el-icon-cpu', '设备管理', 1, 7),
(8, '用户管理', 'user-management', 'menu', 0, '/user-management', 'el-icon-user', '用户管理', 1, 8),
(9, '大数据分析', 'big-data', 'menu', 0, '/big-data', 'el-icon-data-board', '大数据决策分析', 1, 9),
(10, '个人中心', 'profile', 'menu', 0, '/profile', 'el-icon-user', '个人中心', 1, 10),

-- 二级菜单 - 老人档案
(11, '老人列表', 'elderly:list', 'menu', 2, '/elderly/list', 'el-icon-user', '老人列表', 1, 11),
(12, '添加老人', 'elderly:add', 'menu', 2, '/elderly/add', 'el-icon-plus', '添加老人', 1, 12),
(13, '老人详情', 'elderly:detail', 'menu', 2, '/elderly/detail', 'el-icon-view', '老人详情', 1, 13),

-- 二级菜单 - 健康预警
(14, '预警列表', 'health-warning:list', 'menu', 3, '/health-warning/list', 'el-icon-warning', '预警列表', 1, 14),
(15, '预警处理', 'health-warning:handle', 'menu', 3, '/health-warning/handle', 'el-icon-edit', '预警处理', 1, 15),
(16, '预警统计', 'health-warning:statistics', 'menu', 3, '/health-warning/statistics', 'el-icon-data-analysis', '预警统计', 1, 16),

-- 二级菜单 - 用户管理
(17, '用户列表', 'user:list', 'menu', 8, '/user-management/list', 'el-icon-user', '用户列表', 1, 17),
(18, '角色管理', 'role:list', 'menu', 8, '/user-management/role', 'el-icon-user', '角色管理', 1, 18),
(19, '权限管理', 'permission:list', 'menu', 8, '/user-management/permission', 'el-icon-lock', '权限管理', 1, 19),

-- 按钮权限 - 老人档案
(20, '查看老人', 'elderly:view', 'button', 2, '', '', '查看老人详情', 1, 20),
(21, '编辑老人', 'elderly:edit', 'button', 2, '', '', '编辑老人信息', 1, 21),
(22, '删除老人', 'elderly:delete', 'button', 2, '', '', '删除老人', 1, 22),
(23, '导出老人', 'elderly:export', 'button', 2, '', '', '导出老人数据', 1, 23),

-- 按钮权限 - 健康预警
(24, '查看预警', 'health-warning:view', 'button', 3, '', '', '查看预警详情', 1, 24),
(25, '处理预警', 'health-warning:handle', 'button', 3, '', '', '处理预警', 1, 25),
(26, '删除预警', 'health-warning:delete', 'button', 3, '', '', '删除预警', 1, 26),
(27, '导出预警', 'health-warning:export', 'button', 3, '', '', '导出预警数据', 1, 27),

-- 按钮权限 - 用户管理
(28, '查看用户', 'user:view', 'button', 8, '', '', '查看用户详情', 1, 28),
(29, '添加用户', 'user:add', 'button', 8, '', '', '添加用户', 1, 29),
(30, '编辑用户', 'user:edit', 'button', 8, '', '', '编辑用户', 1, 30),
(31, '删除用户', 'user:delete', 'button', 8, '', '', '删除用户', 1, 31),
(32, '重置密码', 'user:reset-password', 'button', 8, '', '', '重置用户密码', 1, 32),

-- API权限
(33, '老人API', 'api:elderly', 'api', 0, '/api/elderly/**', '', '老人相关API', 1, 33),
(34, '健康预警API', 'api:health-warning', 'api', 0, '/api/health-warning/**', '', '健康预警相关API', 1, 34),
(35, '用户管理API', 'api:user', 'api', 0, '/api/user/**', '', '用户管理相关API', 1, 35),
(36, '统计API', 'api:statistics', 'api', 0, '/api/statistics/**', '', '统计相关API', 1, 36),
(37, '设备管理API', 'api:equipment', 'api', 0, '/api/equipment/**', '', '设备管理相关API', 1, 37);

-- 初始化角色权限关联 - 系统管理员拥有所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) 
SELECT 1, id FROM `sys_permission` WHERE status = 1;

-- 初始化角色权限关联 - 医生角色权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
-- 医生可以访问的菜单
(2, 1),  -- 首页仪表板
(2, 2),  -- 老人档案
(2, 3),  -- 健康预警
(2, 4),  -- 评估报告
(2, 5),  -- 重点人群
(2, 6),  -- 报表统计
(2, 10), -- 个人中心
-- 老人档案子菜单
(2, 11), -- 老人列表
(2, 12), -- 添加老人
(2, 13), -- 老人详情
-- 健康预警子菜单
(2, 14), -- 预警列表
(2, 15), -- 预警处理
(2, 16), -- 预警统计
-- 按钮权限
(2, 20), -- 查看老人
(2, 21), -- 编辑老人
(2, 23), -- 导出老人
(2, 24), -- 查看预警
(2, 25), -- 处理预警
(2, 27), -- 导出预警
-- API权限
(2, 33), -- 老人API
(2, 34), -- 健康预警API
(2, 36); -- 统计API

-- 初始化角色权限关联 - 家属角色权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
-- 家属可以访问的菜单
(3, 1),  -- 首页仪表板
(3, 2),  -- 老人档案
(3, 3),  -- 健康预警
(3, 4),  -- 评估报告
(3, 10), -- 个人中心
-- 老人档案子菜单
(3, 11), -- 老人列表
(3, 13), -- 老人详情
-- 健康预警子菜单
(3, 14), -- 预警列表
(3, 16), -- 预警统计
-- 按钮权限（只读）
(3, 20), -- 查看老人
(3, 24), -- 查看预警
-- API权限（只读）
(3, 33), -- 老人API（只读）
(3, 34), -- 健康预警API（只读）
(3, 36); -- 统计API（只读）

-- 初始化默认管理员用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `gender`, `phone`, `email`, `role_code`, `role_name`, `status`, `description`) VALUES
(1, 'admin', '$2a$10$7JB720yubVSNjkdqgAZvdeRYPjFPvXIh2UOmDWObzJUEwJjPMKQ3a', '系统管理员', 1, '13800000000', 'admin@smartcare.com', 'admin', '系统管理员', 1, '默认系统管理员账户'),
(2, 'doctor1', '$2a$10$7JB720yubVSNjkdqgAZvdeRYPjFPvXIh2UOmDWObzJUEwJjPMKQ3a', '张医生', 1, '13800000001', 'doctor1@smartcare.com', 'doctor', '医生', 1, '示例医生账户'),
(3, 'family1', '$2a$10$7JB720yubVSNjkdqgAZvdeRYPjFPvXIh2UOmDWObzJUEwJjPMKQ3a', '李家属', 0, '13800000002', 'family1@smartcare.com', 'family', '家属', 1, '示例家属账户');

-- 默认密码为：123456

-- 初始化用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- admin 用户 -> 系统管理员角色
(2, 2), -- doctor1 用户 -> 医生角色
(3, 3); -- family1 用户 -> 家属角色

-- 更新医生专属信息
UPDATE `sys_user` SET 
    `doctor_title` = '主任医师',
    `doctor_speciality` = '心血管内科',
    `doctor_license_number` = 'DOC202400001',
    `doctor_experience_years` = 15,
    `department` = '心血管内科'
WHERE `id` = 2;

-- 更新家属专属信息
UPDATE `sys_user` SET 
    `family_relationship` = '儿子',
    `family_elderly_ids` = '1,2'
WHERE `id` = 3;

-- 创建视图：用户权限视图
CREATE VIEW `v_user_permissions` AS
SELECT 
    u.id as user_id,
    u.username,
    u.real_name,
    u.role_code,
    u.role_name,
    p.permission_code,
    p.permission_name,
    p.permission_type,
    p.permission_path
FROM `sys_user` u
JOIN `sys_user_role` ur ON u.id = ur.user_id
JOIN `sys_role` r ON ur.role_id = r.id
JOIN `sys_role_permission` rp ON r.id = rp.role_id
JOIN `sys_permission` p ON rp.permission_id = p.id
WHERE u.is_deleted = 0 AND u.status = 1 
  AND r.is_deleted = 0 AND r.status = 1 
  AND p.is_deleted = 0 AND p.status = 1;

-- 创建视图：用户角色视图
CREATE VIEW `v_user_roles` AS
SELECT 
    u.id as user_id,
    u.username,
    u.real_name,
    u.role_code as user_role_code,
    r.id as role_id,
    r.role_name,
    r.role_code,
    r.description as role_description
FROM `sys_user` u
JOIN `sys_user_role` ur ON u.id = ur.user_id
JOIN `sys_role` r ON ur.role_id = r.id
WHERE u.is_deleted = 0 AND u.status = 1 
  AND r.is_deleted = 0 AND r.status = 1;

-- 显示初始化完成信息
SELECT '用户权限系统初始化完成！' as message;
SELECT '默认账户信息：' as info;
SELECT username, real_name, role_name, phone, email, '密码：123456' as password 
FROM sys_user WHERE id IN (1, 2, 3);
