-- ===================================================================
-- 智慧医养平台 - 性能优化索引脚本 (MySQL 5.7兼容版)
-- 
-- 用途: 为核心表添加性能优化索引，提升查询效率
-- 注意: 使用存储过程安全创建索引，避免重复创建错误
-- 兼容性: MySQL 5.7+
-- ===================================================================

USE smartcare_cloud;

-- ===================================================================
-- 创建辅助存储过程（安全创建索引）
-- ===================================================================

DELIMITER $$

-- 删除已存在的存储过程
DROP PROCEDURE IF EXISTS create_index_if_not_exists$$

-- 创建存储过程：安全创建索引
CREATE PROCEDURE create_index_if_not_exists(
    IN p_table_name VARCHAR(64),
    IN p_index_name VARCHAR(64),
    IN p_index_definition TEXT
)
BEGIN
    DECLARE index_exists INT DEFAULT 0;
    
    -- 检查索引是否存在
    SELECT COUNT(*) INTO index_exists
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name
      AND INDEX_NAME = p_index_name;
    
    -- 如果索引不存在，则创建
    IF index_exists = 0 THEN
        SET @sql = p_index_definition;
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;
        SELECT CONCAT('✓ 创建索引: ', p_index_name, ' on ', p_table_name) AS result;
    ELSE
        SELECT CONCAT('○ 索引已存在: ', p_index_name, ' on ', p_table_name) AS result;
    END IF;
END$$

DELIMITER ;

-- ===================================================================
-- 1. 老人档案表索引优化 (elderly_info)
-- ===================================================================

CALL create_index_if_not_exists('elderly_info', 'idx_elderly_name', 
    'CREATE INDEX idx_elderly_name ON elderly_info(name)');

CALL create_index_if_not_exists('elderly_info', 'idx_elderly_health_status', 
    'CREATE INDEX idx_elderly_health_status ON elderly_info(health_status)');

CALL create_index_if_not_exists('elderly_info', 'uk_elderly_id_card', 
    'CREATE UNIQUE INDEX uk_elderly_id_card ON elderly_info(id_card)');

CALL create_index_if_not_exists('elderly_info', 'idx_elderly_phone', 
    'CREATE INDEX idx_elderly_phone ON elderly_info(phone)');

CALL create_index_if_not_exists('elderly_info', 'idx_elderly_create_time', 
    'CREATE INDEX idx_elderly_create_time ON elderly_info(create_time)');

CALL create_index_if_not_exists('elderly_info', 'idx_elderly_health_create', 
    'CREATE INDEX idx_elderly_health_create ON elderly_info(health_status, create_time)');

CALL create_index_if_not_exists('elderly_info', 'idx_elderly_care_level', 
    'CREATE INDEX idx_elderly_care_level ON elderly_info(care_level)');

-- ===================================================================
-- 2. 健康预警表索引优化 (health_warning)
-- ===================================================================

CALL create_index_if_not_exists('health_warning', 'idx_warning_elderly_id', 
    'CREATE INDEX idx_warning_elderly_id ON health_warning(elderly_id)');

CALL create_index_if_not_exists('health_warning', 'idx_warning_level', 
    'CREATE INDEX idx_warning_level ON health_warning(warning_level)');

CALL create_index_if_not_exists('health_warning', 'idx_warning_status', 
    'CREATE INDEX idx_warning_status ON health_warning(status)');

CALL create_index_if_not_exists('health_warning', 'idx_warning_time', 
    'CREATE INDEX idx_warning_time ON health_warning(warning_time)');

CALL create_index_if_not_exists('health_warning', 'idx_warning_elderly_time', 
    'CREATE INDEX idx_warning_elderly_time ON health_warning(elderly_id, warning_time DESC)');

CALL create_index_if_not_exists('health_warning', 'idx_warning_status_level', 
    'CREATE INDEX idx_warning_status_level ON health_warning(status, warning_level)');

-- ===================================================================
-- 3. 健康记录表索引优化 (health_record)
-- ===================================================================

CALL create_index_if_not_exists('health_record', 'idx_record_elderly_id', 
    'CREATE INDEX idx_record_elderly_id ON health_record(elderly_id)');

CALL create_index_if_not_exists('health_record', 'idx_record_record_time', 
    'CREATE INDEX idx_record_record_time ON health_record(record_time)');

CALL create_index_if_not_exists('health_record', 'idx_record_type', 
    'CREATE INDEX idx_record_type ON health_record(record_type)');

CALL create_index_if_not_exists('health_record', 'idx_record_elderly_time', 
    'CREATE INDEX idx_record_elderly_time ON health_record(elderly_id, record_time DESC)');

