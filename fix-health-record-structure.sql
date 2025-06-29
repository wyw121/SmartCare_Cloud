-- 修复 health_record 表结构
-- 添加缺失的字段

USE smartcare_cloud;

-- 检查并添加收缩压字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'smartcare_cloud' 
    AND TABLE_NAME = 'health_record' 
    AND COLUMN_NAME = 'systolic_pressure'
);

SET @sql = IF(@column_exists = 0,
    'ALTER TABLE health_record ADD COLUMN systolic_pressure INT COMMENT "收缩压(mmHg)"',
    'SELECT "systolic_pressure字段已存在" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加舒张压字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'smartcare_cloud' 
    AND TABLE_NAME = 'health_record' 
    AND COLUMN_NAME = 'diastolic_pressure'
);

SET @sql = IF(@column_exists = 0,
    'ALTER TABLE health_record ADD COLUMN diastolic_pressure INT COMMENT "舒张压(mmHg)"',
    'SELECT "diastolic_pressure字段已存在" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加心率字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'smartcare_cloud' 
    AND TABLE_NAME = 'health_record' 
    AND COLUMN_NAME = 'heart_rate'
);

SET @sql = IF(@column_exists = 0,
    'ALTER TABLE health_record ADD COLUMN heart_rate INT COMMENT "心率(次/分钟)"',
    'SELECT "heart_rate字段已存在" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加体温字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'smartcare_cloud' 
    AND TABLE_NAME = 'health_record' 
    AND COLUMN_NAME = 'body_temperature'
);

SET @sql = IF(@column_exists = 0,
    'ALTER TABLE health_record ADD COLUMN body_temperature DECIMAL(4,2) COMMENT "体温(°C)"',
    'SELECT "body_temperature字段已存在" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 检查并添加血糖字段
SET @column_exists = (
    SELECT COUNT(*) 
    FROM INFORMATION_SCHEMA.COLUMNS 
    WHERE TABLE_SCHEMA = 'smartcare_cloud' 
    AND TABLE_NAME = 'health_record' 
    AND COLUMN_NAME = 'blood_glucose'
);

SET @sql = IF(@column_exists = 0,
    'ALTER TABLE health_record ADD COLUMN blood_glucose DECIMAL(5,2) COMMENT "血糖(mmol/L)"',
    'SELECT "blood_glucose字段已存在" as message'
);
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加一些测试数据
INSERT INTO health_record (elderly_id, doctor_id, record_type, record_date, systolic_pressure, diastolic_pressure, heart_rate, body_temperature, blood_glucose, weight, height, symptoms, diagnosis, treatment, medication, doctor_advice, remarks, is_abnormal, urgency_level) VALUES
(1, 1, 4, NOW(), 130, 85, 75, 36.5, 5.8, 65.5, 170.0, '无明显症状', '血压稍高', '建议低盐饮食', '无', '控制饮食，适量运动', '定期监测', 0, 1),
(2, 1, 4, NOW(), 145, 95, 80, 36.8, 6.2, 58.0, 165.0, '偶有头晕', '高血压初期', '饮食控制+运动', '无', '低盐低脂饮食，每日散步30分钟', '密切观察', 1, 2),
(3, 1, 4, NOW(), 120, 80, 72, 36.3, 5.5, 62.0, 168.0, '无', '正常', '继续保持', '无', '保持良好生活习惯', '正常监测', 0, 1),
(4, 1, 4, NOW(), 160, 100, 88, 37.0, 7.1, 70.0, 172.0, '头痛，胸闷', '高血压+糖尿病前期', '药物治疗+饮食控制', '降压药', '严格控制饮食，按时服药', '需要密切监测', 1, 3),
(5, 1, 4, NOW(), 135, 88, 78, 36.6, 6.0, 55.5, 162.0, '轻微乏力', '血压偏高', '生活方式干预', '无', '增加运动量，减少盐分摄入', '定期复查', 1, 2);

-- 查看表结构
DESCRIBE health_record;

SELECT '健康记录表结构修复完成！' as message;
