-- 创建家属用户表
CREATE TABLE IF NOT EXISTS family_user (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  sys_user_id BIGINT NOT NULL COMMENT '系统用户ID（关联sys_user表）',
  real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
  gender TINYINT(1) DEFAULT 1 COMMENT '性别：1-男，2-女',
  phone VARCHAR(20) COMMENT '手机号码',
  email VARCHAR(100) COMMENT '邮箱地址',
  status TINYINT(1) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  create_by BIGINT COMMENT '创建人ID',
  update_by BIGINT COMMENT '更新人ID',
  UNIQUE KEY uk_sys_user_id (sys_user_id),
  INDEX idx_status (status),
  INDEX idx_phone (phone),
  INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属用户表';

-- 创建家属老人关系表
CREATE TABLE IF NOT EXISTS family_elderly_relation (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  family_user_id BIGINT NOT NULL COMMENT '家属用户ID',
  elderly_id BIGINT NOT NULL COMMENT '老人ID',
  relationship_type VARCHAR(20) NOT NULL COMMENT '关系类型',
  relationship_name VARCHAR(20) NOT NULL COMMENT '关系名称',
  is_primary TINYINT(1) DEFAULT 0 COMMENT '是否主要联系人：1-是，0-否',
  is_emergency TINYINT(1) DEFAULT 0 COMMENT '是否紧急联系人：1-是，0-否',
  contact_priority INT DEFAULT 5 COMMENT '联系优先级：1-最高，5-最低',
  visit_frequency VARCHAR(20) COMMENT '探视频率',
  authorized_operations JSON COMMENT '授权操作列表',
  start_date DATE COMMENT '关系开始日期',
  end_date DATE COMMENT '关系结束日期',
  status TINYINT(1) DEFAULT 1 COMMENT '状态：1-有效，0-无效',
  is_deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  create_by BIGINT COMMENT '创建人ID',
  update_by BIGINT COMMENT '更新人ID',
  UNIQUE KEY uk_family_elderly (family_user_id, elderly_id),
  INDEX idx_family_user (family_user_id),
  INDEX idx_elderly (elderly_id),
  INDEX idx_relationship (relationship_type),
  INDEX idx_status (status),
  INDEX idx_is_primary (is_primary),
  INDEX idx_is_emergency (is_emergency),
  INDEX idx_contact_priority (contact_priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家属老人关系表';

-- 插入测试家属用户数据
INSERT IGNORE INTO family_user 
(sys_user_id, real_name, gender, phone, email, status, create_by) 
VALUES (3, '李家属', 1, '13800138002', 'family@smartcare.com', 1, 1);

-- 插入测试关联数据
SET @family_user_id = (SELECT id FROM family_user WHERE sys_user_id = 3);

INSERT IGNORE INTO family_elderly_relation 
(family_user_id, elderly_id, relationship_type, relationship_name, 
is_primary, is_emergency, contact_priority, start_date, status, create_by) 
VALUES 
(@family_user_id, 1, 'child', '儿子', 1, 1, 1, CURDATE(), 1, 1),
(@family_user_id, 2, 'child', '女儿', 0, 1, 2, CURDATE(), 1, 1),
(@family_user_id, 3, 'child', '儿子', 0, 0, 3, CURDATE(), 1, 1),
(@family_user_id, 4, 'grandchild', '孙子', 0, 0, 4, CURDATE(), 1, 1),
(@family_user_id, 5, 'grandchild', '孙女', 0, 0, 5, CURDATE(), 1, 1);

-- 验证数据
SELECT '家属用户表数据:' as '结果';
SELECT * FROM family_user WHERE sys_user_id = 3;

SELECT '家属老人关系表数据:' as '结果'; 
SELECT * FROM family_elderly_relation WHERE family_user_id = @family_user_id;
