# 数据库迁移和测试数据指南

## 📋 目录结构

```
backend/sql/
├── migration/                      # 数据库迁移脚本
│   ├── 001_create_new_tables.sql   # 创建7个新增表
│   ├── 002_alter_existing_tables.sql # 修改现有表
│   ├── 003_create_indexes.sql      # 创建索引
│   ├── master_migration.sql        # 主迁移脚本
│   └── run_migration.ps1           # 迁移执行脚本(PowerShell)
│
└── test-data/                      # 测试数据生成
    ├── generate_basic_data.js      # 基础数据生成脚本(Node.js)
    ├── generate_business_data.js   # 业务数据生成脚本(Node.js)
    ├── generate_and_import.ps1     # 数据生成导入脚本(PowerShell)
    └── README.md                   # 本文件
```

---

## 🚀 快速开始

### 前置要求

1. **MySQL 8.0+** 已安装并运行
2. **Node.js 14+** (用于生成测试数据)
3. **PowerShell** (Windows自带)
4. 数据库 `smartcare_db` 已创建

### 步骤1: 执行数据库迁移

进入迁移脚本目录:

```powershell
cd D:\repositories\SmartCare_Cloud\backend\sql\migration
```

执行迁移脚本:

```powershell
.\run_migration.ps1
```

按提示输入MySQL密码,确认后自动执行3个迁移脚本。

### 步骤2: 生成并导入测试数据

进入测试数据目录:

```powershell
cd D:\repositories\SmartCare_Cloud\backend\sql\test-data
```

执行数据生成脚本:

```powershell
.\generate_and_import.ps1
```

按提示选择是否立即导入数据库。

---

## 📊 迁移脚本说明

### 001_create_new_tables.sql

创建7个新增核心业务表:

| 表名 | 说明 | 记录数(测试) |
|------|------|-------------|
| `doctor_elderly_relation` | 医生-老人关系 | 100 |
| `nurse_info` | 护工信息 | 20 |
| `nurse_elderly_relation` | 护工-老人关系 | 100 |
| `nursing_record` | 护理记录 | 2,000 |
| `medication_record` | 用药记录 | 200 |
| `visit_record` | 巡诊记录 | 150 |
| `operation_log` | 操作审计日志 | 0 (运行时生成) |

### 002_alter_existing_tables.sql

为现有表添加字段并创建2个补充表:

**现有表修改:**
- `elderly` - 新增13个字段(机构ID、责任人、慢性病等)
- `t_doctor` - 新增7个字段(机构ID、排班、服务范围等)
- `health_warning` - 新增5个字段(分配医生、处理结果等)
- `sys_user` - 新增2个字段(机构ID、头像)

**新增表:**
- `organization_info` - 机构信息表
- `social_worker_info` - 社工信息表

### 003_create_indexes.sql

创建性能优化索引:

- 复合索引: 提升多条件查询性能
- 外键约束: (可选)默认未启用,如需启用请取消注释

---

## 🎲 测试数据说明

### 基础数据 (generate_basic_data.js)

| 数据类型 | 数量 | 说明 |
|---------|------|------|
| 机构 | 4 | 1个医养中心+1个医院+2个养老院 |
| 系统管理员 | 3 | admin, admin2, admin3 |
| 业务管理员 | 3 | business_admin1-3 |
| 医生 | 10 | doctor1-10 |
| 护工 | 20 | nurse1-20 |
| 社工 | 5 | social_worker1-5 |
| 家属 | 50 | family1-50 |
| 老人 | 100 | 随机生成姓名、年龄等 |
| 医生-老人关联 | 100 | 每个医生负责8-12位老人 |
| 护工-老人关联 | 100 | 每个护工照护3-6位老人 |
| 家属-老人关联 | 50 | 每个家属关联1位老人 |

**测试账号:**
- 用户名: admin / doctor1 / nurse1 / family1
- 密码: 123456 (所有账号统一)

### 业务数据 (generate_business_data.js)

| 数据类型 | 数量 | 时间范围 |
|---------|------|----------|
| 健康记录 | 3,000 | 最近3个月 |
| 健康预警 | 50 | 最近3个月 |
| 护理记录 | 2,000 | 最近3个月 |
| 用药记录 | 200 | 当前在用 |
| 巡诊记录 | 150 | 最近3个月 |

**数据特点:**
- 健康记录: 包含血压、血糖、心率等体征数据
- 健康预警: 覆盖不同预警级别和处理状态
- 护理记录: 涵盖晨间护理、喂食、翻身等8种护理类型
- 用药记录: 包含常见慢性病用药
- 巡诊记录: 医生日常巡诊工作记录

---

## 🔧 手动执行SQL脚本

如果不使用PowerShell脚本,可以手动执行:

### 方式1: MySQL命令行

