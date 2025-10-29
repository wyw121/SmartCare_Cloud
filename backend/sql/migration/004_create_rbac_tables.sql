-- =====================================================
-- 智慧医养平台 - RBAC权限系统补充脚本
-- 创建日期: 2025-10-29
-- 说明: 创建缺失的角色、权限、角色权限关联表
-- =====================================================

USE smartcare_cloud;

-- =====================================================
-- 1. 角色表 (sys_role)
-- =====================================================
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_code` VARCHAR(20) NOT NULL COMMENT '角色编码: system_admin, business_admin, doctor, nurse, social_worker, family',
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `role_desc` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
  `data_scope` VARCHAR(20) DEFAULT 'all' COMMENT '数据范围: all-全部, org-本机构, dept-本部门, self-仅本人',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1-启用, 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `is_deleted` TINYINT DEFAULT 0 COMMENT '删除标记: 0-未删除, 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_code` (`role_code`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- =====================================================
-- 2. 权限表 (sys_permission)
-- =====================================================
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_code` VARCHAR(50) NOT NULL COMMENT '权限编码',
  `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `permission_type` VARCHAR(20) DEFAULT NULL COMMENT '权限类型: menu-菜单, button-按钮, api-接口',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父权限ID',
  `resource_path` VARCHAR(200) DEFAULT NULL COMMENT '资源路径',
  `method` VARCHAR(10) DEFAULT NULL COMMENT 'HTTP方法: GET, POST, PUT, DELETE',
  `sort_order` INT DEFAULT 0 COMMENT '排序号',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1-启用, 0-禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_permission_code` (`permission_code`),
  INDEX `idx_parent_id` (`parent_id`),
  INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- =====================================================
-- 3. 角色-权限关联表 (sys_role_permission)
-- =====================================================
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '关联ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  INDEX `idx_role_id` (`role_id`),
  INDEX `idx_permission_id` (`permission_id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限关联表';

-- =====================================================
-- 初始化角色数据
-- =====================================================
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `role_desc`, `data_scope`, `status`) VALUES
(1, 'system_admin', '系统管理员', '系统最高权限,可以管理所有功能和数据', 'all', 1),
(2, 'business_admin', '业务管理员', '管理日常业务运营,可查看和管理所有老人档案', 'all', 1),
(3, 'doctor', '医生', '负责老人医疗服务,只能查看和管理责任范围内的老人', 'self', 1),
(4, 'nurse', '护工', '负责老人日常照护,只能查看和管理责任范围内的老人', 'self', 1),
(5, 'social_worker', '社工', '负责社会服务工作,可查看本机构的老人信息', 'org', 1),
(6, 'family', '家属', '查看关联老人的信息,权限最受限', 'self', 1);

-- =====================================================
-- 初始化权限数据 (基础权限)
-- =====================================================
INSERT INTO `sys_permission` (`id`, `permission_code`, `permission_name`, `permission_type`, `parent_id`, `resource_path`, `method`, `sort_order`, `status`) VALUES
-- 一级菜单
(1, 'dashboard', '首页仪表板', 'menu', NULL, '/dashboard', 'GET', 1, 1),
(2, 'elderly', '老人管理', 'menu', NULL, '/elderly', 'GET', 2, 1),
(3, 'health', '健康管理', 'menu', NULL, '/health', 'GET', 3, 1),
(4, 'nursing', '护理管理', 'menu', NULL, '/nursing', 'GET', 4, 1),
(5, 'doctor_manage', '医生管理', 'menu', NULL, '/doctor', 'GET', 5, 1),
(6, 'equipment', '设备管理', 'menu', NULL, '/equipment', 'GET', 6, 1),
(7, 'reports', '报表统计', 'menu', NULL, '/reports', 'GET', 7, 1),
(8, 'system', '系统管理', 'menu', NULL, '/system', 'GET', 8, 1),

-- 老人管理子权限
(10, 'elderly:view', '查看老人', 'button', 2, '/api/elderly/*', 'GET', 1, 1),
(11, 'elderly:add', '新增老人', 'button', 2, '/api/elderly', 'POST', 2, 1),
(12, 'elderly:edit', '编辑老人', 'button', 2, '/api/elderly/*', 'PUT', 3, 1),
(13, 'elderly:delete', '删除老人', 'button', 2, '/api/elderly/*', 'DELETE', 4, 1),
(14, 'elderly:export', '导出老人', 'button', 2, '/api/elderly/export', 'POST', 5, 1),

-- 健康管理子权限
(20, 'health:view', '查看健康记录', 'button', 3, '/api/health/*', 'GET', 1, 1),
(21, 'health:add', '新增健康记录', 'button', 3, '/api/health', 'POST', 2, 1),
(22, 'health:edit', '编辑健康记录', 'button', 3, '/api/health/*', 'PUT', 3, 1),
(23, 'health:delete', '删除健康记录', 'button', 3, '/api/health/*', 'DELETE', 4, 1),
(24, 'health-warning:view', '查看健康预警', 'button', 3, '/api/health-warning/*', 'GET', 5, 1),
(25, 'health-warning:handle', '处理健康预警', 'button', 3, '/api/health-warning/handle', 'POST', 6, 1),

-- 系统管理子权限
(30, 'system:user:manage', '用户管理', 'button', 8, '/api/system/users/*', 'POST', 1, 1),
(31, 'system:role:manage', '角色管理', 'button', 8, '/api/system/roles/*', 'POST', 2, 1),
(32, 'system:permission:manage', '权限管理', 'button', 8, '/api/system/permissions/*', 'POST', 3, 1);

-- =====================================================
-- 初始化角色-权限关联 (系统管理员拥有所有权限)
-- =====================================================
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `sys_permission` WHERE `status` = 1;

-- 业务管理员权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(2, 1), -- 首页
(2, 2), (2, 10), (2, 11), (2, 12), (2, 13), (2, 14), -- 老人管理全部
(2, 3), (2, 20), (2, 21), (2, 22), (2, 24), (2, 25), -- 健康管理
(2, 4), -- 护理管理
(2, 5), -- 医生管理
(2, 6), -- 设备管理
(2, 7); -- 报表统计

-- 医生权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(3, 1), -- 首页
(3, 2), (3, 10), (3, 11), (3, 12), (3, 14), -- 老人管理(无删除)
(3, 3), (3, 20), (3, 21), (3, 22), (3, 24), (3, 25), -- 健康管理全部
(3, 4), -- 护理管理
(3, 7); -- 报表统计

-- 护工权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(4, 1), -- 首页
(4, 2), (4, 10), -- 老人查看
(4, 3), (4, 20), -- 健康查看
(4, 4); -- 护理管理

-- 社工权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(5, 1), -- 首页
(5, 2), (5, 10), (5, 14), -- 老人查看和导出
(5, 7); -- 报表统计

-- 家属权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(6, 1), -- 首页
(6, 2), (6, 10), -- 老人查看
(6, 3), (6, 20), (6, 24); -- 健康记录和预警查看

-- =====================================================
-- 脚本执行完成
-- =====================================================
SELECT 'RBAC权限系统表创建完成!' AS Message;
SELECT 
    (SELECT COUNT(*) FROM sys_role) AS '角色数量',
    (SELECT COUNT(*) FROM sys_permission) AS '权限数量',
    (SELECT COUNT(*) FROM sys_role_permission) AS '角色权限关联数';