-- ===================================================================
-- 4. 医生表索引优化 (doctor_info)
-- ===================================================================

CALL create_index_if_not_exists('doctor_info', 'uk_doctor_employee_number', 
    'CREATE UNIQUE INDEX uk_doctor_employee_number ON doctor_info(employee_number)');

CALL create_index_if_not_exists('doctor_info', 'idx_doctor_name', 
    'CREATE INDEX idx_doctor_name ON doctor_info(name)');

CALL create_index_if_not_exists('doctor_info', 'idx_doctor_department', 
    'CREATE INDEX idx_doctor_department ON doctor_info(department)');

CALL create_index_if_not_exists('doctor_info', 'idx_doctor_title', 
    'CREATE INDEX idx_doctor_title ON doctor_info(title)');

-- ===================================================================
-- 5. 用户表索引优化 (user)
-- ===================================================================

CALL create_index_if_not_exists('user', 'uk_user_username', 
    'CREATE UNIQUE INDEX uk_user_username ON user(username)');

CALL create_index_if_not_exists('user', 'uk_user_phone', 
    'CREATE UNIQUE INDEX uk_user_phone ON user(phone)');

CALL create_index_if_not_exists('user', 'idx_user_email', 
    'CREATE INDEX idx_user_email ON user(email)');

CALL create_index_if_not_exists('user', 'idx_user_role', 
    'CREATE INDEX idx_user_role ON user(role_type)');

CALL create_index_if_not_exists('user', 'idx_user_status', 
    'CREATE INDEX idx_user_status ON user(status)');

CALL create_index_if_not_exists('user', 'idx_user_role_status', 
    'CREATE INDEX idx_user_role_status ON user(role_type, status)');

-- ===================================================================
-- 6. 家属-老人关系表索引优化 (family_elderly_relation)
-- ===================================================================

CALL create_index_if_not_exists('family_elderly_relation', 'idx_family_relation_family_id', 
    'CREATE INDEX idx_family_relation_family_id ON family_elderly_relation(family_id)');

CALL create_index_if_not_exists('family_elderly_relation', 'idx_family_relation_elderly_id', 
    'CREATE INDEX idx_family_relation_elderly_id ON family_elderly_relation(elderly_id)');

CALL create_index_if_not_exists('family_elderly_relation', 'idx_family_relation_type', 
    'CREATE INDEX idx_family_relation_type ON family_elderly_relation(relation_type)');

CALL create_index_if_not_exists('family_elderly_relation', 'uk_family_elderly', 
    'CREATE UNIQUE INDEX uk_family_elderly ON family_elderly_relation(family_id, elderly_id)');

-- ===================================================================
-- 7. 设备管理表索引优化 (equipment)
-- ===================================================================

CALL create_index_if_not_exists('equipment', 'uk_equipment_number', 
    'CREATE UNIQUE INDEX uk_equipment_number ON equipment(equipment_number)');

CALL create_index_if_not_exists('equipment', 'idx_equipment_type', 
    'CREATE INDEX idx_equipment_type ON equipment(equipment_type)');

CALL create_index_if_not_exists('equipment', 'idx_equipment_status', 
    'CREATE INDEX idx_equipment_status ON equipment(status)');

CALL create_index_if_not_exists('equipment', 'idx_equipment_elderly_id', 
    'CREATE INDEX idx_equipment_elderly_id ON equipment(elderly_id)');

CALL create_index_if_not_exists('equipment', 'idx_equipment_purchase_date', 
    'CREATE INDEX idx_equipment_purchase_date ON equipment(purchase_date)');

CALL create_index_if_not_exists('equipment', 'idx_equipment_status_type', 
    'CREATE INDEX idx_equipment_status_type ON equipment(status, equipment_type)');

-- ===================================================================
-- 清理辅助存储过程
-- ===================================================================

DROP PROCEDURE IF EXISTS create_index_if_not_exists;

-- ===================================================================
-- 验证索引创建结果
-- ===================================================================

SELECT '============================================' AS '';
SELECT '【索引创建完成统计】' AS '';
SELECT '============================================' AS '';

-- 统计各表索引数量
SELECT 
    TABLE_NAME AS '表名',
    COUNT(DISTINCT INDEX_NAME) AS '索引数量'
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_NAME IN ('elderly_info', 'health_warning', 'health_record', 
                     'doctor_info', 'user', 'family_elderly_relation', 'equipment')
GROUP BY TABLE_NAME
ORDER BY TABLE_NAME;

SELECT '============================================' AS '';
SELECT '✅ 性能索引创建完成！' AS '';
SELECT '建议: 使用 EXPLAIN 分析查询语句验证索引效果' AS '';
SELECT '============================================' AS '';
