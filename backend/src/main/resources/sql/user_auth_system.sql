-- 用户权限系统相关表
-- 创建用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密后）',
    `name` VARCHAR(50) NOT NULL COMMENT '真实姓名',
    `gender` TINYINT(1) NULL COMMENT '性别：1-男，0-女',
    `phone` VARCHAR(20) NULL COMMENT '手机号',
    `email` VARCHAR(100) NULL COMMENT '邮箱',
    `avatar` VARCHAR(255) NULL COMMENT '头像URL',
    `department` VARCHAR(100) NULL COMMENT '部门',
    `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
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

-- 创建权限表
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

-- 创建角色权限关联表
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

-- 创建用户角色关联表（支持一个用户多个角色）
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

-- 插入基础角色数据
INSERT INTO `sys_role` (`id`, `role_name`, `role_code`, `description`, `status`, `sort_order`) VALUES
(1, '系统管理员', 'admin', '拥有系统全部权限，负责平台整体运营管理', 1, 1),
(2, '医生', 'doctor', '拥有医疗管理相关权限，负责老人医疗健康管理', 1, 2),
(3, '家属', 'family', '拥有查看权限，可查看关联老人的基本信息和健康状况', 1, 3);

-- 插入基础权限数据
INSERT INTO `sys_permission` (`id`, `permission_name`, `permission_code`, `permission_type`, `parent_id`, `permission_path`, `icon`, `description`, `status`, `sort_order`) VALUES
-- 仪表板
(1, '仪表板', 'dashboard', 'menu', 0, '/dashboard', 'Dashboard', '系统首页仪表板', 1, 1),
(2, '查看仪表板', 'dashboard:view', 'api', 1, '', '', '查看仪表板数据', 1, 1),

-- 老人档案管理
(10, '老人档案', 'elderly', 'menu', 0, '/elderly', 'User', '老人档案管理', 1, 2),
(11, '查看老人档案', 'elderly:view', 'api', 10, '', '', '查看老人档案信息', 1, 1),
(12, '新增老人档案', 'elderly:add', 'api', 10, '', '', '新增老人档案', 1, 2),
(13, '编辑老人档案', 'elderly:edit', 'api', 10, '', '', '编辑老人档案', 1, 3),
(14, '删除老人档案', 'elderly:delete', 'api', 10, '', '', '删除老人档案', 1, 4),

-- 健康预警
(20, '健康预警', 'health-warning', 'menu', 0, '/health-warning', 'Warning', '健康预警管理', 1, 3),
(21, '查看预警', 'warning:view', 'api', 20, '', '', '查看健康预警信息', 1, 1),
(22, '处理预警', 'warning:handle', 'api', 20, '', '', '处理健康预警', 1, 2),
(23, '忽略预警', 'warning:ignore', 'api', 20, '', '', '忽略健康预警', 1, 3),

-- 健康管理
(30, '健康管理', 'health', 'menu', 0, '/health', 'Postcard', '健康数据管理', 1, 4),
(31, '查看健康数据', 'health:view', 'api', 30, '', '', '查看健康数据', 1, 1),
(32, '管理健康数据', 'health:manage', 'api', 30, '', '', '管理健康数据', 1, 2),

-- 评估报告
(40, '评估报告', 'report', 'menu', 0, '/report', 'Document', '评估报告管理', 1, 5),
(41, '查看报告', 'report:view', 'api', 40, '', '', '查看评估报告', 1, 1),
(42, '创建报告', 'report:create', 'api', 40, '', '', '创建评估报告', 1, 2),
(43, '导出报告', 'report:export', 'api', 40, '', '', '导出评估报告', 1, 3),

-- 设备管理
(50, '设备管理', 'equipment', 'menu', 0, '/equipment', 'Monitor', '设备管理', 1, 6),
(51, '查看设备', 'equipment:view', 'api', 50, '', '', '查看设备信息', 1, 1),
(52, '管理设备', 'equipment:manage', 'api', 50, '', '', '管理设备', 1, 2),

-- 报表统计
(60, '报表统计', 'statistics', 'menu', 0, '/statistics', 'DataAnalysis', '报表统计', 1, 7),
(61, '查看统计', 'statistics:view', 'api', 60, '', '', '查看统计数据', 1, 1),

-- 数据分析
(70, '数据分析', 'analysis', 'menu', 0, '/analysis', 'TrendCharts', '大数据分析', 1, 8),
(71, '查看分析', 'analysis:view', 'api', 70, '', '', '查看数据分析', 1, 1),

-- 系统管理
(80, '系统管理', 'system', 'menu', 0, '/system', 'Setting', '系统管理', 1, 9),
(81, '用户管理', 'system:user', 'menu', 80, '/system/users', 'User', '用户管理', 1, 1),
(82, '角色管理', 'system:role', 'menu', 80, '/system/roles', 'UserFilled', '角色管理', 1, 2),
(83, '权限管理', 'system:permission', 'menu', 80, '/system/permissions', 'Lock', '权限管理', 1, 3),
(84, '查看用户', 'user:view', 'api', 81, '', '', '查看用户信息', 1, 1),
(85, '新增用户', 'user:add', 'api', 81, '', '', '新增用户', 1, 2),
(86, '编辑用户', 'user:edit', 'api', 81, '', '', '编辑用户', 1, 3),
(87, '删除用户', 'user:delete', 'api', 81, '', '', '删除用户', 1, 4),
(88, '查看角色', 'role:view', 'api', 82, '', '', '查看角色信息', 1, 1),
(89, '管理角色', 'role:manage', 'api', 82, '', '', '管理角色', 1, 2),
(90, '查看权限', 'permission:view', 'api', 83, '', '', '查看权限信息', 1, 1),
(91, '管理权限', 'permission:manage', 'api', 83, '', '', '管理权限', 1, 2);

-- 插入角色权限关联数据
-- 管理员拥有所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) 
SELECT 1, id FROM `sys_permission` WHERE `status` = 1;

-- 医生权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(2, 1), (2, 2),   -- 仪表板
(2, 10), (2, 11), (2, 12), (2, 13), -- 老人档案（不能删除）
(2, 20), (2, 21), (2, 22), (2, 23), -- 健康预警
(2, 30), (2, 31), (2, 32), -- 健康管理
(2, 40), (2, 41), (2, 42), (2, 43), -- 评估报告
(2, 50), (2, 51), -- 设备管理（只能查看）
(2, 60), (2, 61), -- 报表统计
(2, 70), (2, 71); -- 数据分析

-- 家属权限（只有查看权限）
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(3, 1), (3, 2),   -- 仪表板
(3, 10), (3, 11), -- 老人档案（只能查看）
(3, 20), (3, 21), -- 健康预警（只能查看）
(3, 30), (3, 31), -- 健康管理（只能查看）
(3, 40), (3, 41); -- 评估报告（只能查看）

-- 插入默认用户数据
INSERT INTO `sys_user` (`id`, `username`, `password`, `name`, `phone`, `email`, `role_id`, `role_code`, `role_name`, `status`, `description`) VALUES
(1, 'admin', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCa6VHOzMUJCcP1/9YZh/oVQAqL7q', '系统管理员', '13800138000', 'admin@smartcare.com', 1, 'admin', '系统管理员', 1, '系统默认管理员账户'),
(2, 'doctor', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCa6VHOzMUJCcP1/9YZh/oVQAqL7q', '张医生', '13800138001', 'doctor@smartcare.com', 2, 'doctor', '医生', 1, '系统默认医生账户'),
(3, 'family', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCa6VHOzMUJCcP1/9YZh/oVQAqL7q', '李家属', '13800138002', 'family@smartcare.com', 3, 'family', '家属', 1, '系统默认家属账户');

-- 插入用户角色关联数据
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1), -- admin 拥有管理员角色
(2, 2), -- doctor 拥有医生角色
(3, 3); -- family 拥有家属角色

-- 注：密码 123456 的BCrypt加密结果为 $2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCa6VHOzMUJCcP1/9YZh/oVQAqL7q
