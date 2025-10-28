-- ============================================
-- 智慧医养平台数据库优化脚本
-- 功能: 添加外键约束和性能索引
-- 日期: 2025-10-28
-- ============================================

USE smartcare_cloud;

-- ============================================
-- 1. 外键约束优化
-- ============================================

-- 1.1 健康预警表 (t_health_warning)
-- 添加外键约束确保数据一致性
ALTER TABLE t_health_warning
ADD CONSTRAINT fk_health_warning_elderly 
FOREIGN KEY (elderly_id) REFERENCES elderly(id) 
ON DELETE CASCADE 
ON UPDATE CASCADE
COMMENT '健康预警关联老人外键';

-- 1.2 健康记录表 (t_health_record)  
ALTER TABLE t_health_record
ADD CONSTRAINT fk_health_record_elderly
FOREIGN KEY (elderly_id) REFERENCES elderly(id)
ON DELETE CASCADE
ON UPDATE CASCADE
COMMENT '健康记录关联老人外键';

-- 1.3 设备表 (t_equipment)
-- 如果有老人关联字段
-- ALTER TABLE t_equipment
-- ADD CONSTRAINT fk_equipment_elderly
-- FOREIGN KEY (elderly_id) REFERENCES elderly(id)
-- ON DELETE SET NULL
-- ON UPDATE CASCADE
-- COMMENT '设备关联老人外键';

-- 1.4 家属老人关系表 (t_family_elderly_relation)
ALTER TABLE t_family_elderly_relation
ADD CONSTRAINT fk_family_relation_elderly
FOREIGN KEY (elderly_id) REFERENCES elderly(id)
ON DELETE CASCADE
ON UPDATE CASCADE
COMMENT '家属关系关联老人外键';

ALTER TABLE t_family_elderly_relation
ADD CONSTRAINT fk_family_relation_family
FOREIGN KEY (family_id) REFERENCES t_user(id)
ON DELETE CASCADE
ON UPDATE CASCADE
COMMENT '家属关系关联家属用户外键';


-- ============================================
-- 2. 性能索引优化
-- ============================================

-- 2.1 老人表 (elderly) 索引
-- 健康状态索引 - 用于统计查询
CREATE INDEX idx_elderly_health_status ON elderly(health_status) 
COMMENT '健康状态索引';

-- 护理等级索引 - 用于筛选查询
CREATE INDEX idx_elderly_care_level ON elderly(care_level) 
COMMENT '护理等级索引';

-- 年龄索引 - 用于年龄段统计
CREATE INDEX idx_elderly_age ON elderly(age) 
COMMENT '年龄索引';

-- 创建时间索引 - 用于趋势分析
CREATE INDEX idx_elderly_create_time ON elderly(create_time) 
COMMENT '创建时间索引';

-- 组合索引: 健康状态+护理等级 - 用于复合查询
CREATE INDEX idx_elderly_health_care ON elderly(health_status, care_level) 
COMMENT '健康状态和护理等级组合索引';


-- 2.2 健康预警表 (t_health_warning) 索引
-- 老人ID索引 - 最常用的查询条件
CREATE INDEX idx_warning_elderly_id ON t_health_warning(elderly_id) 
COMMENT '老人ID索引';

-- 预警级别索引 - 用于优先级筛选
CREATE INDEX idx_warning_level ON t_health_warning(level) 
COMMENT '预警级别索引';

-- 预警状态索引 - 用于处理状态筛选
CREATE INDEX idx_warning_status ON t_health_warning(status) 
COMMENT '预警状态索引';

-- 创建时间索引 - 用于时间范围查询
CREATE INDEX idx_warning_created_time ON t_health_warning(created_time) 
COMMENT '创建时间索引';

-- 组合索引: 老人ID+状态 - 用于查询某老人的未处理预警
CREATE INDEX idx_warning_elderly_status ON t_health_warning(elderly_id, status) 
COMMENT '老人ID和状态组合索引';

-- 组合索引: 状态+级别+创建时间 - 用于预警列表查询
CREATE INDEX idx_warning_status_level_time ON t_health_warning(status, level, created_time) 
COMMENT '状态、级别和时间组合索引';

-- 组合索引: 级别+创建时间 - 用于高级别预警查询
CREATE INDEX idx_warning_level_time ON t_health_warning(level, created_time) 
COMMENT '级别和时间组合索引';


