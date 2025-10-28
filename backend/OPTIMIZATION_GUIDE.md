# 数据库优化执行指南

## 快速执行步骤

### 前置检查

1. **备份数据库** (重要!)
```bash
mysqldump -u root -p smartcare_cloud > smartcare_cloud_backup_$(date +%Y%m%d).sql
```

2. **检查MySQL服务状态**
```powershell
Get-Service -Name MySQL*
```

3. **检查当前数据库状态**
```bash
mysql -u root -p smartcare_cloud < backend/check_database_status.sql > db_status_before.txt
```

---

### 方式一: 命令行执行 (推荐)

```bash
# 1. 连接数据库
mysql -u root -p

# 2. 选择数据库
USE smartcare_cloud;

# 3. 执行优化脚本
SOURCE d:/repositories/SmartCare_Cloud/backend/database_optimization.sql;

# 4. 验证结果
SHOW INDEX FROM t_health_warning;
SHOW INDEX FROM elderly;

# 5. 查看外键
SELECT * FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND REFERENCED_TABLE_NAME IS NOT NULL;
```

---

### 方式二: PowerShell一键执行

```powershell
# 设置变量
$dbUser = "root"
$dbName = "smartcare_cloud"
$scriptPath = "d:\repositories\SmartCare_Cloud\backend\database_optimization.sql"

# 执行优化脚本
mysql -u $dbUser -p $dbName < $scriptPath

# 执行检查脚本
mysql -u $dbUser -p $dbName < "d:\repositories\SmartCare_Cloud\backend\check_database_status.sql"
```

---

### 方式三: 分步执行 (谨慎模式)

如果担心一次执行全部脚本,可以分步执行:

#### 步骤1: 先添加索引 (低风险)
```sql
-- 复制database_optimization.sql中的索引部分
-- 从 "2. 性能索引优化" 开始到结束
```

#### 步骤2: 清理孤立数据 (必须!)
```sql
-- 执行外键前必须清理不一致数据
DELETE FROM t_health_warning 
WHERE elderly_id NOT IN (SELECT id FROM elderly);

DELETE FROM t_health_record 
WHERE elderly_id NOT IN (SELECT id FROM elderly);

DELETE FROM t_family_elderly_relation 
WHERE elderly_id NOT IN (SELECT id FROM elderly);

DELETE FROM t_family_elderly_relation 
WHERE family_id NOT IN (SELECT id FROM t_user);
```

#### 步骤3: 添加外键约束 (生产环境建议维护期执行)
```sql
-- 复制database_optimization.sql中的外键部分
-- 从 "1. 外键约束优化" 开始
```

---

## 验证清单

### ✅ Lombok验证
```bash
cd backend
mvn clean compile

# 检查是否编译成功
# 检查Elderly.java是否简化
```

### ✅ 索引验证
```sql
-- 检查健康预警表索引
SHOW INDEX FROM t_health_warning;

-- 预期结果: 应该看到以下索引
-- idx_warning_elderly_id
-- idx_warning_level
-- idx_warning_status
-- idx_warning_created_time
-- idx_warning_elderly_status
-- idx_warning_status_level_time
-- idx_warning_level_time
```

### ✅ 外键验证
```sql
-- 查看所有外键约束
SELECT 
    TABLE_NAME,
    CONSTRAINT_NAME,
    COLUMN_NAME,
    REFERENCED_TABLE_NAME,
    REFERENCED_COLUMN_NAME
FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

-- 预期结果: 应该看到
-- fk_health_warning_elderly
-- fk_health_record_elderly
-- fk_family_relation_elderly
-- fk_family_relation_family
```

### ✅ 性能验证
```sql
-- 执行查询计划分析
EXPLAIN SELECT * FROM t_health_warning 
WHERE elderly_id = 1 AND status = 0;

-- 预期结果:
-- type: ref (使用索引)
-- key: idx_warning_elderly_status
-- rows: 很少 (不是ALL全表扫描)
```

---

## 回滚方案

如果优化后出现问题,可以回滚:

### 回滚索引
```sql
-- 删除创建的索引
DROP INDEX idx_warning_elderly_id ON t_health_warning;
DROP INDEX idx_warning_level ON t_health_warning;
-- ... 依次删除其他索引
```

### 回滚外键
```sql
-- 删除外键约束
ALTER TABLE t_health_warning DROP FOREIGN KEY fk_health_warning_elderly;
ALTER TABLE t_health_record DROP FOREIGN KEY fk_health_record_elderly;
-- ... 依次删除其他外键
```

### 恢复备份
```bash
# 如果需要完全回滚,恢复备份
mysql -u root -p smartcare_cloud < smartcare_cloud_backup_20251028.sql
```

---

## 常见问题

### Q1: 添加外键时报错 "Cannot add foreign key constraint"
**原因:** 存在孤立数据(子表引用的父表记录不存在)
**解决:** 先执行步骤2清理孤立数据

### Q2: 添加索引失败
**原因:** 字段不存在或字段类型不支持索引
**解决:** 检查字段是否存在,确认字段类型

### Q3: 性能反而下降
**原因:** 索引过多或不合理
**解决:** 删除不必要的索引,保留最常用的

### Q4: Lombok编译失败
**原因:** 注解处理器配置错误
**解决:** 检查pom.xml中annotationProcessorPaths配置

---

## 执行时间预估

| 操作 | 预估时间 | 影响 |
|------|---------|------|
| 添加索引 | 1-5分钟 | 无影响 |
| 清理孤立数据 | 取决于数据量 | 锁表 |
| 添加外键 | 1-3分钟 | 锁表 |
| 验证测试 | 5-10分钟 | 无影响 |
| **总计** | **10-20分钟** | - |

**建议:** 在业务低峰期或维护窗口执行

---

## 执行后监控

执行优化后,需要持续监控:

1. **查询性能监控**
```sql
-- 启用慢查询日志
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 1;

-- 查看慢查询
SELECT * FROM mysql.slow_log ORDER BY start_time DESC LIMIT 10;
```

2. **索引使用情况**
```sql
-- 查看未使用的索引
SELECT * FROM sys.schema_unused_indexes 
WHERE object_schema = 'smartcare_cloud';
```

3. **表空间监控**
```sql
-- 定期检查表大小
SELECT 
    TABLE_NAME,
    ROUND(DATA_LENGTH/1024/1024, 2) AS 'Data(MB)',
    ROUND(INDEX_LENGTH/1024/1024, 2) AS 'Index(MB)'
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'smartcare_cloud'
ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;
```

---

## 完成确认

执行完成后,请确认以下检查点:

- [ ] Lombok依赖已启用,Maven编译成功
- [ ] Elderly实体类已简化,无getter/setter
- [ ] 数据库已备份
- [ ] 17个索引已创建
- [ ] 4个外键约束已添加
- [ ] 查询执行计划显示使用索引
- [ ] 无数据丢失或损坏
- [ ] 应用程序功能正常

**确认后即可投入使用!**
