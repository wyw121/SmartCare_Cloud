-- 照护统计测试数据初始化脚本
-- 更新老人表的照护等级数据
USE smartcare_cloud;
-- 更新现有老人数据的照护等级
UPDATE elderly
SET care_level = 1
WHERE id IN (1, 8, 9, 10, 15, 16, 17, 18, 19, 20);
-- 自理
UPDATE elderly
SET care_level = 2
WHERE id IN (2, 11, 12, 13, 14);
-- 半自理  
UPDATE elderly
SET care_level = 3
WHERE id IN (3, 4, 5, 6, 7);
-- 不能自理
-- 确保老人表有照护等级字段
ALTER TABLE elderly
ADD COLUMN IF NOT EXISTS care_level TINYINT(1) DEFAULT 1 COMMENT '照护等级:1-自理,2-半自理,3-不能自理';
-- 创建老人服务记录表（如果不存在）
CREATE TABLE IF NOT EXISTS elderly_service_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '服务记录ID',
  elderly_id BIGINT NOT NULL COMMENT '老人ID',
  service_date DATE NOT NULL COMMENT '服务日期',
  service_type VARCHAR(50) NOT NULL COMMENT '服务类型',
  service_provider_id BIGINT COMMENT '服务提供者ID',
  service_provider_name VARCHAR(50) COMMENT '服务提供者姓名',
  service_content TEXT COMMENT '服务内容',
  service_duration INT COMMENT '服务时长(分钟)',
  service_quality_score TINYINT(1) COMMENT '服务质量评分(1-5)',
  elderly_satisfaction TINYINT(1) COMMENT '老人满意度(1-5)',
  service_cost DECIMAL(10, 2) COMMENT '服务费用',
  service_status VARCHAR(20) DEFAULT 'COMPLETED' COMMENT '服务状态',
  feedback TEXT COMMENT '服务反馈',
  remark TEXT COMMENT '备注',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  INDEX idx_elderly_id (elderly_id),
  INDEX idx_service_date (service_date),
  INDEX idx_service_type (service_type),
  INDEX idx_service_status (service_status)
) COMMENT = '老人服务记录表';
-- 插入服务记录测试数据
INSERT INTO elderly_service_record (
    elderly_id,
    service_date,
    service_type,
    service_provider_name,
    service_content,
    service_duration,
    service_quality_score,
    elderly_satisfaction,
    service_cost,
    service_status,
    feedback
  )
VALUES -- 自理老人服务记录
  (
    1,
    '2024-12-01',
    '生活照料',
    '李护士',
    '协助洗澡，整理房间',
    45,
    5,
    5,
    80.00,
    'COMPLETED',
    '服务态度很好，老人很满意'
  ),
  (
    8,
    '2024-12-01',
    '医疗护理',
    '王护士',
    '测量血压，健康检查',
    30,
    4,
    4,
    50.00,
    'COMPLETED',
    '专业水平高，老人很信任'
  ),
  (
    9,
    '2024-12-01',
    '康复训练',
    '张护士',
    '关节活动训练，肌肉按摩',
    60,
    5,
    5,
    120.00,
    'COMPLETED',
    '康复效果明显，老人很配合'
  ),
  (
    10,
    '2024-12-01',
    '心理疏导',
    '赵护士',
    '聊天陪伴，情绪疏导',
    40,
    4,
    4,
    60.00,
    'COMPLETED',
    '帮助老人缓解孤独感'
  ),
  -- 半自理老人服务记录
  (
    2,
    '2024-12-01',
    '生活照料',
    '陈护士',
    '协助穿衣，饮食照顾',
    90,
    4,
    4,
    150.00,
    'COMPLETED',
    '照顾周到，老人家属满意'
  ),
  (
    11,
    '2024-12-01',
    '医疗护理',
    '李护士',
    '用药提醒，血糖监测',
    25,
    5,
    5,
    40.00,
    'COMPLETED',
    '用药管理到位，血糖控制良好'
  ),
  (
    12,
    '2024-12-01',
    '营养配餐',
    '王护士',
    '制定营养餐单，饮食指导',
    35,
    4,
    4,
    70.00,
    'COMPLETED',
    '营养搭配合理，老人胃口改善'
  ),
  (
    13,
    '2024-12-01',
    '生活照料',
    '张护士',
    '洗澡协助，房间清洁',
    80,
    4,
    4,
    130.00,
    'COMPLETED',
    '服务细致，注意安全'
  ),
  -- 不能自理老人服务记录
  (
    3,
    '2024-12-01',
    '生活照料',
    '赵护士',
    '全程生活照顾，翻身护理',
    180,
    5,
    4,
    300.00,
    'COMPLETED',
    '24小时精心照顾，预防褥疮'
  ),
  (
    4,
    '2024-12-01',
    '医疗护理',
    '陈护士',
    '输液护理，病情监测',
    120,
    5,
    5,
    200.00,
    'COMPLETED',
    '医护水平专业，家属放心'
  ),
  (
    5,
    '2024-12-01',
    '康复训练',
    '李护士',
    '被动关节活动，肌肉按摩',
    90,
    4,
    4,
    180.00,
    'COMPLETED',
    '有助于防止肌肉萎缩'
  ),
  (
    6,
    '2024-12-01',
    '心理疏导',
    '王护士',
    '情感支持，家属沟通',
    50,
    4,
    3,
    80.00,
    'COMPLETED',
    '老人情绪有所改善'
  ),
  (
    7,
    '2024-12-01',
    '营养配餐',
    '张护士',
    '流质饮食准备，喂食护理',
    60,
    5,
    4,
    100.00,
    'COMPLETED',
    '营养摄入充足，吞咽安全'
  );
