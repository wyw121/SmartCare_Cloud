-- ===================================================================
-- 智慧医养平台 - 性能优化索引脚本
-- 
-- 用途: 为核心表添加性能优化索引，提升查询效率
-- 注意: 请在业务低峰期执行，大表创建索引可能需要较长时间
-- 执行前: 建议先在测试环境验证
-- ===================================================================

USE smartcare_cloud;

-- ===================================================================
-- 1. 老人档案表索引优化
-- ===================================================================
-- 检查表是否存在
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
    WHERE table_schema = 'smartcare_cloud' AND table_name = 'elderly_info');

-- 如果表存在，添加索引
SET @sql = IF(@table_exists > 0, '
-- 姓名查询索引
CREATE INDEX IF NOT EXISTS idx_elderly_name ON elderly_info(name);

-- 健康状态索引
CREATE INDEX IF NOT EXISTS idx_elderly_health_status ON elderly_info(health_status);

-- 身份证号唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_elderly_id_card ON elderly_info(id_card);

-- 联系电话索引
CREATE INDEX IF NOT EXISTS idx_elderly_phone ON elderly_info(phone);

-- 创建时间索引
CREATE INDEX IF NOT EXISTS idx_elderly_create_time ON elderly_info(create_time);

-- 复合索引：健康状态+创建时间
CREATE INDEX IF NOT EXISTS idx_elderly_health_create ON elderly_info(health_status, create_time);

-- 照护等级索引
CREATE INDEX IF NOT EXISTS idx_elderly_care_level ON elderly_info(care_level);
', 'SELECT "elderly_info表不存在，跳过" AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ===================================================================
-- 2. 健康预警表索引优化
-- ===================================================================
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
    WHERE table_schema = 'smartcare_cloud' AND table_name = 'health_warning');

SET @sql = IF(@table_exists > 0, '
-- 老人ID索引
CREATE INDEX IF NOT EXISTS idx_warning_elderly_id ON health_warning(elderly_id);

-- 预警状态索引
CREATE INDEX IF NOT EXISTS idx_warning_status ON health_warning(status);

-- 预警等级索引
CREATE INDEX IF NOT EXISTS idx_warning_level ON health_warning(warning_level);

-- 处理人ID索引
CREATE INDEX IF NOT EXISTS idx_warning_handler_id ON health_warning(handler_id);

-- 创建时间索引
CREATE INDEX IF NOT EXISTS idx_warning_create_time ON health_warning(create_time);

-- 复合索引：老人ID+状态
CREATE INDEX IF NOT EXISTS idx_warning_elderly_status ON health_warning(elderly_id, status);

-- 复合索引：状态+等级+创建时间
CREATE INDEX IF NOT EXISTS idx_warning_status_level_time ON health_warning(status, warning_level, create_time);

-- 复合索引：处理人+状态
CREATE INDEX IF NOT EXISTS idx_warning_handler_status ON health_warning(handler_id, status);
', 'SELECT "health_warning表不存在，跳过" AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ===================================================================
-- 3. 健康记录表索引优化
-- ===================================================================
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
    WHERE table_schema = 'smartcare_cloud' AND table_name = 'health_record');

SET @sql = IF(@table_exists > 0, '
-- 老人ID索引
CREATE INDEX IF NOT EXISTS idx_record_elderly_id ON health_record(elderly_id);

-- 记录时间索引
CREATE INDEX IF NOT EXISTS idx_record_record_time ON health_record(record_time);

-- 记录类型索引
CREATE INDEX IF NOT EXISTS idx_record_type ON health_record(record_type);

-- 复合索引：老人ID+记录时间（降序）
CREATE INDEX IF NOT EXISTS idx_record_elderly_time ON health_record(elderly_id, record_time DESC);
', 'SELECT "health_record表不存在，跳过" AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ===================================================================
-- 4. 医生表索引优化
-- ===================================================================
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
    WHERE table_schema = 'smartcare_cloud' AND table_name = 'doctor_info');

SET @sql = IF(@table_exists > 0, '
-- 工号唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_doctor_employee_number ON doctor_info(employee_number);

-- 姓名索引
CREATE INDEX IF NOT EXISTS idx_doctor_name ON doctor_info(name);

-- 科室索引
CREATE INDEX IF NOT EXISTS idx_doctor_department ON doctor_info(department);

-- 职称索引
CREATE INDEX IF NOT EXISTS idx_doctor_title ON doctor_info(title);

-- 联系电话索引
CREATE INDEX IF NOT EXISTS idx_doctor_phone ON doctor_info(phone);

-- 复合索引：科室+职称
CREATE INDEX IF NOT EXISTS idx_doctor_dept_title ON doctor_info(department, title);
', 'SELECT "doctor_info表不存在，跳过" AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ===================================================================
-- 5. 用户表索引优化
-- ===================================================================
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
    WHERE table_schema = 'smartcare_cloud' AND table_name = 'user');

SET @sql = IF(@table_exists > 0, '
-- 用户名唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_username ON user(username);

-- 手机号唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_phone ON user(phone);

-- 角色索引
CREATE INDEX IF NOT EXISTS idx_user_role ON user(role_id);

-- 状态索引
CREATE INDEX IF NOT EXISTS idx_user_status ON user(status);

-- 复合索引：角色+状态
CREATE INDEX IF NOT EXISTS idx_user_role_status ON user(role_id, status);
', 'SELECT "user表不存在，跳过" AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ===================================================================
-- 6. 家属老人关联表索引优化
-- ===================================================================
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
    WHERE table_schema = 'smartcare_cloud' AND table_name = 'family_elderly_relation');

SET @sql = IF(@table_exists > 0, '
-- 家属用户ID索引
CREATE INDEX IF NOT EXISTS idx_relation_family_user_id ON family_elderly_relation(family_user_id);

-- 老人ID索引
CREATE INDEX IF NOT EXISTS idx_relation_elderly_id ON family_elderly_relation(elderly_id);

-- 关系类型索引
CREATE INDEX IF NOT EXISTS idx_relation_relationship ON family_elderly_relation(relationship);

-- 唯一约束：防止重复关联
CREATE UNIQUE INDEX IF NOT EXISTS uk_relation_family_elderly ON family_elderly_relation(family_user_id, elderly_id);
', 'SELECT "family_elderly_relation表不存在，跳过" AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ===================================================================
-- 7. 设备表索引优化
-- ===================================================================
SET @table_exists = (SELECT COUNT(*) FROM information_schema.tables 
    WHERE table_schema = 'smartcare_cloud' AND table_name = 'equipment');

SET @sql = IF(@table_exists > 0, '
-- 设备编号唯一索引
CREATE UNIQUE INDEX IF NOT EXISTS uk_equipment_code ON equipment(equipment_code);

-- 设备状态索引
CREATE INDEX IF NOT EXISTS idx_equipment_status ON equipment(status);

-- 设备类型索引
CREATE INDEX IF NOT EXISTS idx_equipment_type ON equipment(equipment_type);

-- 复合索引：类型+状态
CREATE INDEX IF NOT EXISTS idx_equipment_type_status ON equipment(equipment_type, status);
', 'SELECT "equipment表不存在，跳过" AS message');

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- ===================================================================
-- 索引创建完成提示
-- ===================================================================
SELECT '==================================' AS '';
SELECT '性能优化索引创建完成！' AS 'Status';
SELECT '==================================' AS '';

-- 查看已创建的索引
SELECT 
    TABLE_NAME AS '表名',
    INDEX_NAME AS '索引名',
    COLUMN_NAME AS '列名',
    INDEX_TYPE AS '索引类型',
    NON_UNIQUE AS '是否非唯一'
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;
