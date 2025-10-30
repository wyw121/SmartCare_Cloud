-- 修复sys_user表字段不匹配问题
USE smartcare_cloud;

-- 检查并添加缺失的字段
ALTER TABLE sys_user 
  ADD COLUMN IF NOT EXISTS email VARCHAR(100) NULL COMMENT '邮箱地址' AFTER phone,
  ADD COLUMN IF NOT EXISTS real_name VARCHAR(50) NULL COMMENT '真实姓名' AFTER password;

-- 如果存在name字段但不存在real_name,则复制数据
UPDATE sys_user SET real_name = name WHERE real_name IS NULL OR real_name = '';

-- 确认字段存在
SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_SCHEMA = 'smartcare_cloud' 
  AND TABLE_NAME = 'sys_user'
  AND COLUMN_NAME IN ('real_name', 'email', 'phone')
ORDER BY ORDINAL_POSITION;