-- 2.3 健康记录表 (t_health_record) 索引
-- 老人ID索引
CREATE INDEX idx_record_elderly_id ON t_health_record(elderly_id) 
COMMENT '老人ID索引';

-- 记录时间索引
CREATE INDEX idx_record_time ON t_health_record(record_time) 
COMMENT '记录时间索引';

-- 组合索引: 老人ID+记录时间 - 用于时间序列查询
CREATE INDEX idx_record_elderly_time ON t_health_record(elderly_id, record_time) 
COMMENT '老人ID和记录时间组合索引';


-- 2.4 设备表 (t_equipment) 索引
-- 设备状态索引
CREATE INDEX idx_equipment_status ON t_equipment(status) 
COMMENT '设备状态索引';

-- 设备类型索引
CREATE INDEX idx_equipment_type ON t_equipment(device_type) 
COMMENT '设备类型索引';

-- 最后在线时间索引
CREATE INDEX idx_equipment_last_online ON t_equipment(last_online_time) 
COMMENT '最后在线时间索引';


-- 2.5 用户表 (t_user) 索引
-- 用户名唯一索引
CREATE UNIQUE INDEX uk_user_username ON t_user(username) 
COMMENT '用户名唯一索引';

-- 手机号索引
CREATE INDEX idx_user_phone ON t_user(phone) 
COMMENT '手机号索引';

-- 用户类型索引
CREATE INDEX idx_user_type ON t_user(user_type) 
COMMENT '用户类型索引';


-- 2.6 家属关系表 (t_family_elderly_relation) 索引
-- 老人ID索引
CREATE INDEX idx_family_elderly_id ON t_family_elderly_relation(elderly_id) 
COMMENT '老人ID索引';

-- 家属ID索引
CREATE INDEX idx_family_family_id ON t_family_elderly_relation(family_id) 
COMMENT '家属ID索引';

-- 关系类型索引
CREATE INDEX idx_family_relation_type ON t_family_elderly_relation(relation_type) 
COMMENT '关系类型索引';

-- 组合索引: 家属ID+老人ID - 用于关系查询
CREATE INDEX idx_family_relation ON t_family_elderly_relation(family_id, elderly_id) 
COMMENT '家属和老人组合索引';


-- 2.7 医生表 (t_doctor) 索引
-- 科室索引
CREATE INDEX idx_doctor_department ON t_doctor(department) 
COMMENT '科室索引';

-- 职称索引
CREATE INDEX idx_doctor_title ON t_doctor(title) 
COMMENT '职称索引';

-- 状态索引
CREATE INDEX idx_doctor_status ON t_doctor(status) 
COMMENT '状态索引';


-- ============================================
-- 3. 查看索引创建结果
-- ============================================

-- 查看健康预警表索引
SHOW INDEX FROM t_health_warning;

-- 查看老人表索引
SHOW INDEX FROM elderly;

-- 查看健康记录表索引
SHOW INDEX FROM t_health_record;

-- 查看设备表索引
SHOW INDEX FROM t_equipment;


-- ============================================
-- 4. 性能分析建议
-- ============================================

-- 4.1 分析查询性能 (示例)
-- EXPLAIN SELECT * FROM t_health_warning WHERE elderly_id = 1 AND status = 0;

-- 4.2 检查索引使用情况
-- SELECT 
--     TABLE_NAME,
--     INDEX_NAME,
--     SEQ_IN_INDEX,
--     COLUMN_NAME,
--     CARDINALITY,
--     INDEX_TYPE
-- FROM information_schema.STATISTICS
-- WHERE TABLE_SCHEMA = 'smartcare_cloud'
-- ORDER BY TABLE_NAME, INDEX_NAME, SEQ_IN_INDEX;

-- 4.3 查看表大小和索引大小
-- SELECT 
--     TABLE_NAME AS '表名',
--     ROUND(DATA_LENGTH/1024/1024, 2) AS '数据大小(MB)',
--     ROUND(INDEX_LENGTH/1024/1024, 2) AS '索引大小(MB)',
--     ROUND((DATA_LENGTH + INDEX_LENGTH)/1024/1024, 2) AS '总大小(MB)',
--     TABLE_ROWS AS '行数'
-- FROM information_schema.TABLES
-- WHERE TABLE_SCHEMA = 'smartcare_cloud'
-- ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;


-- ============================================
-- 执行完成提示
-- ============================================

SELECT '数据库优化完成! 已添加外键约束和性能索引。' AS message;
