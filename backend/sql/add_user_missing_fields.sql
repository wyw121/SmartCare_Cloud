-- 添加sys_user表缺失字段
USE smartcare_cloud;

-- 添加gender字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'sys_user' 
     AND table_schema = 'smartcare_cloud' 
     AND column_name = 'gender') > 0,
    "SELECT 'gender字段已存在'",
    "ALTER TABLE sys_user ADD COLUMN gender TINYINT(1) NULL COMMENT '性别:0女1男' AFTER phone"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加role_name字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'sys_user' 
     AND table_schema = 'smartcare_cloud' 
     AND column_name = 'role_name') > 0,
    "SELECT 'role_name字段已存在'",
    "ALTER TABLE sys_user ADD COLUMN role_name VARCHAR(50) NULL COMMENT '角色名称' AFTER role_code"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加doctor_title字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'sys_user' 
     AND table_schema = 'smartcare_cloud' 
     AND column_name = 'doctor_title') > 0,
    "SELECT 'doctor_title字段已存在'",
    "ALTER TABLE sys_user ADD COLUMN doctor_title VARCHAR(50) NULL COMMENT '医生职称'"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加doctor_speciality字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'sys_user' 
     AND table_schema = 'smartcare_cloud' 
     AND column_name = 'doctor_speciality') > 0,
    "SELECT 'doctor_speciality字段已存在'",
    "ALTER TABLE sys_user ADD COLUMN doctor_speciality VARCHAR(200) NULL COMMENT '医生专长'"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加family_relationship字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'sys_user' 
     AND table_schema = 'smartcare_cloud' 
     AND column_name = 'family_relationship') > 0,
    "SELECT 'family_relationship字段已存在'",
    "ALTER TABLE sys_user ADD COLUMN family_relationship VARCHAR(50) NULL COMMENT '家属关系'"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加is_deleted字段
SET @s = (SELECT IF(
    (SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS 
     WHERE table_name = 'sys_user' 
     AND table_schema = 'smartcare_cloud' 
     AND column_name = 'is_deleted') > 0,
    "SELECT 'is_deleted字段已存在'",
    "ALTER TABLE sys_user ADD COLUMN is_deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '删除标记'"
));
PREPARE stmt FROM @s;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 同步role_name数据
UPDATE sys_user SET role_name = CASE role_code
    WHEN 'admin' THEN '系统管理员'
    WHEN 'system_admin' THEN '系统管理员'
    WHEN 'business_admin' THEN '业务管理员'
    WHEN 'doctor' THEN '医生'
    WHEN 'nurse' THEN '护士'
    WHEN 'social_worker' THEN '社工'
    WHEN 'family' THEN '家属'
    ELSE role_code
END
WHERE role_name IS NULL OR role_name = '';

-- 显示最终表结构
SELECT '=== sys_user表字段修复完成 ===' AS info;
DESCRIBE sys_user;
