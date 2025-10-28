-- 创建角色权限系统表的简化版本
-- 删除现有表
DROP TABLE IF EXISTS `sys_user_role`;
DROP TABLE IF EXISTS `sys_role_permission`;
DROP TABLE IF EXISTS `sys_role`;
DROP TABLE IF EXISTS `sys_permission`;

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
    KEY `idx_permission_id` (`permission_id`)
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
    KEY `idx_role_id` (`role_id`)
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
(4, '用户管理', 'user-management', 'menu', 0, '/user-management', 'el-icon-user', '用户管理', 1, 4),
(5, '个人中心', 'profile', 'menu', 0, '/profile', 'el-icon-user', '个人中心', 1, 5);

-- 初始化角色权限关联 - 系统管理员拥有所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 1), (2, 2), (2, 3), (2, 5),
(3, 1), (3, 2), (3, 5);
