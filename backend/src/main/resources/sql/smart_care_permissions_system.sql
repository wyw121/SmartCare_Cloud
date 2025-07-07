-- 智慧医养平台权限系统初始化脚本
-- 创建日期: 2025-07-07
-- 描述: 初始化三角色（系统管理员、医生、家属）权限体系

-- ====================================
-- 1. 创建角色表数据
-- ====================================
INSERT INTO sys_role (id, role_name, role_code, description, status, create_time, update_time) VALUES
(1, '系统管理员', 'admin', '系统管理员，拥有系统全部管理权限', 1, NOW(), NOW()),
(2, '医生', 'doctor', '医生角色，拥有医疗管理相关权限', 1, NOW(), NOW()),
(3, '家属', 'family', '家属角色，只能查看关联老人的信息和健康状况', 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
role_name = VALUES(role_name),
description = VALUES(description),
update_time = NOW();

-- ====================================
-- 2. 创建权限表数据（分层结构）
-- ====================================

-- 一级菜单权限
INSERT INTO sys_permission (id, permission_name, permission_code, permission_type, parent_id, permission_path, icon, description, status, sort_order, create_time, update_time) VALUES
-- 仪表板
(100, '首页仪表板', 'dashboard', 'menu', 0, '/dashboard', 'DataBoard', '系统首页和数据概览', 1, 1, NOW(), NOW()),
-- 老人管理
(200, '老人档案管理', 'elderly', 'menu', 0, '/elderly', 'User', '老人档案信息管理', 1, 2, NOW(), NOW()),
-- 医生管理
(300, '医生管理', 'doctor', 'menu', 0, '/doctor', 'Avatar', '医生信息管理', 1, 3, NOW(), NOW()),
-- 健康预警
(400, '健康预警', 'health-warning', 'menu', 0, '/health-warning', 'Warning', '健康预警监控', 1, 4, NOW(), NOW()),
-- 设备管理
(500, '设备管理', 'equipment', 'menu', 0, '/equipment', 'Monitor', '医疗设备管理', 1, 5, NOW(), NOW()),
-- 报表统计
(600, '报表统计', 'reports', 'menu', 0, '/reports', 'DataAnalysis', '数据报表和统计分析', 1, 6, NOW(), NOW()),
-- 系统管理
(700, '系统管理', 'system', 'menu', 0, '/system', 'Setting', '系统配置和管理', 1, 7, NOW(), NOW())
ON DUPLICATE KEY UPDATE
permission_name = VALUES(permission_name),
description = VALUES(description),
update_time = NOW();

-- 二级菜单权限
INSERT INTO sys_permission (id, permission_name, permission_code, permission_type, parent_id, permission_path, icon, description, status, sort_order, create_time, update_time) VALUES
-- 仪表板子菜单
(101, '仪表板首页', 'dashboard:view', 'menu', 100, '/dashboard/index', 'Platform', '仪表板主页面', 1, 1, NOW(), NOW()),
(102, '模块化仪表板', 'dashboard:modular', 'menu', 100, '/dashboard/modular', 'Grid', '模块化仪表板', 1, 2, NOW(), NOW()),

-- 老人管理子菜单
(201, '老人列表', 'elderly:list', 'menu', 200, '/elderly/list', 'List', '老人信息列表', 1, 1, NOW(), NOW()),
(202, '添加老人', 'elderly:add', 'menu', 200, '/elderly/add', 'Plus', '添加老人档案', 1, 2, NOW(), NOW()),
(203, '老人详情', 'elderly:detail', 'menu', 200, '/elderly/detail', 'View', '查看老人详细信息', 1, 3, NOW(), NOW()),

-- 医生管理子菜单
(301, '医生列表', 'doctor:list', 'menu', 300, '/doctor/list', 'List', '医生信息列表', 1, 1, NOW(), NOW()),
(302, '添加医生', 'doctor:add', 'menu', 300, '/doctor/add', 'Plus', '添加医生信息', 1, 2, NOW(), NOW()),

-- 健康预警子菜单
(401, '预警列表', 'health-warning:list', 'menu', 400, '/health-warning/index', 'List', '健康预警列表', 1, 1, NOW(), NOW()),

-- 设备管理子菜单
(501, '设备列表', 'equipment:list', 'menu', 500, '/equipment/list', 'List', '设备信息列表', 1, 1, NOW(), NOW()),
(502, '设备监控', 'equipment:monitor', 'menu', 500, '/equipment/monitor', 'Monitor', '设备实时监控', 1, 2, NOW(), NOW()),

-- 报表统计子菜单
(601, '统计报表', 'reports:statistics', 'menu', 600, '/reports/statistics', 'PieChart', '统计报表查看', 1, 1, NOW(), NOW()),
(602, '数据分析', 'reports:analysis', 'menu', 600, '/reports/analysis', 'TrendCharts', '大数据分析', 1, 2, NOW(), NOW()),

-- 系统管理子菜单（仅管理员）
(701, '用户管理', 'system:users', 'menu', 700, '/system/users', 'User', '系统用户管理', 1, 1, NOW(), NOW()),
(702, '角色管理', 'system:roles', 'menu', 700, '/system/roles', 'Avatar', '角色权限管理', 1, 2, NOW(), NOW()),
(703, '权限管理', 'system:permissions', 'menu', 700, '/system/permissions', 'Key', '权限配置管理', 1, 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE
permission_name = VALUES(permission_name),
description = VALUES(description),
update_time = NOW();

-- 按钮/操作权限
INSERT INTO sys_permission (id, permission_name, permission_code, permission_type, parent_id, permission_path, icon, description, status, sort_order, create_time, update_time) VALUES
-- 老人管理操作权限
(2001, '查看老人', 'elderly:view', 'button', 200, '', '', '查看老人信息', 1, 1, NOW(), NOW()),
(2002, '新增老人', 'elderly:create', 'button', 200, '', '', '新增老人档案', 1, 2, NOW(), NOW()),
(2003, '编辑老人', 'elderly:edit', 'button', 200, '', '', '编辑老人信息', 1, 3, NOW(), NOW()),
(2004, '删除老人', 'elderly:delete', 'button', 200, '', '', '删除老人档案', 1, 4, NOW(), NOW()),
(2005, '导出老人', 'elderly:export', 'button', 200, '', '', '导出老人数据', 1, 5, NOW(), NOW()),

-- 医生管理操作权限
(3001, '查看医生', 'doctor:view', 'button', 300, '', '', '查看医生信息', 1, 1, NOW(), NOW()),
(3002, '新增医生', 'doctor:create', 'button', 300, '', '', '新增医生账户', 1, 2, NOW(), NOW()),
(3003, '编辑医生', 'doctor:edit', 'button', 300, '', '', '编辑医生信息', 1, 3, NOW(), NOW()),
(3004, '删除医生', 'doctor:delete', 'button', 300, '', '', '删除医生账户', 1, 4, NOW(), NOW()),

-- 健康预警操作权限
(4001, '查看预警', 'health-warning:view', 'button', 400, '', '', '查看健康预警', 1, 1, NOW(), NOW()),
(4002, '处理预警', 'health-warning:handle', 'button', 400, '', '', '处理健康预警', 1, 2, NOW(), NOW()),
(4003, '导出预警', 'health-warning:export', 'button', 400, '', '', '导出预警数据', 1, 3, NOW(), NOW()),

-- 设备管理操作权限
(5001, '查看设备', 'equipment:view', 'button', 500, '', '', '查看设备信息', 1, 1, NOW(), NOW()),
(5002, '新增设备', 'equipment:create', 'button', 500, '', '', '新增设备信息', 1, 2, NOW(), NOW()),
(5003, '编辑设备', 'equipment:edit', 'button', 500, '', '', '编辑设备信息', 1, 3, NOW(), NOW()),
(5004, '删除设备', 'equipment:delete', 'button', 500, '', '', '删除设备信息', 1, 4, NOW(), NOW()),
(5005, '设备监控', 'equipment:monitor', 'button', 500, '', '', '实时监控设备', 1, 5, NOW(), NOW()),

-- 报表统计操作权限
(6001, '查看报表', 'reports:view', 'button', 600, '', '', '查看统计报表', 1, 1, NOW(), NOW()),
(6002, '导出报表', 'reports:export', 'button', 600, '', '', '导出报表数据', 1, 2, NOW(), NOW()),
(6003, '数据分析', 'reports:analyze', 'button', 600, '', '', '进行数据分析', 1, 3, NOW(), NOW()),

-- 系统管理操作权限（仅管理员）
(7001, '用户查看', 'system:user:view', 'button', 701, '', '', '查看系统用户', 1, 1, NOW(), NOW()),
(7002, '用户新增', 'system:user:create', 'button', 701, '', '', '新增系统用户', 1, 2, NOW(), NOW()),
(7003, '用户编辑', 'system:user:edit', 'button', 701, '', '', '编辑系统用户', 1, 3, NOW(), NOW()),
(7004, '用户删除', 'system:user:delete', 'button', 701, '', '', '删除系统用户', 1, 4, NOW(), NOW()),
(7005, '重置密码', 'system:user:reset', 'button', 701, '', '', '重置用户密码', 1, 5, NOW(), NOW()),

(7011, '角色查看', 'system:role:view', 'button', 702, '', '', '查看系统角色', 1, 1, NOW(), NOW()),
(7012, '角色新增', 'system:role:create', 'button', 702, '', '', '新增系统角色', 1, 2, NOW(), NOW()),
(7013, '角色编辑', 'system:role:edit', 'button', 702, '', '', '编辑系统角色', 1, 3, NOW(), NOW()),
(7014, '角色删除', 'system:role:delete', 'button', 702, '', '', '删除系统角色', 1, 4, NOW(), NOW()),
(7015, '分配权限', 'system:role:assign', 'button', 702, '', '', '分配角色权限', 1, 5, NOW(), NOW()),

(7021, '权限查看', 'system:permission:view', 'button', 703, '', '', '查看系统权限', 1, 1, NOW(), NOW()),
(7022, '权限新增', 'system:permission:create', 'button', 703, '', '', '新增系统权限', 1, 2, NOW(), NOW()),
(7023, '权限编辑', 'system:permission:edit', 'button', 703, '', '', '编辑系统权限', 1, 3, NOW(), NOW()),
(7024, '权限删除', 'system:permission:delete', 'button', 703, '', '', '删除系统权限', 1, 4, NOW(), NOW())
ON DUPLICATE KEY UPDATE
permission_name = VALUES(permission_name),
description = VALUES(description),
update_time = NOW();

-- ====================================
-- 3. 角色权限关联表数据
-- ====================================

-- 系统管理员权限（所有权限）
INSERT INTO sys_role_permission (role_id, permission_id, create_time) 
SELECT 1, id, NOW() FROM sys_permission WHERE status = 1
ON DUPLICATE KEY UPDATE create_time = VALUES(create_time);

-- 医生权限
DELETE FROM sys_role_permission WHERE role_id = 2;
INSERT INTO sys_role_permission (role_id, permission_id, create_time) VALUES
-- 仪表板权限
(2, 100, NOW()), (2, 101, NOW()), (2, 102, NOW()),
-- 老人管理权限（除删除外）
(2, 200, NOW()), (2, 201, NOW()), (2, 202, NOW()), (2, 203, NOW()),
(2, 2001, NOW()), (2, 2002, NOW()), (2, 2003, NOW()), (2, 2005, NOW()),
-- 医生管理权限（查看和编辑自己）
(2, 300, NOW()), (2, 301, NOW()),
(2, 3001, NOW()), (2, 3003, NOW()),
-- 健康预警权限
(2, 400, NOW()), (2, 401, NOW()),
(2, 4001, NOW()), (2, 4002, NOW()), (2, 4003, NOW()),
-- 设备管理权限
(2, 500, NOW()), (2, 501, NOW()), (2, 502, NOW()),
(2, 5001, NOW()), (2, 5002, NOW()), (2, 5003, NOW()), (2, 5004, NOW()), (2, 5005, NOW()),
-- 报表统计权限
(2, 600, NOW()), (2, 601, NOW()), (2, 602, NOW()),
(2, 6001, NOW()), (2, 6002, NOW()), (2, 6003, NOW())
ON DUPLICATE KEY UPDATE create_time = VALUES(create_time);

-- 家属权限（只读权限）
DELETE FROM sys_role_permission WHERE role_id = 3;
INSERT INTO sys_role_permission (role_id, permission_id, create_time) VALUES
-- 仪表板权限
(3, 100, NOW()), (3, 101, NOW()),
-- 老人管理权限（仅查看关联老人）
(3, 200, NOW()), (3, 201, NOW()), (3, 203, NOW()),
(3, 2001, NOW()),
-- 健康预警权限（仅查看）
(3, 400, NOW()), (3, 401, NOW()),
(3, 4001, NOW()),
-- 报表统计权限（基础查看）
(3, 600, NOW()), (3, 601, NOW()),
(3, 6001, NOW())
ON DUPLICATE KEY UPDATE create_time = VALUES(create_time);

-- ====================================
-- 4. 创建默认用户账户
-- ====================================

-- 插入默认管理员账户
INSERT INTO sys_user (id, username, password, email, real_name, phone, role_id, status, create_time, update_time) VALUES
(1, 'admin', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCShPZOYgYCM7dqbzYMNMQFB5G7FK', 'admin@smartcare.com', '系统管理员', '13800138000', 1, 1, NOW(), NOW()),
(2, 'doctor_test', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCShPZOYgYCM7dqbzYMNMQFB5G7FK', 'doctor@smartcare.com', '测试医生', '13800138001', 2, 1, NOW(), NOW()),
(3, 'family_test', '$2a$10$N.ZOn9G6/YLFixAOPMg/h.z7pCShPZOYgYCM7dqbzYMNMQFB5G7FK', 'family@smartcare.com', '测试家属', '13800138002', 3, 1, NOW(), NOW())
ON DUPLICATE KEY UPDATE
real_name = VALUES(real_name),
phone = VALUES(phone),
role_id = VALUES(role_id),
update_time = NOW();

-- ====================================
-- 5. 用户角色关联表数据
-- ====================================
INSERT INTO sys_user_role (user_id, role_id, create_time) VALUES
(1, 1, NOW()),
(2, 2, NOW()),
(3, 3, NOW())
ON DUPLICATE KEY UPDATE create_time = VALUES(create_time);

-- ====================================
-- 权限系统初始化完成
-- ====================================

-- 查询验证结果
SELECT '权限系统初始化完成！' as message;
SELECT 
    r.role_name as '角色名称',
    COUNT(rp.permission_id) as '权限数量'
FROM sys_role r 
LEFT JOIN sys_role_permission rp ON r.id = rp.role_id 
LEFT JOIN sys_permission p ON rp.permission_id = p.id AND p.status = 1
GROUP BY r.id, r.role_name
ORDER BY r.id;
