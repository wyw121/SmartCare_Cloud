-- 简单的数据库修复脚本 - 只修复必要的字段匹配问题
USE smartcare_cloud;
-- 修改existing表添加缺少的字段
ALTER TABLE elderly
ADD COLUMN IF NOT EXISTS gender_code TINYINT(1) COMMENT '性别代码(1-男,0-女)'
AFTER gender,
  ADD COLUMN IF NOT EXISTS age INT(3) COMMENT '年龄',
  ADD COLUMN IF NOT EXISTS emergency_contact_name VARCHAR(50) COMMENT '紧急联系人姓名',
  ADD COLUMN IF NOT EXISTS emergency_contact_phone VARCHAR(20) COMMENT '紧急联系人电话',
  ADD COLUMN IF NOT EXISTS care_level TINYINT(1) DEFAULT 1 COMMENT '照护等级',
  ADD COLUMN IF NOT EXISTS insurance_type VARCHAR(50) COMMENT '医保类型',
  ADD COLUMN IF NOT EXISTS medical_history TEXT COMMENT '既往病史',
  ADD COLUMN IF NOT EXISTS allergy_history TEXT COMMENT '过敏史',
  ADD COLUMN IF NOT EXISTS family_doctor_id BIGINT(20) COMMENT '家庭医生ID',
  ADD COLUMN IF NOT EXISTS status TINYINT(1) DEFAULT 1 COMMENT '状态',
  ADD COLUMN IF NOT EXISTS remarks TEXT COMMENT '备注',
  ADD COLUMN IF NOT EXISTS create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  ADD COLUMN IF NOT EXISTS update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  ADD COLUMN IF NOT EXISTS create_by VARCHAR(50) COMMENT '创建者',
  ADD COLUMN IF NOT EXISTS update_by VARCHAR(50) COMMENT '更新者';
-- 更新现有数据，设置gender_code
UPDATE elderly
SET gender_code = CASE
    WHEN gender = 'MALE' THEN 1
    WHEN gender = 'FEMALE' THEN 0
    ELSE 1
  END;
-- 更新health_status字段值
UPDATE elderly
SET health_status = CASE
    WHEN health_status = 'NORMAL' THEN 'HEALTHY'
    ELSE health_status
  END;
-- 插入一些测试数据（如果不存在）
INSERT IGNORE INTO elderly (
    name,
    gender,
    gender_code,
    id_card,
    phone,
    address,
    health_status,
    age,
    care_level,
    create_by
  )
VALUES (
    'Test Elder 1',
    'MALE',
    1,
    '110101195001011111',
    '13800138001',
    'Test Address 1',
    'HEALTHY',
    70,
    1,
    'system'
  ),
  (
    'Test Elder 2',
    'FEMALE',
    0,
    '110101195001012222',
    '13800138002',
    'Test Address 2',
    'WARNING',
    75,
    2,
    'system'
  ),
  (
    'Test Elder 3',
    'MALE',
    1,
    '110101195001013333',
    '13800138003',
    'Test Address 3',
    'DANGER',
    80,
    3,
    'system'
  );
SELECT 'Database fix completed successfully!' as message;