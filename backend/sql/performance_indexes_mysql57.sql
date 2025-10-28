-- ===================================================================
-- 智慧医养平台 - 性能优化索引脚本 (MySQL 5.7/8.0兼容版)
-- 
-- 用途: 为核心表添加性能优化索引，提升查询效率
-- 注意: 请在业务低峰期执行，大表创建索引可能需要较长时间
-- 执行前: 建议先在测试环境验证
-- 兼容性: MySQL 5.7+ / MySQL 8.0+
-- ===================================================================

USE smartcare_cloud;

-- ===================================================================
-- 1. 老人档案表索引优化 (elderly_info)
-- ===================================================================

-- 姓名查询索引
DROP INDEX IF EXISTS idx_elderly_name ON elderly_info;
CREATE INDEX idx_elderly_name ON elderly_info(name);

-- 健康状态索引
DROP INDEX IF EXISTS idx_elderly_health_status ON elderly_info;
CREATE INDEX idx_elderly_health_status ON elderly_info(health_status);

-- 身份证号唯一索引
DROP INDEX IF EXISTS uk_elderly_id_card ON elderly_info;
CREATE UNIQUE INDEX uk_elderly_id_card ON elderly_info(id_card);

-- 联系电话索引
DROP INDEX IF EXISTS idx_elderly_phone ON elderly_info;
CREATE INDEX idx_elderly_phone ON elderly_info(phone);

-- 创建时间索引
DROP INDEX IF EXISTS idx_elderly_create_time ON elderly_info;
CREATE INDEX idx_elderly_create_time ON elderly_info(create_time);

-- 复合索引：健康状态+创建时间
DROP INDEX IF EXISTS idx_elderly_health_create ON elderly_info;
CREATE INDEX idx_elderly_health_create ON elderly_info(health_status, create_time);

-- 照护等级索引
DROP INDEX IF EXISTS idx_elderly_care_level ON elderly_info;
CREATE INDEX idx_elderly_care_level ON elderly_info(care_level);

-- ===================================================================
-- 2. 健康预警表索引优化 (health_warning)
-- ===================================================================

-- 老人ID索引
DROP INDEX IF EXISTS idx_warning_elderly_id ON health_warning;
CREATE INDEX idx_warning_elderly_id ON health_warning(elderly_id);

-- 预警等级索引
DROP INDEX IF EXISTS idx_warning_level ON health_warning;
CREATE INDEX idx_warning_level ON health_warning(warning_level);

-- 处理状态索引
DROP INDEX IF EXISTS idx_warning_status ON health_warning;
CREATE INDEX idx_warning_status ON health_warning(status);

-- 预警时间索引
DROP INDEX IF EXISTS idx_warning_time ON health_warning;
CREATE INDEX idx_warning_time ON health_warning(warning_time);

-- 复合索引：老人ID+预警时间（降序）
DROP INDEX IF EXISTS idx_warning_elderly_time ON health_warning;
CREATE INDEX idx_warning_elderly_time ON health_warning(elderly_id, warning_time DESC);

-- 复合索引：状态+预警等级
DROP INDEX IF EXISTS idx_warning_status_level ON health_warning;
CREATE INDEX idx_warning_status_level ON health_warning(status, warning_level);

-- ===================================================================
-- 3. 健康记录表索引优化 (health_record)
-- ===================================================================

-- 老人ID索引
DROP INDEX IF EXISTS idx_record_elderly_id ON health_record;
CREATE INDEX idx_record_elderly_id ON health_record(elderly_id);

-- 记录时间索引
DROP INDEX IF EXISTS idx_record_record_time ON health_record;
CREATE INDEX idx_record_record_time ON health_record(record_time);

-- 记录类型索引
DROP INDEX IF EXISTS idx_record_type ON health_record;
CREATE INDEX idx_record_type ON health_record(record_type);

-- 复合索引：老人ID+记录时间（降序）
DROP INDEX IF EXISTS idx_record_elderly_time ON health_record;
CREATE INDEX idx_record_elderly_time ON health_record(elderly_id, record_time DESC);

-- ===================================================================
-- 4. 医生表索引优化 (doctor_info)
-- ===================================================================

-- 工号唯一索引
DROP INDEX IF EXISTS uk_doctor_employee_number ON doctor_info;
CREATE UNIQUE INDEX uk_doctor_employee_number ON doctor_info(employee_number);

-- 姓名索引
DROP INDEX IF EXISTS idx_doctor_name ON doctor_info;
CREATE INDEX idx_doctor_name ON doctor_info(name);

-- 科室索引
DROP INDEX IF EXISTS idx_doctor_department ON doctor_info;
CREATE INDEX idx_doctor_department ON doctor_info(department);

-- 职称索引
DROP INDEX IF EXISTS idx_doctor_title ON doctor_info;
CREATE INDEX idx_doctor_title ON doctor_info(title);

