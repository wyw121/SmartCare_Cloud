-- 修复数据库表结构
USE smartcare_cloud;
-- 检查并添加缺失的字段
-- 如果字段不存在就添加
SET @sql = (
    SELECT IF(
        (
          SELECT COUNT(*)
          FROM INFORMATION_SCHEMA.COLUMNS
          WHERE TABLE_SCHEMA = 'smartcare_cloud'
            AND TABLE_NAME = 'elderly'
            AND COLUMN_NAME = 'emergency_contact'
        ) = 0,
        'ALTER TABLE elderly ADD COLUMN emergency_contact VARCHAR(100) COMMENT ''紧急联系人'';',
        'SELECT ''emergency_contact字段已存在'' AS message;'
      )
  );
PREPARE stmt
FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
SET @sql = (
    SELECT IF(
        (
          SELECT COUNT(*)
          FROM INFORMATION_SCHEMA.COLUMNS
          WHERE TABLE_SCHEMA = 'smartcare_cloud'
            AND TABLE_NAME = 'elderly'
            AND COLUMN_NAME = 'emergency_phone'
        ) = 0,
        'ALTER TABLE elderly ADD COLUMN emergency_phone VARCHAR(20) COMMENT ''紧急联系人电话'';',
        'SELECT ''emergency_phone字段已存在'' AS message;'
      )
  );
PREPARE stmt
FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
-- 显示当前表结构
DESCRIBE elderly;