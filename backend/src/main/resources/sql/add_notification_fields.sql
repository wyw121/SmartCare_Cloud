-- 更新健康预警表结构，添加缺失的字段
USE smartcare_cloud;

-- 检查并添加 is_notified_family 字段
SET @sql = (
    SELECT IF(
        EXISTS(
            SELECT 1 FROM information_schema.columns 
            WHERE table_schema = 'smartcare_cloud' 
            AND table_name = 't_health_warning' 
            AND column_name = 'is_notified_family'
        ),
        'SELECT "字段 is_notified_family 已存在" AS message',
        'ALTER TABLE t_health_warning ADD COLUMN is_notified_family TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否已通知家属 (0-否, 1-是)" AFTER handle_remark'
    )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加 is_notified_doctor 字段
SET @sql = (
    SELECT IF(
        EXISTS(
            SELECT 1 FROM information_schema.columns 
            WHERE table_schema = 'smartcare_cloud' 
            AND table_name = 't_health_warning' 
            AND column_name = 'is_notified_doctor'
        ),
        'SELECT "字段 is_notified_doctor 已存在" AS message',
        'ALTER TABLE t_health_warning ADD COLUMN is_notified_doctor TINYINT(1) NOT NULL DEFAULT 0 COMMENT "是否已通知医生 (0-否, 1-是)" AFTER is_notified_family'
    )
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 验证表结构
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM information_schema.columns 
WHERE table_schema = 'smartcare_cloud' 
AND table_name = 't_health_warning' 
AND column_name IN ('is_notified_family', 'is_notified_doctor')
ORDER BY ORDINAL_POSITION;

-- 如果需要，清空表数据重新插入
-- TRUNCATE TABLE t_health_warning;