```bash
# 执行迁移
mysql -uroot -p smartcare_db < 001_create_new_tables.sql
mysql -uroot -p smartcare_db < 002_alter_existing_tables.sql
mysql -uroot -p smartcare_db < 003_create_indexes.sql

# 导入测试数据
node generate_basic_data.js > basic_data.sql
node generate_business_data.js > business_data.sql
mysql -uroot -p smartcare_db < basic_data.sql
mysql -uroot -p smartcare_db < business_data.sql
```

### 方式2: MySQL Workbench

1. 打开MySQL Workbench
2. 连接到数据库
3. File -> Open SQL Script
4. 选择SQL文件并执行

---

## 🔍 验证数据完整性

执行以下SQL查询验证数据:

```sql
-- 查看所有表
SHOW TABLES;

-- 查看各表记录数
SELECT 'organization_info' as table_name, COUNT(*) as count FROM organization_info
UNION ALL
SELECT 'sys_user', COUNT(*) FROM sys_user
UNION ALL
SELECT 't_doctor', COUNT(*) FROM t_doctor
UNION ALL
SELECT 'nurse_info', COUNT(*) FROM nurse_info
UNION ALL
SELECT 'elderly', COUNT(*) FROM elderly
UNION ALL
SELECT 'doctor_elderly_relation', COUNT(*) FROM doctor_elderly_relation
UNION ALL
SELECT 'nurse_elderly_relation', COUNT(*) FROM nurse_elderly_relation
UNION ALL
SELECT 'family_elderly_relation', COUNT(*) FROM family_elderly_relation
UNION ALL
SELECT 'health_record', COUNT(*) FROM health_record
UNION ALL
SELECT 'health_warning', COUNT(*) FROM health_warning
UNION ALL
SELECT 'nursing_record', COUNT(*) FROM nursing_record
UNION ALL
SELECT 'medication_record', COUNT(*) FROM medication_record
UNION ALL
SELECT 'visit_record', COUNT(*) FROM visit_record;

-- 查看医生负责的老人数
SELECT 
  d.name AS doctor_name,
  COUNT(der.elderly_id) AS patient_count
FROM t_doctor d
LEFT JOIN doctor_elderly_relation der ON d.id = der.doctor_id AND der.status = 1
GROUP BY d.id, d.name
ORDER BY patient_count DESC;

-- 查看护工照护的老人数
SELECT 
  n.name AS nurse_name,
  COUNT(ner.elderly_id) AS care_count
FROM nurse_info n
LEFT JOIN nurse_elderly_relation ner ON n.id = ner.nurse_id AND ner.status = 1
GROUP BY n.id, n.name
ORDER BY care_count DESC;

-- 查看家属关联情况
SELECT 
  u.real_name AS family_name,
  e.name AS elderly_name,
  fer.relationship,
  fer.access_level
FROM family_elderly_relation fer
JOIN sys_user u ON fer.family_id = u.id
JOIN elderly e ON fer.elderly_id = e.id
WHERE fer.status = 1
LIMIT 10;
```

---

## ⚠️ 注意事项

### 1. 备份数据库

在生产环境执行迁移前,**务必备份数据库**:

```bash
mysqldump -uroot -p smartcare_db > backup_$(date +%Y%m%d_%H%M%S).sql
```

### 2. 外键约束

默认未启用外键约束。如需启用:

1. 打开 `003_create_indexes.sql`
2. 找到外键约束部分(被注释)
3. 取消注释并执行

### 3. 数据一致性

- 医生ID: 1-10
- 护工ID: 1-20
- 老人ID: 1-100
- 确保关联关系中的ID存在

### 4. 性能优化

大量数据导入后,建议执行:

```sql
ANALYZE TABLE elderly, health_record, nursing_record, medication_record;
OPTIMIZE TABLE elderly, health_record, nursing_record, medication_record;
```

---

## 🔄 回滚迁移

如需回滚到迁移前状态:

### 方式1: 从备份恢复

```bash
mysql -uroot -p smartcare_db < backup_20251028_120000.sql
```

### 方式2: 手动删除

```sql
-- 删除新增的表
DROP TABLE IF EXISTS operation_log;
DROP TABLE IF EXISTS visit_record;
DROP TABLE IF EXISTS medication_record;
DROP TABLE IF EXISTS nursing_record;
DROP TABLE IF EXISTS nurse_elderly_relation;
DROP TABLE IF EXISTS nurse_info;
DROP TABLE IF EXISTS doctor_elderly_relation;
DROP TABLE IF EXISTS social_worker_info;
DROP TABLE IF EXISTS organization_info;

-- 删除新增的字段(示例)
ALTER TABLE elderly 
DROP COLUMN organization_id,
DROP COLUMN admission_date,
-- ... 其他字段
```

---

## 📞 问题反馈

遇到问题请检查:

1. ✅ MySQL服务是否运行
2. ✅ 数据库名称是否正确
3. ✅ 用户权限是否足够
4. ✅ Node.js版本是否符合要求
5. ✅ SQL文件编码是否为UTF-8

---

**最后更新:** 2025-10-28  
**版本:** V1.0  
**作者:** SmartCare开发团队
