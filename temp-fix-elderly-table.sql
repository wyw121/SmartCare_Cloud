-- 临时修复elderly表结构
USE smartcare_cloud;
-- 检查并添加缺失的字段
ALTER TABLE elderly
ADD COLUMN IF NOT EXISTS care_level TINYINT(1) DEFAULT 1 COMMENT '照护等级:1-自理,2-半自理,3-不能自理',
  ADD COLUMN IF NOT EXISTS room_number VARCHAR(20) COMMENT '房间号',
  ADD COLUMN IF NOT EXISTS guardian_name VARCHAR(50) COMMENT '监护人姓名',
  ADD COLUMN IF NOT EXISTS guardian_phone VARCHAR(20) COMMENT '监护人电话',
  ADD COLUMN IF NOT EXISTS status TINYINT(1) DEFAULT 1 COMMENT '档案状态:1-正常,0-停用';
-- 确保字段名称正确
SELECT 'elderly表结构检查完成' as message;
-- 插入一些测试数据（如果表为空的话）
INSERT IGNORE INTO elderly (
    name,
    id_card,
    phone,
    gender,
    birth_date,
    health_status,
    care_level,
    room_number,
    guardian_name
  )
VALUES (
    '张三',
    '110101199001011234',
    '13800138001',
    'MALE',
    '1990-01-01',
    'HEALTHY',
    1,
    'A101',
    '张四'
  ),
  (
    '李四',
    '110101198502021234',
    '13800138002',
    'FEMALE',
    '1985-02-02',
    'NORMAL',
    1,
    'A102',
    '李五'
  ),
  (
    '王五',
    '110101198003031234',
    '13800138003',
    'MALE',
    '1980-03-03',
    'SICK',
    2,
    'B201',
    '王六'
  );