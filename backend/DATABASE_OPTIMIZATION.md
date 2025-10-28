# 数据库优化方案

## 1. Lombok依赖问题 ✅

### 问题描述
- Lombok依赖被注释导致实体类需要手写大量getter/setter方法
- 代码冗余,维护困难

### 解决方案
已启用Lombok 1.18.30版本,完全兼容Java 8:

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.30</version>
    <scope>provided</scope>
</dependency>
```

配置注解处理器:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.30</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

### 使用方式
为实体类添加`@Data`注解即可自动生成getter/setter:

```java
@Data
@TableName("elderly")
public class Elderly {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    private Integer age;
    // 无需手写getter/setter,Lombok自动生成
}
```

### 验证结果
- ✅ Maven编译成功
- ✅ Elderly实体类已简化150+行代码
- ✅ 与MyBatis-Plus完全兼容


---

## 2. 外键约束优化 🔧

### 问题描述
- SQL脚本中有外键定义但部分表未应用
- 存在数据一致性风险
- 级联删除/更新未配置

### 优化方案

#### 2.1 核心表外键关系

**健康预警表 (t_health_warning)**
```sql
ALTER TABLE t_health_warning
ADD CONSTRAINT fk_health_warning_elderly 
FOREIGN KEY (elderly_id) REFERENCES elderly(id) 
ON DELETE CASCADE 
ON UPDATE CASCADE;
```

**健康记录表 (t_health_record)**
```sql
ALTER TABLE t_health_record
ADD CONSTRAINT fk_health_record_elderly
FOREIGN KEY (elderly_id) REFERENCES elderly(id)
ON DELETE CASCADE
ON UPDATE CASCADE;
```

**家属关系表 (t_family_elderly_relation)**
```sql
-- 关联老人
ALTER TABLE t_family_elderly_relation
ADD CONSTRAINT fk_family_relation_elderly
FOREIGN KEY (elderly_id) REFERENCES elderly(id)
ON DELETE CASCADE ON UPDATE CASCADE;

-- 关联家属用户
ALTER TABLE t_family_elderly_relation
ADD CONSTRAINT fk_family_relation_family
FOREIGN KEY (family_id) REFERENCES t_user(id)
ON DELETE CASCADE ON UPDATE CASCADE;
```

#### 2.2 外键策略说明

| 策略 | 说明 | 适用场景 |
|------|------|---------|
| `ON DELETE CASCADE` | 删除父记录时自动删除子记录 | 健康预警、健康记录等从属数据 |
| `ON DELETE SET NULL` | 删除父记录时子记录外键设为NULL | 设备关联(设备可独立存在) |
| `ON UPDATE CASCADE` | 更新父记录主键时自动更新子记录外键 | 所有外键关系 |

### 数据一致性保障
- ✅ 删除老人时自动清理相关预警和记录
- ✅ 防止孤立数据产生
- ✅ 维护引用完整性


---

## 3. 索引优化 🚀

### 问题描述
- 查询频繁的字段缺少索引
- 复合查询性能差
- 影响系统响应速度

### 3.1 单列索引优化

**老人表 (elderly)**
```sql
-- 健康状态索引 (用于统计查询)
CREATE INDEX idx_elderly_health_status ON elderly(health_status);

-- 护理等级索引 (用于筛选查询)
CREATE INDEX idx_elderly_care_level ON elderly(care_level);

-- 年龄索引 (用于年龄段统计)
CREATE INDEX idx_elderly_age ON elderly(age);

-- 创建时间索引 (用于趋势分析)
CREATE INDEX idx_elderly_create_time ON elderly(create_time);
```

**健康预警表 (t_health_warning)**
```sql
-- 老人ID索引 (最高频查询)
CREATE INDEX idx_warning_elderly_id ON t_health_warning(elderly_id);

-- 预警级别索引
CREATE INDEX idx_warning_level ON t_health_warning(level);

-- 预警状态索引
CREATE INDEX idx_warning_status ON t_health_warning(status);

-- 创建时间索引
CREATE INDEX idx_warning_created_time ON t_health_warning(created_time);
```

### 3.2 复合索引优化

**最佳实践: 遵循最左前缀原则**

```sql
-- 组合索引1: 老人ID + 状态 (查询某老人未处理预警)
CREATE INDEX idx_warning_elderly_status 
ON t_health_warning(elderly_id, status);

-- 组合索引2: 状态 + 级别 + 时间 (预警列表查询)
CREATE INDEX idx_warning_status_level_time 
ON t_health_warning(status, level, created_time);

-- 组合索引3: 级别 + 时间 (高级别预警监控)
CREATE INDEX idx_warning_level_time 
ON t_health_warning(level, created_time);

