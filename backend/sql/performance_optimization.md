# 数据库性能优化SQL脚本

本文件包含智慧医养平台的数据库性能优化索引和查询优化建议。

## 📊 索引优化策略

### 1. 老人档案表 (elderly_info)

```sql
-- 已有索引
-- PRIMARY KEY (id)

-- 新增推荐索引
-- 姓名模糊查询索引（支持 LIKE 'xxx%'）
CREATE INDEX idx_elderly_name ON elderly_info(name);

-- 健康状态查询索引（常用于统计和筛选）
CREATE INDEX idx_elderly_health_status ON elderly_info(health_status);

-- 身份证号唯一索引（业务唯一键）
CREATE UNIQUE INDEX uk_elderly_id_card ON elderly_info(id_card);

-- 联系电话索引（支持快速查询）
CREATE INDEX idx_elderly_phone ON elderly_info(phone);

-- 创建时间索引（支持时间范围查询）
CREATE INDEX idx_elderly_create_time ON elderly_info(create_time);

-- 复合索引：健康状态+创建时间（支持统计分析）
CREATE INDEX idx_elderly_health_create ON elderly_info(health_status, create_time);

-- 照护等级索引（支持照护统计）
CREATE INDEX idx_elderly_care_level ON elderly_info(care_level);
```

### 2. 健康预警表 (health_warning)

```sql
-- 已有索引
-- PRIMARY KEY (id)

-- 新增推荐索引
-- 老人ID索引（关联查询）
CREATE INDEX idx_warning_elderly_id ON health_warning(elderly_id);

-- 预警状态索引（常用于筛选待处理预警）
CREATE INDEX idx_warning_status ON health_warning(status);

-- 预警等级索引（按严重程度查询）
CREATE INDEX idx_warning_level ON health_warning(warning_level);

-- 处理人ID索引（查询医生处理的预警）
CREATE INDEX idx_warning_handler_id ON health_warning(handler_id);

-- 创建时间索引（时间范围查询）
CREATE INDEX idx_warning_create_time ON health_warning(create_time);

-- 复合索引：老人ID+状态（家属查看老人预警）
CREATE INDEX idx_warning_elderly_status ON health_warning(elderly_id, status);

-- 复合索引：状态+等级+创建时间（预警列表查询）
CREATE INDEX idx_warning_status_level_time ON health_warning(status, warning_level, create_time);

-- 复合索引：处理人+状态（医生工作台查询）
CREATE INDEX idx_warning_handler_status ON health_warning(handler_id, status);
```

### 3. 健康记录表 (health_record)

```sql
-- 已有索引
-- PRIMARY KEY (id)

-- 新增推荐索引
-- 老人ID索引（查询老人所有健康记录）
CREATE INDEX idx_record_elderly_id ON health_record(elderly_id);

-- 记录时间索引（时间范围查询）
CREATE INDEX idx_record_record_time ON health_record(record_time);

-- 记录类型索引（按类型筛选）
CREATE INDEX idx_record_type ON health_record(record_type);

-- 复合索引：老人ID+记录时间（获取老人历史记录）
CREATE INDEX idx_record_elderly_time ON health_record(elderly_id, record_time DESC);

-- 复合索引：老人ID+类型+时间（按类型查询记录）
CREATE INDEX idx_record_elderly_type_time ON health_record(elderly_id, record_type, record_time DESC);
```

### 4. 医生表 (doctor_info)

```sql
-- 已有索引
-- PRIMARY KEY (id)

-- 新增推荐索引
-- 工号唯一索引（业务唯一键）
CREATE UNIQUE INDEX uk_doctor_employee_number ON doctor_info(employee_number);

-- 姓名索引（支持模糊查询）
CREATE INDEX idx_doctor_name ON doctor_info(name);

-- 科室索引（按科室筛选）
CREATE INDEX idx_doctor_department ON doctor_info(department);

-- 职称索引（按职称筛选）
CREATE INDEX idx_doctor_title ON doctor_info(title);

-- 联系电话索引
CREATE INDEX idx_doctor_phone ON doctor_info(phone);

-- 复合索引：科室+职称（科室人员管理）
CREATE INDEX idx_doctor_dept_title ON doctor_info(department, title);
```

### 5. 用户表 (user)

```sql
-- 已有索引
-- PRIMARY KEY (id)

-- 新增推荐索引
-- 用户名唯一索引（登录查询）
CREATE UNIQUE INDEX uk_user_username ON user(username);

-- 手机号唯一索引（手机登录）
CREATE UNIQUE INDEX uk_user_phone ON user(phone);

-- 邮箱唯一索引（邮箱登录）
CREATE UNIQUE INDEX uk_user_email ON user(email) WHERE email IS NOT NULL;

-- 角色索引（按角色查询用户）
CREATE INDEX idx_user_role ON user(role_id);

-- 状态索引（筛选启用/禁用用户）
CREATE INDEX idx_user_status ON user(status);

-- 复合索引：角色+状态（用户管理列表）
CREATE INDEX idx_user_role_status ON user(role_id, status);
```

### 6. 家属老人关联表 (family_elderly_relation)

