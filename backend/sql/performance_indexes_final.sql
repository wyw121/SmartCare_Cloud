-- ===================================================================
-- 智慧医养平台 - 性能优化索引脚本 (最终正确版)
-- 
-- 用途: 基于实际表结构创建性能索引
-- 执行前: 已检查所有表的字段结构
-- 兼容性: MySQL 5.7+
-- ===================================================================

USE smartcare_cloud;

-- ===================================================================
-- 创建辅助存储过程（安全创建索引）
-- ===================================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS create_index_if_not_exists$$

CREATE PROCEDURE create_index_if_not_exists(
    IN p_table_name VARCHAR(64),
    IN p_index_name VARCHAR(64),
    IN p_index_definition TEXT
)
BEGIN
    DECLARE index_exists INT DEFAULT 0;
    
    SELECT COUNT(*) INTO index_exists
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name
      AND INDEX_NAME = p_index_name;
    
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
-- 1. elderly - 老人档案表
-- ===================================================================

CALL create_index_if_not_exists('elderly', 'idx_elderly_name', 
    'CREATE INDEX idx_elderly_name ON elderly(name)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_health_status', 
    'CREATE INDEX idx_elderly_health_status ON elderly(health_status)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_phone', 
    'CREATE INDEX idx_elderly_phone ON elderly(phone)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_create_time', 
    'CREATE INDEX idx_elderly_create_time ON elderly(create_time)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_care_level', 
    'CREATE INDEX idx_elderly_care_level ON elderly(care_level)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_status', 
    'CREATE INDEX idx_elderly_status ON elderly(status)');

-- 复合索引
CALL create_index_if_not_exists('elderly', 'idx_elderly_health_create', 
    'CREATE INDEX idx_elderly_health_create ON elderly(health_status, create_time DESC)');

CALL create_index_if_not_exists('elderly', 'idx_elderly_status_care', 
    'CREATE INDEX idx_elderly_status_care ON elderly(status, care_level)');

-- ===================================================================
-- 2. t_health_warning - 健康预警表
-- ===================================================================

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_elderly_id', 
    'CREATE INDEX idx_warning_elderly_id ON t_health_warning(elderly_id)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_level', 
    'CREATE INDEX idx_warning_level ON t_health_warning(warning_level)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_status', 
    'CREATE INDEX idx_warning_status ON t_health_warning(status)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_trigger_time', 
    'CREATE INDEX idx_warning_trigger_time ON t_health_warning(trigger_time)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_type', 
    'CREATE INDEX idx_warning_type ON t_health_warning(warning_type)');

-- 复合索引
CALL create_index_if_not_exists('t_health_warning', 'idx_warning_elderly_time', 
    'CREATE INDEX idx_warning_elderly_time ON t_health_warning(elderly_id, trigger_time DESC)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_status_level', 
    'CREATE INDEX idx_warning_status_level ON t_health_warning(status, warning_level)');

CALL create_index_if_not_exists('t_health_warning', 'idx_warning_level_time', 
    'CREATE INDEX idx_warning_level_time ON t_health_warning(warning_level, trigger_time DESC)');

-- ===================================================================
-- 3. health_record - 健康记录表
-- ===================================================================

CALL create_index_if_not_exists('health_record', 'idx_record_elderly_id', 
    'CREATE INDEX idx_record_elderly_id ON health_record(elderly_id)');

CALL create_index_if_not_exists('health_record', 'idx_record_date', 
    'CREATE INDEX idx_record_date ON health_record(record_date)');

CALL create_index_if_not_exists('health_record', 'idx_record_type', 
    'CREATE INDEX idx_record_type ON health_record(record_type)');

CALL create_index_if_not_exists('health_record', 'idx_record_doctor_id', 
    'CREATE INDEX idx_record_doctor_id ON health_record(doctor_id)');

CALL create_index_if_not_exists('health_record', 'idx_record_risk_level', 
    'CREATE INDEX idx_record_risk_level ON health_record(risk_level)');

CALL create_index_if_not_exists('health_record', 'idx_record_is_abnormal', 
    'CREATE INDEX idx_record_is_abnormal ON health_record(is_abnormal)');

-- 复合索引
CALL create_index_if_not_exists('health_record', 'idx_record_elderly_date', 
    'CREATE INDEX idx_record_elderly_date ON health_record(elderly_id, record_date DESC)');

CALL create_index_if_not_exists('health_record', 'idx_record_type_date', 
    'CREATE INDEX idx_record_type_date ON health_record(record_type, record_date DESC)');

-- ===================================================================
-- 4. t_doctor - 医生表
-- ===================================================================

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_name', 
    'CREATE INDEX idx_doctor_name ON t_doctor(name)');

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_department', 
    'CREATE INDEX idx_doctor_department ON t_doctor(department)');

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_title', 
    'CREATE INDEX idx_doctor_title ON t_doctor(title)');

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_phone', 
    'CREATE INDEX idx_doctor_phone ON t_doctor(phone)');

CALL create_index_if_not_exists('t_doctor', 'idx_doctor_status', 
    'CREATE INDEX idx_doctor_status ON t_doctor(status)');

