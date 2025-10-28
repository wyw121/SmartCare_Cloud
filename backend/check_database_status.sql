-- ============================================
-- 数据库状态检查脚本
-- 功能: 检查外键约束和索引是否存在
-- ============================================

USE smartcare_cloud;

-- ============================================
-- 1. 检查所有表的外键约束
-- ============================================

SELECT 
    '外键约束检查' AS category,
    TABLE_NAME AS table_name,
    CONSTRAINT_NAME AS constraint_name,
    COLUMN_NAME AS column_name,
    REFERENCED_TABLE_NAME AS referenced_table,
    REFERENCED_COLUMN_NAME AS referenced_column
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND REFERENCED_TABLE_NAME IS NOT NULL
ORDER BY TABLE_NAME, CONSTRAINT_NAME;


-- ============================================
-- 2. 检查所有表的索引
-- ============================================

SELECT 
    '索引检查' AS category,
    TABLE_NAME AS table_name,
    INDEX_NAME AS index_name,
    GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX) AS columns,
    NON_UNIQUE AS is_non_unique,
    INDEX_TYPE AS index_type,
    INDEX_COMMENT AS comment
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
GROUP BY TABLE_NAME, INDEX_NAME, NON_UNIQUE, INDEX_TYPE, INDEX_COMMENT
ORDER BY TABLE_NAME, INDEX_NAME;


-- ============================================
-- 3. 检查缺失的索引 (常用查询字段)
-- ============================================

-- 检查health_warning表是否有elderly_id索引
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 存在'
        ELSE '✗ 缺失 - 需要添加'
    END AS status,
    't_health_warning.elderly_id' AS field
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_NAME = 't_health_warning'
  AND COLUMN_NAME = 'elderly_id';

-- 检查health_warning表是否有status索引
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 存在'
        ELSE '✗ 缺失 - 需要添加'
    END AS status,
    't_health_warning.status' AS field
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_NAME = 't_health_warning'
  AND COLUMN_NAME = 'status';

-- 检查elderly表是否有health_status索引
SELECT 
    CASE 
        WHEN COUNT(*) > 0 THEN '✓ 存在'
        ELSE '✗ 缺失 - 需要添加'
    END AS status,
    'elderly.health_status' AS field
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_NAME = 'elderly'
  AND COLUMN_NAME = 'health_status';


-- ============================================
-- 4. 表大小和行数统计
-- ============================================

SELECT 
    TABLE_NAME AS '表名',
    TABLE_ROWS AS '行数',
    ROUND(DATA_LENGTH/1024/1024, 2) AS '数据大小(MB)',
    ROUND(INDEX_LENGTH/1024/1024, 2) AS '索引大小(MB)',
    ROUND((DATA_LENGTH + INDEX_LENGTH)/1024/1024, 2) AS '总大小(MB)'
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_TYPE = 'BASE TABLE'
ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;


-- ============================================
-- 5. 检查表引擎
-- ============================================

SELECT 
    TABLE_NAME AS table_name,
    ENGINE AS engine,
    TABLE_COLLATION AS collation,
    CREATE_TIME AS create_time
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;