-- 创建护理人员表（如果不存在）
CREATE TABLE IF NOT EXISTS caregiver (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '护理人员ID',
  name VARCHAR(50) NOT NULL COMMENT '姓名',
  gender TINYINT(1) COMMENT '性别:1-男,0-女',
  age INT COMMENT '年龄',
  phone VARCHAR(20) COMMENT '联系电话',
  qualification VARCHAR(100) COMMENT '资质证书',
  experience_years INT COMMENT '工作经验(年)',
  specialty VARCHAR(200) COMMENT '专业特长',
  employment_date DATE COMMENT '入职日期',
  status TINYINT(1) DEFAULT 1 COMMENT '状态:1-在职,0-离职',
  created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT = '护理人员表';
-- 插入护理人员数据
INSERT INTO caregiver (
    name,
    gender,
    age,
    phone,
    qualification,
    experience_years,
    specialty,
    employment_date,
    status
  )
VALUES (
    '李护士',
    0,
    28,
    '13800001001',
    '护士执业资格证',
    5,
    '内科护理，康复护理',
    '2020-03-15',
    1
  ),
  (
    '王护士',
    0,
    32,
    '13800001002',
    '护士执业资格证，营养师证',
    8,
    '老年护理，营养配餐',
    '2018-07-20',
    1
  ),
  (
    '张护士',
    0,
    26,
    '13800001003',
    '护士执业资格证，康复治疗师证',
    3,
    '康复训练，物理治疗',
    '2022-01-10',
    1
  ),
  (
    '赵护士',
    1,
    35,
    '13800001004',
    '护士执业资格证，心理咨询师证',
    10,
    '心理护理，重症护理',
    '2015-09-01',
    1
  ),
  (
    '陈护士',
    0,
    29,
    '13800001005',
    '护士执业资格证',
    6,
    '基础护理，生活照料',
    '2019-05-18',
    1
  );
-- 更新服务记录表，关联护理人员ID
UPDATE elderly_service_record
SET service_provider_id = 1
WHERE service_provider_name = '李护士';
UPDATE elderly_service_record
SET service_provider_id = 2
WHERE service_provider_name = '王护士';
UPDATE elderly_service_record
SET service_provider_id = 3
WHERE service_provider_name = '张护士';
UPDATE elderly_service_record
SET service_provider_id = 4
WHERE service_provider_name = '赵护士';
UPDATE elderly_service_record
SET service_provider_id = 5
WHERE service_provider_name = '陈护士';
-- 增加更多的历史服务记录（用于趋势分析）
INSERT INTO elderly_service_record (
    elderly_id,
    service_date,
    service_type,
    service_provider_id,
    service_provider_name,
    service_content,
    service_duration,
    service_quality_score,
    elderly_satisfaction,
    service_cost,
    service_status
  )
VALUES -- 11月份记录
  (
    1,
    '2024-11-15',
    '生活照料',
    1,
    '李护士',
    '日常生活协助',
    40,
    4,
    4,
    75.00,
    'COMPLETED'
  ),
  (
    2,
    '2024-11-15',
    '医疗护理',
    2,
    '王护士',
    '健康监测',
    30,
    5,
    5,
    45.00,
    'COMPLETED'
  ),
  (
    3,
    '2024-11-15',
    '生活照料',
    3,
    '张护士',
    '全程护理',
    150,
    4,
    4,
    280.00,
    'COMPLETED'
  ),
  -- 10月份记录
  (
    1,
    '2024-10-20',
    '康复训练',
    3,
    '张护士',
    '关节训练',
    50,
    4,
    4,
    110.00,
    'COMPLETED'
  ),
  (
    2,
    '2024-10-20',
    '营养配餐',
    2,
    '王护士',
    '营养指导',
    35,
    4,
    4,
    65.00,
    'COMPLETED'
  ),
  (
    3,
    '2024-10-20',
    '医疗护理',
    4,
    '赵护士',
    '病情监护',
    100,
    5,
    4,
    180.00,
    'COMPLETED'
  );
-- 验证数据
SELECT '照护等级分布统计:' as info;
SELECT care_level,
  CASE
    care_level
    WHEN 1 THEN '自理'
    WHEN 2 THEN '半自理'
    WHEN 3 THEN '不能自理'
    ELSE '未知'
  END as care_level_name,
  COUNT(*) as count,
  ROUND(
    COUNT(*) * 100.0 / (
      SELECT COUNT(*)
      FROM elderly
    ),
    2
  ) as percentage
FROM elderly
WHERE care_level IS NOT NULL
GROUP BY care_level
ORDER BY care_level;
SELECT '服务记录统计:' as info;
SELECT service_type,
  COUNT(*) as count,
  AVG(service_quality_score) as avg_quality,
  AVG(elderly_satisfaction) as avg_satisfaction
FROM elderly_service_record
GROUP BY service_type
ORDER BY count DESC;
SELECT '护理人员工作量统计:' as info;
SELECT c.name,
  COUNT(DISTINCT esr.elderly_id) as elderly_count,
  COUNT(esr.id) as service_count,
  SUM(esr.service_duration) as total_duration,
  AVG(esr.service_quality_score) as avg_rating
FROM caregiver c
  LEFT JOIN elderly_service_record esr ON c.id = esr.service_provider_id
WHERE c.status = 1
GROUP BY c.id,
  c.name
ORDER BY service_count DESC;
COMMIT;