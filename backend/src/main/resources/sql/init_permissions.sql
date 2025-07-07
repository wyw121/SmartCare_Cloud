-- 智慧医养平台权限系统初始化脚本
USE smartcare_cloud;

-- 检查并创建权限相关表
CREATE TABLE IF NOT EXISTS `sys_permission` (
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

CREATE TABLE IF NOT EXISTS `sys_role` (
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

CREATE TABLE IF NOT EXISTS `sys_role_permission` (
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

CREATE TABLE IF NOT EXISTS `sys_user_role` (
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

-- 初始化系统角色
INSERT IGNORE INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `status`, `sort_order`) VALUES
(1, '系统管理员', 'admin', '拥有系统全部管理权限，包括用户管理、角色管理、权限管理等', 1, 1),
(2, '医生', 'doctor', '拥有医疗管理相关权限，可进行老人健康管理、预警处理、评估报告等操作', 1, 2),
(3, '家属', 'family', '拥有查看权限，可查看关联老人的健康信息、报告等，但不能进行修改操作', 1, 3);

-- 初始化系统权限
INSERT IGNORE INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `permission_path`, `icon`, `description`, `status`, `sort_order`) VALUES
-- 一级菜单权限
(1, '首页仪表板', 'dashboard:view', 'menu', 0, '/dashboard', 'Monitor', '系统首页仪表板访问权限', 1, 1),
(2, '老人档案', 'elderly:menu', 'menu', 0, '/elderly', 'User', '老人档案管理菜单', 1, 2),
(3, '健康预警', 'health-warning:menu', 'menu', 0, '/health-warning', 'Warning', '健康预警管理菜单', 1, 3),
(4, '评估报告', 'assessment:menu', 'menu', 0, '/assessment', 'Document', '评估报告管理菜单', 1, 4),
(5, '重点人群', 'key-population:menu', 'menu', 0, '/key-population', 'UserSolid', '重点人群管理菜单', 1, 5),
(6, '报表统计', 'statistics:menu', 'menu', 0, '/statistics', 'DataAnalysis', '报表统计菜单', 1, 6),
(7, '设备管理', 'equipment:menu', 'menu', 0, '/equipment', 'Cpu', '设备管理菜单', 1, 7),
(8, '用户管理', 'user-management:menu', 'menu', 0, '/user-management', 'User', '用户管理菜单（仅管理员）', 1, 8),
(9, '大数据分析', 'big-data:menu', 'menu', 0, '/big-data', 'DataBoard', '大数据决策分析菜单', 1, 9),
(10, '个人中心', 'profile:view', 'menu', 0, '/profile', 'User', '个人中心访问权限', 1, 10),

-- 老人档案子权限
(11, '查看老人列表', 'elderly:list', 'button', 2, '', '', '查看老人列表权限', 1, 11),
(12, '查看老人详情', 'elderly:view', 'button', 2, '', '', '查看老人详情权限', 1, 12),
(13, '添加老人', 'elderly:add', 'button', 2, '', '', '添加老人权限', 1, 13),
(14, '编辑老人', 'elderly:edit', 'button', 2, '', '', '编辑老人信息权限', 1, 14),
(15, '删除老人', 'elderly:delete', 'button', 2, '', '', '删除老人权限', 1, 15),
(16, '导出老人数据', 'elderly:export', 'button', 2, '', '', '导出老人数据权限', 1, 16),

-- 健康预警子权限
(17, '查看预警列表', 'health-warning:list', 'button', 3, '', '', '查看健康预警列表权限', 1, 17),
(18, '查看预警详情', 'health-warning:view', 'button', 3, '', '', '查看健康预警详情权限', 1, 18),
(19, '处理预警', 'health-warning:handle', 'button', 3, '', '', '处理健康预警权限', 1, 19),
(20, '删除预警', 'health-warning:delete', 'button', 3, '', '', '删除健康预警权限', 1, 20),
(21, '预警统计', 'health-warning:statistics', 'button', 3, '', '', '健康预警统计权限', 1, 21),

-- 用户管理子权限（仅管理员）
(22, '用户列表', 'user:list', 'button', 8, '', '', '查看用户列表权限', 1, 22),
(23, '添加用户', 'user:add', 'button', 8, '', '', '添加用户权限', 1, 23),
(24, '编辑用户', 'user:edit', 'button', 8, '', '', '编辑用户权限', 1, 24),
(25, '删除用户', 'user:delete', 'button', 8, '', '', '删除用户权限', 1, 25),
(26, '重置密码', 'user:reset-password', 'button', 8, '', '', '重置用户密码权限', 1, 26),
(27, '角色管理', 'role:manage', 'button', 8, '', '', '角色管理权限', 1, 27),
(28, '权限管理', 'permission:manage', 'button', 8, '', '', '权限管理权限', 1, 28),

-- 设备管理子权限
(29, '查看设备', 'equipment:view', 'button', 7, '', '', '查看设备权限', 1, 29),
(30, '管理设备', 'equipment:manage', 'button', 7, '', '', '管理设备权限', 1, 30),

-- API权限
(31, '老人管理API', 'api:elderly', 'api', 0, '/api/elderly/**', '', '老人管理相关API权限', 1, 31),
(32, '健康预警API', 'api:health-warning', 'api', 0, '/api/health-warning/**', '', '健康预警相关API权限', 1, 32),
(33, '用户管理API', 'api:user', 'api', 0, '/api/user/**', '', '用户管理相关API权限', 1, 33),
(34, '统计API', 'api:statistics', 'api', 0, '/api/statistics/**', '', '统计相关API权限', 1, 34),
(35, '设备管理API', 'api:equipment', 'api', 0, '/api/equipment/**', '', '设备管理相关API权限', 1, 35);

-- 为系统管理员角色分配所有权限
INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`) 
SELECT 1, id FROM `sys_permission` WHERE status = 1;

-- 为医生角色分配权限
INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
-- 基础菜单权限
(2, 1),   -- 首页仪表板
(2, 2),   -- 老人档案
(2, 3),   -- 健康预警
(2, 4),   -- 评估报告
(2, 5),   -- 重点人群
(2, 6),   -- 报表统计
(2, 7),   -- 设备管理
(2, 9),   -- 大数据分析
(2, 10),  -- 个人中心
-- 老人档案权限
(2, 11),  -- 查看老人列表
(2, 12),  -- 查看老人详情
(2, 13),  -- 添加老人
(2, 14),  -- 编辑老人
(2, 16),  -- 导出老人数据
-- 健康预警权限
(2, 17),  -- 查看预警列表
(2, 18),  -- 查看预警详情
(2, 19),  -- 处理预警
(2, 21),  -- 预警统计
-- 设备管理权限
(2, 29),  -- 查看设备
(2, 30),  -- 管理设备
-- API权限
(2, 31),  -- 老人管理API
(2, 32),  -- 健康预警API
(2, 34),  -- 统计API
(2, 35);  -- 设备管理API

-- 为家属角色分配权限（只读权限）
INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
-- 基础菜单权限
(3, 1),   -- 首页仪表板
(3, 2),   -- 老人档案
(3, 3),   -- 健康预警
(3, 4),   -- 评估报告
(3, 6),   -- 报表统计
(3, 10),  -- 个人中心
-- 老人档案权限（只读）
(3, 11),  -- 查看老人列表
(3, 12),  -- 查看老人详情
-- 健康预警权限（只读）
(3, 17),  -- 查看预警列表
(3, 18),  -- 查看预警详情
(3, 21),  -- 预警统计
-- 设备管理权限（只读）
(3, 29),  -- 查看设备
-- API权限（只读）
(3, 31),  -- 老人管理API（限制为只读）
(3, 32),  -- 健康预警API（限制为只读）
(3, 34);  -- 统计API

-- 为现有用户分配角色（如果用户存在的话）
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`)
SELECT u.id, 
       CASE 
           WHEN u.role_code = 'admin' THEN 1
           WHEN u.role_code = 'doctor' THEN 2
           WHEN u.role_code = 'family' THEN 3
           ELSE 1
       END as role_id
FROM `sys_user` u 
WHERE u.is_deleted = 0 AND u.status = 1;

-- 显示初始化结果
SELECT '权限系统初始化完成！' as message;
SELECT '角色权限统计：' as info;
SELECT r.role_name, COUNT(rp.permission_id) as permission_count
FROM sys_role r 
LEFT JOIN sys_role_permission rp ON r.id = rp.role_id 
WHERE r.status = 1 AND r.is_deleted = 0
GROUP BY r.id, r.role_name
ORDER BY r.sort_order;
