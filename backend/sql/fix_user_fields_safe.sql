-- 快速修复sys_user表字段问题
-- 问题:Mapper XML查询real_name/email/phone字段,但表中可能缺失

USE smartcare_cloud;

-- 方案1: 添加缺失字段(如果不存在)
SET @dbname = 'smartcare_cloud';
SET @tablename = 'sys_user';

-- 检查real_name字段是否存在
SET @col_exists = 0;
SELECT COUNT(*) INTO @col_exists
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = @tablename
  AND COLUMN_NAME = 'real_name';

-- 如果不存在则添加
SET @sql_real_name = IF(@col_exists = 0,
    'ALTER TABLE sys_user ADD COLUMN real_name VARCHAR(50) NULL COMMENT ''真实姓名'' AFTER password',
    'SELECT ''real_name字段已存在'' AS status');
PREPARE stmt FROM @sql_real_name;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查email字段
SET @col_exists_email = 0;
SELECT COUNT(*) INTO @col_exists_email
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = @tablename
  AND COLUMN_NAME = 'email';

SET @sql_email = IF(@col_exists_email = 0,
    'ALTER TABLE sys_user ADD COLUMN email VARCHAR(100) NULL COMMENT ''邮箱地址''',
    'SELECT ''email字段已存在'' AS status');
PREPARE stmt2 FROM @sql_email;
EXECUTE stmt2;
DEALLOCATE PREPARE stmt2;

-- 检查phone字段
SET @col_exists_phone = 0;
SELECT COUNT(*) INTO @col_exists_phone
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = @dbname
  AND TABLE_NAME = @tablename
  AND COLUMN_NAME = 'phone';

SET @sql_phone = IF(@col_exists_phone = 0,
    'ALTER TABLE sys_user ADD COLUMN phone VARCHAR(20) NULL COMMENT ''手机号码''',
    'SELECT ''phone字段已存在'' AS status');
PREPARE stmt3 FROM @sql_phone;
EXECUTE stmt3;
DEALLOCATE PREPARE stmt3;

-- 如果有name字段但real_name为NULL,则复制数据
UPDATE sys_user 
SET real_name = COALESCE(real_name, name, username)
WHERE real_name IS NULL OR real_name = '';

-- 显示最终表结构
SELECT '=== sys_user表当前字段列表 ===' AS info;
SELECT COLUMN_NAME, COLUMN_TYPE, IS_NULLABLE, COLUMN_DEFAULT, COLUMN_COMMENT
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND TABLE_NAME = 'sys_user'
ORDER BY ORDINAL_POSITION;