```sql
-- 已有索引
-- PRIMARY KEY (id)

-- 新增推荐索引
-- 家属用户ID索引（查询家属关联的所有老人）
CREATE INDEX idx_relation_family_user_id ON family_elderly_relation(family_user_id);

-- 老人ID索引（查询老人的所有家属）
CREATE INDEX idx_relation_elderly_id ON family_elderly_relation(elderly_id);

-- 关系类型索引
CREATE INDEX idx_relation_relationship ON family_elderly_relation(relationship);

-- 唯一约束：防止重复关联
CREATE UNIQUE INDEX uk_relation_family_elderly ON family_elderly_relation(family_user_id, elderly_id);
```

### 7. 设备表 (equipment)

```sql
-- 已有索引
-- PRIMARY KEY (id)

-- 新增推荐索引
-- 设备编号唯一索引
CREATE UNIQUE INDEX uk_equipment_code ON equipment(equipment_code);

-- 设备状态索引
CREATE INDEX idx_equipment_status ON equipment(status);

-- 设备类型索引
CREATE INDEX idx_equipment_type ON equipment(equipment_type);

-- 关联老人ID索引
CREATE INDEX idx_equipment_elderly_id ON equipment(elderly_id) WHERE elderly_id IS NOT NULL;

-- 复合索引：类型+状态（设备管理）
CREATE INDEX idx_equipment_type_status ON equipment(equipment_type, status);
```

## 🚀 查询优化建议

### 1. 分页查询优化

**问题查询:**
```sql
-- 慢查询：使用 OFFSET
SELECT * FROM elderly_info 
WHERE health_status = 'HEALTHY'
ORDER BY create_time DESC
LIMIT 20 OFFSET 1000;  -- 需要扫描1020行
```

**优化方案:**
```sql
-- 使用游标分页（记录上次查询的最后一条记录的ID）
SELECT * FROM elderly_info 
WHERE health_status = 'HEALTHY'
  AND id < 上次查询的最后ID
ORDER BY id DESC
LIMIT 20;
```

### 2. 统计查询优化

**问题查询:**
```sql
-- 多次COUNT查询
SELECT COUNT(*) FROM elderly_info WHERE health_status = 'HEALTHY';
SELECT COUNT(*) FROM elderly_info WHERE health_status = 'SUBHEALTH';
SELECT COUNT(*) FROM elderly_info WHERE health_status = 'SICK';
```

**优化方案:**
```sql
-- 一次GROUP BY查询
SELECT health_status, COUNT(*) as count 
FROM elderly_info 
GROUP BY health_status;
```

### 3. 关联查询优化

**问题查询:**
```sql
-- N+1查询问题
SELECT * FROM health_warning WHERE status = 'PENDING';
-- 然后为每条预警查询老人信息
SELECT * FROM elderly_info WHERE id = ?;
```

**优化方案:**
```sql
-- 使用JOIN一次查询
SELECT 
    hw.*,
    e.name as elderly_name,
    e.phone as elderly_phone,
    e.health_status
FROM health_warning hw
INNER JOIN elderly_info e ON hw.elderly_id = e.id
WHERE hw.status = 'PENDING'
ORDER BY hw.create_time DESC;
```

### 4. 索引覆盖查询

**问题查询:**
```sql
-- 回表查询
SELECT id, name, phone, health_status 
FROM elderly_info 
WHERE health_status = 'HEALTHY';
```

**优化方案:**
```sql
-- 创建覆盖索引
CREATE INDEX idx_elderly_health_covering 
ON elderly_info(health_status, name, phone);

-- 这样查询可以直接从索引获取数据，无需回表
```

## 📈 性能监控SQL

### 1. 慢查询分析

```sql
-- 查看慢查询日志
SHOW VARIABLES LIKE 'slow_query_log';
SHOW VARIABLES LIKE 'long_query_time';

-- 开启慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;  -- 超过2秒的查询会被记录
```

### 2. 索引使用情况

```sql
-- 查看表的索引
SHOW INDEX FROM elderly_info;

-- 分析查询是否使用索引
EXPLAIN SELECT * FROM elderly_info WHERE name LIKE '张%';

-- 查看未使用的索引
SELECT * FROM sys.schema_unused_indexes WHERE object_schema = 'smartcare_cloud';
```

### 3. 表统计信息

```sql
-- 查看表行数和大小
SELECT 
    table_name,
    table_rows,
    ROUND(data_length / 1024 / 1024, 2) AS data_mb,
    ROUND(index_length / 1024 / 1024, 2) AS index_mb
FROM information_schema.tables
WHERE table_schema = 'smartcare_cloud'
ORDER BY data_length DESC;
```

## ⚠️ 注意事项

1. **索引不是越多越好**
   - 每个索引都会占用存储空间
   - 写操作（INSERT/UPDATE/DELETE）需要维护索引，会降低性能
   - 建议定期监控索引使用情况，删除未使用的索引

2. **复合索引顺序很重要**
   - 遵循"最左前缀"原则
   - 选择性高的列放在前面
   - 查询条件中使用频率高的列放在前面

3. **定期维护**
   - 定期分析表：`ANALYZE TABLE table_name;`
   - 定期优化表：`OPTIMIZE TABLE table_name;`
   - 监控表碎片率

4. **执行建议**
   - 在业务低峰期执行索引创建
   - 大表创建索引时使用 `ALGORITHM=INPLACE` 避免锁表
   - 先在测试环境验证效果

## 🔧 执行命令

```bash
# 连接数据库
mysql -u root -p smartcare_cloud

# 执行索引创建（建议在业务低峰期）
source performance_indexes.sql

# 查看执行结果
SHOW INDEX FROM elderly_info;
```
