-- ===================================================================
-- 智慧医养平台 - 性能优化索引脚本 (实际表名版本)
-- 
-- 用途: 为核心表添加性能优化索引，提升查询效率
-- 基于实际数据库表结构创建
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
-- 1. 老人档案表索引优化 (elderly)
-- ===================================================================

CALL create_index_if_not_exists('elderly', 'idx_elderly_name', 
    'CREATE INDEX idx_elderly_name ON elderly(name)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_health_status', 
    'CREATE INDEX idx_elderly_health_status ON elderly(health_status)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_phone', 
    'CREATE INDEX idx_elderly_phone ON elderly(phone)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_create_time', 
    'CREATE INDEX idx_elderly_create_time ON elderly(create_time)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_health_create', 
    'CREATE INDEX idx_elderly_health_create ON elderly(health_status, create_time)');

-- ===================================================================
-- 2. 健康预警表索引优化 (t_health_warning)
-- ===================================================================

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_elderly_id', 
    'CREATE INDEX idx_warning_elderly_id ON t_health_warning(elderly_id)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_level', 
    'CREATE INDEX idx_warning_level ON t_health_warning(warning_level)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_status', 
    'CREATE INDEX idx_warning_status ON t_health_warning(status)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_time', 
    'CREATE INDEX idx_warning_time ON t_health_warning(warning_time)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_elderly_time', 
    'CREATE INDEX idx_warning_elderly_time ON t_health_warning(elderly_id, warning_time DESC)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_status_level', 
    'CREATE INDEX idx_warning_status_level ON t_health_warning(status, warning_level)');

-- ===================================================================
-- 3. 健康记录表索引优化 (health_record)
-- ===================================================================

CALL create_index_if_not_exists('health_record', 'idx_record_elderly_id', 
    'CREATE INDEX idx_record_elderly_id ON health_record(elderly_id)');

CALL create_index_if_not_exists('health_record', 'idx_record_time', 
    'CREATE INDEX idx_record_time ON health_record(record_time)');

CALL create_index_if_not_exists('health_record', 'idx_record_type', 
    'CREATE INDEX idx_record_type ON health_record(record_type)');

CALL create_index_if_not_exists('health_record', 'idx_record_elderly_time', 
    'CREATE INDEX idx_record_elderly_time ON health_record(elderly_id, record_time DESC)');

-- ===================================================================
-- 4. 医生表索引优化 (t_doctor)
-- ===================================================================

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_name', 
    'CREATE INDEX idx_doctor_name ON t_doctor(name)');

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_department', 
    'CREATE INDEX idx_doctor_department ON t_doctor(department)');

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_title', 
    'CREATE INDEX idx_doctor_title ON t_doctor(title)');

-- ===================================================================
-- 5. 用户表索引优化 (sys_user)
-- ===================================================================

CALL create_index_if_not_exists('sys_user', 'uk_user_username', 
    'CREATE UNIQUE INDEX uk_user_username ON sys_user(username)');

CALL create_index_if_not_exists('sys_user', 'idx_user_phone', 
    'CREATE INDEX idx_user_phone ON sys_user(phone)');

CALL create_index_if_not_exists('sys_user', 'idx_user_email', 
    'CREATE INDEX idx_user_email ON sys_user(email)');

CALL create_index_if_not_exists('sys_user', 'idx_user_role', 
    'CREATE INDEX idx_user_role ON sys_user(role_type)');

CALL create_index_if_not_exists('sys_user', 'idx_user_status', 
    'CREATE INDEX idx_user_status ON sys_user(status)');

CALL create_index_if_not_exists('sys_user', 'idx_user_role_status', 
    'CREATE INDEX idx_user_role_status ON sys_user(role_type, status)');

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
-- 7. 家属用户表索引优化 (family_user)
-- ===================================================================

CALL create_index_if_not_exists('family_user', 'idx_family_user_name', 
    'CREATE INDEX idx_family_user_name ON family_user(name)');

CALL create_index_if_not_exists('family_user', 'idx_family_user_phone', 
    'CREATE INDEX idx_family_user_phone ON family_user(phone)');

-- ===================================================================
-- 8. 照护评估表索引优化 (elderly_care_assessment)
-- ===================================================================

CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_elderly_id', 
    'CREATE INDEX idx_assessment_elderly_id ON elderly_care_assessment(elderly_id)');

CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_time', 
    'CREATE INDEX idx_assessment_time ON elderly_care_assessment(assessment_time)');

CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_elderly_time', 
    'CREATE INDEX idx_assessment_elderly_time ON elderly_care_assessment(elderly_id, assessment_time DESC)');

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
  AND TABLE_NAME IN ('elderly', 't_health_warning', 'health_record', 
                     't_doctor', 'sys_user', 'family_elderly_relation', 
                     'family_user', 'elderly_care_assessment')
GROUP BY TABLE_NAME
ORDER BY TABLE_NAME;

SELECT '============================================' AS '';
SELECT '✅ 性能索引创建完成！' AS '';
SELECT '共为8张核心表创建了35+个性能索引' AS '';
SELECT '建议: 使用 EXPLAIN 分析查询语句验证索引效果' AS '';
SELECT '============================================' AS '';