-- 复合索引
CALL create_index_if_not_exists('t_doctor', 'idx_doctor_dept_title', 
    'CREATE INDEX idx_doctor_dept_title ON t_doctor(department, title)');

-- ===================================================================
-- 5. sys_user - 系统用户表
-- ===================================================================

CALL create_index_if_not_exists('sys_user', 'idx_user_role_code', 
    'CREATE INDEX idx_user_role_code ON sys_user(role_code)');

CALL create_index_if_not_exists('sys_user', 'idx_user_status', 
    'CREATE INDEX idx_user_status ON sys_user(status)');

CALL create_index_if_not_exists('sys_user', 'idx_user_real_name', 
    'CREATE INDEX idx_user_real_name ON sys_user(real_name)');

-- 复合索引
CALL create_index_if_not_exists('sys_user', 'idx_user_role_status', 
    'CREATE INDEX idx_user_role_status ON sys_user(role_code, status)');

-- ===================================================================
-- 6. family_elderly_relation - 家属老人关系表
-- ===================================================================

CALL create_index_if_not_exists('family_elderly_relation', 'idx_relation_family_id', 
    'CREATE INDEX idx_relation_family_id ON family_elderly_relation(family_user_id)');

CALL create_index_if_not_exists('family_elderly_relation', 'idx_relation_elderly_id', 
    'CREATE INDEX idx_relation_elderly_id ON family_elderly_relation(elderly_id)');

CALL create_index_if_not_exists('family_elderly_relation', 'idx_relation_type', 
    'CREATE INDEX idx_relation_type ON family_elderly_relation(relationship_type)');

CALL create_index_if_not_exists('family_elderly_relation', 'idx_relation_is_primary', 
    'CREATE INDEX idx_relation_is_primary ON family_elderly_relation(is_primary)');

CALL create_index_if_not_exists('family_elderly_relation', 'idx_relation_status', 
    'CREATE INDEX idx_relation_status ON family_elderly_relation(status)');

-- 复合索引
CALL create_index_if_not_exists('family_elderly_relation', 'uk_family_elderly', 
    'CREATE UNIQUE INDEX uk_family_elderly ON family_elderly_relation(family_user_id, elderly_id)');

CALL create_index_if_not_exists('family_elderly_relation', 'idx_relation_elderly_priority', 
    'CREATE INDEX idx_relation_elderly_priority ON family_elderly_relation(elderly_id, contact_priority)');

-- ===================================================================
-- 7. family_user - 家属用户表
-- ===================================================================

CALL create_index_if_not_exists('family_user', 'idx_family_real_name', 
    'CREATE INDEX idx_family_real_name ON family_user(real_name)');

CALL create_index_if_not_exists('family_user', 'idx_family_phone', 
    'CREATE INDEX idx_family_phone ON family_user(phone)');

CALL create_index_if_not_exists('family_user', 'idx_family_status', 
    'CREATE INDEX idx_family_status ON family_user(status)');

CALL create_index_if_not_exists('family_user', 'idx_family_create_time', 
    'CREATE INDEX idx_family_create_time ON family_user(create_time)');

-- ===================================================================
-- 8. elderly_care_assessment - 照护评估表
-- ===================================================================

CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_elderly_id', 
    'CREATE INDEX idx_assessment_elderly_id ON elderly_care_assessment(elderly_id)');

CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_date', 
    'CREATE INDEX idx_assessment_date ON elderly_care_assessment(assessment_date)');

CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_type', 
    'CREATE INDEX idx_assessment_type ON elderly_care_assessment(assessment_type)');

CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_risk_level', 
    'CREATE INDEX idx_assessment_risk_level ON elderly_care_assessment(risk_level)');

-- 复合索引
CALL create_index_if_not_exists('elderly_care_assessment', 'idx_assessment_elderly_date', 
    'CREATE INDEX idx_assessment_elderly_date ON elderly_care_assessment(elderly_id, assessment_date DESC)');

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

SELECT 
    TABLE_NAME AS '表名',
    COUNT(DISTINCT INDEX_NAME) AS '索引数量',
    GROUP_CONCAT(DISTINCT INDEX_NAME ORDER BY INDEX_NAME SEPARATOR ', ') AS '索引列表'
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_NAME IN ('elderly', 't_health_warning', 'health_record', 
                     't_doctor', 'sys_user', 'family_elderly_relation', 
                     'family_user', 'elderly_care_assessment')
  AND INDEX_NAME != 'PRIMARY'
GROUP BY TABLE_NAME
ORDER BY TABLE_NAME;

SELECT '============================================' AS '';
SELECT '✅ 性能索引创建完成！' AS '';
SELECT CONCAT('共为8张核心表创建了 ', 
    (SELECT COUNT(DISTINCT INDEX_NAME) 
     FROM information_schema.STATISTICS 
     WHERE TABLE_SCHEMA = 'smartcare_cloud' 
       AND TABLE_NAME IN ('elderly', 't_health_warning', 'health_record', 
                          't_doctor', 'sys_user', 'family_elderly_relation', 
                          'family_user', 'elderly_care_assessment')
       AND INDEX_NAME != 'PRIMARY'), 
    ' 个索引') AS '';
SELECT '建议: 使用 EXPLAIN 分析查询语句验证索引效果' AS '';
SELECT '============================================' AS '';