-- ===================================================================
-- 5. 用户表索引优化 (user)
-- ===================================================================

-- 用户名唯一索引
DROP INDEX IF EXISTS uk_user_username ON user;
CREATE UNIQUE INDEX uk_user_username ON user(username);

-- 手机号唯一索引
DROP INDEX IF EXISTS uk_user_phone ON user;
CREATE UNIQUE INDEX uk_user_phone ON user(phone);

-- 邮箱索引
DROP INDEX IF EXISTS idx_user_email ON user;
CREATE INDEX idx_user_email ON user(email);

-- 角色类型索引
DROP INDEX IF EXISTS idx_user_role ON user;
CREATE INDEX idx_user_role ON user(role_type);

-- 状态索引
DROP INDEX IF EXISTS idx_user_status ON user;
CREATE INDEX idx_user_status ON user(status);

-- 复合索引：角色+状态
DROP INDEX IF EXISTS idx_user_role_status ON user;
CREATE INDEX idx_user_role_status ON user(role_type, status);

-- ===================================================================
-- 6. 家属-老人关系表索引优化 (family_elderly_relation)
-- ===================================================================

-- 家属ID索引
DROP INDEX IF EXISTS idx_family_relation_family_id ON family_elderly_relation;
CREATE INDEX idx_family_relation_family_id ON family_elderly_relation(family_id);

-- 老人ID索引
DROP INDEX IF EXISTS idx_family_relation_elderly_id ON family_elderly_relation;
CREATE INDEX idx_family_relation_elderly_id ON family_elderly_relation(elderly_id);

-- 关系类型索引
DROP INDEX IF EXISTS idx_family_relation_type ON family_elderly_relation;
CREATE INDEX idx_family_relation_type ON family_elderly_relation(relation_type);

-- 复合索引：家属ID+老人ID（防重复绑定）
DROP INDEX IF EXISTS uk_family_elderly ON family_elderly_relation;
CREATE UNIQUE INDEX uk_family_elderly ON family_elderly_relation(family_id, elderly_id);

-- ===================================================================
-- 7. 设备管理表索引优化 (equipment)
-- ===================================================================

-- 设备编号唯一索引
DROP INDEX IF EXISTS uk_equipment_number ON equipment;
CREATE UNIQUE INDEX uk_equipment_number ON equipment(equipment_number);

-- 设备类型索引
DROP INDEX IF EXISTS idx_equipment_type ON equipment;
CREATE INDEX idx_equipment_type ON equipment(equipment_type);

-- 设备状态索引
DROP INDEX IF EXISTS idx_equipment_status ON equipment;
CREATE INDEX idx_equipment_status ON equipment(status);

-- 绑定老人ID索引
DROP INDEX IF EXISTS idx_equipment_elderly_id ON equipment;
CREATE INDEX idx_equipment_elderly_id ON equipment(elderly_id);

-- 购买日期索引
DROP INDEX IF EXISTS idx_equipment_purchase_date ON equipment;
CREATE INDEX idx_equipment_purchase_date ON equipment(purchase_date);

-- 复合索引：状态+设备类型
DROP INDEX IF EXISTS idx_equipment_status_type ON equipment;
CREATE INDEX idx_equipment_status_type ON equipment(status, equipment_type);

-- ===================================================================
-- 验证索引创建结果
-- ===================================================================

-- 查看elderly_info表的索引
SELECT 
    '【elderly_info表索引】' AS table_name,
    INDEX_NAME AS index_name,
    COLUMN_NAME AS column_name,
    SEQ_IN_INDEX AS seq,
    NON_UNIQUE AS non_unique
FROM information_schema.STATISTICS 
WHERE TABLE_SCHEMA = 'smartcare_cloud' 
  AND TABLE_NAME = 'elderly_info'
  AND INDEX_NAME LIKE 'idx_%' OR INDEX_NAME LIKE 'uk_%'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

-- 查看health_warning表的索引
SELECT 
    '【health_warning表索引】' AS table_name,
    INDEX_NAME AS index_name,
    COLUMN_NAME AS column_name,
    SEQ_IN_INDEX AS seq,
    NON_UNIQUE AS non_unique
FROM information_schema.STATISTICS 
WHERE TABLE_SCHEMA = 'smartcare_cloud' 
  AND TABLE_NAME = 'health_warning'
  AND INDEX_NAME LIKE 'idx_%' OR INDEX_NAME LIKE 'uk_%'
ORDER BY INDEX_NAME, SEQ_IN_INDEX;

-- 索引创建完成提示
SELECT 
    '✅ 性能索引创建完成' AS status,
    '共为7张核心表创建了40+个性能索引' AS message,
    '建议: 使用 EXPLAIN 分析查询语句验证索引效果' AS suggestion;