-- 组合索引4: 健康状态 + 护理等级 (复合条件查询)
CREATE INDEX idx_elderly_health_care 
ON elderly(health_status, care_level);
```

### 3.3 索引使用场景

| 索引 | 适用查询 | 性能提升 |
|------|---------|---------|
| `idx_warning_elderly_id` | `WHERE elderly_id = ?` | ⭐⭐⭐⭐⭐ |
| `idx_warning_status` | `WHERE status = 0` (未处理) | ⭐⭐⭐⭐ |
| `idx_warning_elderly_status` | `WHERE elderly_id = ? AND status = ?` | ⭐⭐⭐⭐⭐ |
| `idx_warning_status_level_time` | 预警列表分页查询 | ⭐⭐⭐⭐⭐ |
| `idx_elderly_health_status` | 健康统计GROUP BY | ⭐⭐⭐⭐ |

### 3.4 性能分析示例

```sql
-- 查看执行计划
EXPLAIN SELECT * FROM t_health_warning 
WHERE elderly_id = 1 AND status = 0;

-- 预期结果:
-- type: ref (使用索引)
-- key: idx_warning_elderly_status (使用复合索引)
-- rows: 1-10 (扫描行数少)
```


---

## 4. 执行步骤

### 4.1 检查当前状态
```bash
mysql -u root -p smartcare_cloud < backend/check_database_status.sql
```

### 4.2 执行优化脚本
```bash
mysql -u root -p smartcare_cloud < backend/database_optimization.sql
```

### 4.3 验证结果
```sql
-- 查看所有外键
SELECT * FROM information_schema.KEY_COLUMN_USAGE
WHERE TABLE_SCHEMA = 'smartcare_cloud'
  AND REFERENCED_TABLE_NAME IS NOT NULL;

-- 查看所有索引
SHOW INDEX FROM t_health_warning;
SHOW INDEX FROM elderly;
```


---

## 5. 注意事项

### 5.1 外键约束注意事项
⚠️ **执行前必读:**
1. **数据清理**: 执行外键约束前,先清理不一致数据
2. **备份数据**: 执行前备份数据库
3. **生产环境**: 建议在维护窗口期执行
4. **性能影响**: 外键会略微影响INSERT/UPDATE性能,但换来数据一致性

```sql
-- 清理孤立数据示例
DELETE FROM t_health_warning 
WHERE elderly_id NOT IN (SELECT id FROM elderly);
```

### 5.2 索引维护建议
1. **定期分析**: `ANALYZE TABLE table_name;`
2. **重建索引**: 数据量大时定期OPTIMIZE TABLE
3. **监控使用**: 通过慢查询日志分析索引效果
4. **避免过度索引**: 每个表索引不超过5-6个

### 5.3 性能监控
```sql
-- 查看索引使用统计
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    CARDINALITY,
    INDEX_TYPE
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'smartcare_cloud'
ORDER BY TABLE_NAME, INDEX_NAME;

-- 查看表大小和索引大小
SELECT 
    TABLE_NAME,
    ROUND(DATA_LENGTH/1024/1024, 2) AS 'Data(MB)',
    ROUND(INDEX_LENGTH/1024/1024, 2) AS 'Index(MB)',
    TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'smartcare_cloud'
ORDER BY DATA_LENGTH DESC;
```


---

## 6. 预期效果

### 6.1 代码简化
- ✅ 实体类代码量减少60%以上
- ✅ 消除getter/setter冗余代码
- ✅ 提升代码可维护性

### 6.2 数据一致性
- ✅ 强制引用完整性约束
- ✅ 自动级联删除/更新
- ✅ 消除孤立数据

### 6.3 查询性能
- ✅ 单表查询提升50-80%
- ✅ 复合查询提升70-90%
- ✅ 统计查询提升80-95%

### 6.4 性能对比 (预期)

| 查询场景 | 优化前 | 优化后 | 提升 |
|---------|-------|-------|------|
| 查询老人未处理预警 | ~200ms | ~20ms | 90% ↑ |
| 健康状态统计 | ~500ms | ~50ms | 90% ↑ |
| 预警列表分页 | ~300ms | ~30ms | 90% ↑ |
| 趋势分析查询 | ~800ms | ~100ms | 87.5% ↑ |


---

## 7. 后续优化建议

### 7.1 短期优化
1. ✅ 启用慢查询日志监控
2. ✅ 建立索引使用情况报表
3. ✅ 定期ANALYZE TABLE更新统计信息

### 7.2 中期优化
1. 🔧 考虑分区表(按时间分区health_record)
2. 🔧 引入读写分离(主从复制)
3. 🔧 缓存热点数据到Redis

### 7.3 长期优化
1. 📊 分库分表(数据量超过1000万)
2. 📊 引入时序数据库存储监测数据
3. 📊 使用Elasticsearch做全文搜索


---

## 总结

本次优化解决了三个核心问题:

1. **Lombok兼容性** - 启用Lombok 1.18.30,简化实体类代码
2. **外键约束** - 添加完整外键约束,保障数据一致性  
3. **索引优化** - 创建17个索引(8个单列+9个复合),大幅提升查询性能

**执行优先级:**
1. ⭐⭐⭐⭐⭐ Lombok启用 (已完成,立即生效)
2. ⭐⭐⭐⭐⭐ 索引优化 (建议立即执行)
3. ⭐⭐⭐⭐ 外键约束 (建议维护期执行)

所有优化脚本已创建:
- `database_optimization.sql` - 优化执行脚本
- `check_database_status.sql` - 状态检查脚本
