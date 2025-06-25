# 数据库设计和SQL生成

## 目标
为智慧医养平台生成标准的数据库表结构和SQL脚本

## 必需信息
如果未提供，请询问：
- 表名和功能描述
- 主要字段及类型
- 关联关系

## 数据库设计规范

### 命名规范
- 表名：使用下划线命名法（snake_case）
- 字段名：使用下划线命名法
- 索引名：`idx_表名_字段名`
- 外键名：`fk_表名_关联表名`

### 标准字段
每个业务表必须包含：
```sql
id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
created_by BIGINT COMMENT '创建人ID',
updated_by BIGINT COMMENT '更新人ID',
is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标志'
```

### 数据类型规范
- 主键：`BIGINT AUTO_INCREMENT`
- 字符串：`VARCHAR(长度)` 或 `TEXT`
- 数字：`BIGINT`, `INT`, `DECIMAL(10,2)`
- 日期：`DATETIME`, `DATE`, `TIME`
- 布尔值：`TINYINT(1)`
- 枚举：`ENUM('值1', '值2')`

### 索引设计
- 主键自动创建聚簇索引
- 外键字段创建普通索引
- 查询频繁的字段创建索引
- 复合查询创建联合索引

### 字符集和引擎
```sql
ENGINE = InnoDB 
DEFAULT CHARSET = utf8mb4 
COLLATE = utf8mb4_unicode_ci
```

### 注释要求
- 表注释：说明表的业务用途
- 字段注释：说明字段含义和用途
- 索引注释：说明索引的查询场景

## 生成要求
1. 完整的CREATE TABLE语句
2. 适当的索引创建
3. 外键约束（如有关联）
4. 初始化数据INSERT语句
5. 表结构说明文档

## 隐私保护
对于老人个人信息相关表：
- 敏感字段考虑加密存储
- 添加数据脱敏字段
- 设置适当的访问权限
