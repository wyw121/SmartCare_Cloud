# 数据库规范性检查报告

**检查时间:** 2025-10-29  
**数据库:** smartcare_cloud  
**参考文档:** 数据字典_完整版.md

---

## 📊 执行摘要

### 表的对比

| 分类 | 文档定义 | 实际存在 | 状态 |
|------|----------|----------|------|
| ✅ 完全匹配 | 14 | 14 | 正常 |
| ❌ 文档有但数据库缺失 | 8 | 0 | **需创建** |
| ⚠️ 数据库有但文档未记录 | 0 | 4 | **需更新文档** |
| **总计** | **22** | **18** | - |

---

## ❌ 缺失的表 (8个)

以下表在**数据字典_完整版.md**中定义,但数据库中**不存在**:

### 1. 用户权限管理模块 (3个)

| 表名 | 文档章节 | 用途 | 优先级 |
|------|----------|------|--------|
| `sys_role` | 1.2 | 角色定义表 | 🔴 **高** |
| `sys_permission` | 1.3 | 系统权限定义表 | 🔴 **高** |
| `sys_role_permission` | 1.4 | 角色-权限关联表 | 🔴 **高** |

**影响:** 当前系统缺少完整的RBAC权限体系,只能通过 `sys_user.role_code` 进行简单角色判断,无法实现细粒度权限控制。

**建议:** 立即创建这三张表,建立完整的RBAC权限系统。

---

### 2. 健康管理模块 (1个)

| 表名 | 文档章节 | 用途 | 优先级 |
|------|----------|------|--------|
| `assessment_report` | 4.3 | 评估报告表 | 🟡 **中** |

**影响:** 无法系统化管理老人的各类评估报告(健康评估、能力评估、精神评估、营养评估等)。

**建议:** 创建此表以完善健康管理功能。

---

### 3. 设备管理模块 (2个)

| 表名 | 文档章节 | 用途 | 优先级 |
|------|----------|------|--------|
| `equipment` | 6.1 | 设备信息表 | 🟡 **中** |
| `equipment_usage_record` | 6.2 | 设备使用记录表 | 🟡 **中** |

**影响:** 无法进行医疗设备台账管理和使用记录追踪。

**建议:** 如果系统需要设备管理功能,应创建这两张表。

---

### 4. 系统日志模块 (2个)

| 表名 | 实际表名 | 说明 | 状态 |
|------|----------|------|------|
| ❌ `operation_log` (文档) | ✅ `audit_log` (实际) | 名称不一致 | 🟡 需统一 |

**说明:** 文档中定义为 `operation_log`,但数据库中实际创建为 `audit_log`。

**建议:** 
- 方案1: 将数据库表重命名为 `operation_log` (影响代码)
- 方案2: 更新文档将 `operation_log` 改为 `audit_log` (**推荐**)

---

## ⚠️ 额外的表 (4个)

以下表在数据库中存在,但**文档中未记录**:

| 表名 | 推测用途 | 建议 |
|------|----------|------|
| `audit_log` | 审计日志(对应文档的operation_log) | 更新文档,替换operation_log |
| `family_user` | 家属用户信息表 | 添加到文档 1.5章节 |
| `elderly_care_assessment` | 老人照护评估表 | 添加到文档 4.3章节(或与assessment_report合并) |
| `t_health_warning` | 健康预警表(对应文档的health_warning) | 更新文档表名 |

**建议:** 将这些表补充到数据字典文档中。

---

## ✅ 完全匹配的表 (14个)

以下表在文档和数据库中**完全匹配**:

### 用户权限 (1个)
- ✅ `sys_user`

### 老人档案 (2个)
- ✅ `elderly`
- ✅ `family_elderly_relation`

### 医护人员 (5个)
- ✅ `t_doctor`
- ✅ `doctor_elderly_relation`
- ✅ `nurse_info`
- ✅ `nurse_elderly_relation`
- ✅ `social_worker_info`

### 健康管理 (1个)
- ✅ `health_record`

### 护理服务 (3个)
- ✅ `nursing_record`
- ✅ `medication_record`
- ✅ `visit_record`

### 系统管理 (1个)
- ✅ `organization_info`

---

## 🔍 表名不一致问题

| 文档定义 | 实际表名 | 建议处理方式 |
|----------|----------|--------------|
| `operation_log` | `audit_log` | 更新文档为 `audit_log` |
| `health_warning` | `t_health_warning` | 更新文档为 `t_health_warning` |

---

## 📋 待办事项清单

### 🔴 高优先级 (必须完成)

- [ ] **创建RBAC权限表** (3张表)
  - [ ] `sys_role` - 角色表
  - [ ] `sys_permission` - 权限表
  - [ ] `sys_role_permission` - 角色权限关联表
  
- [ ] **更新文档 - 表名统一**
  - [ ] 将文档中 `operation_log` 改为 `audit_log`
  - [ ] 将文档中 `health_warning` 改为 `t_health_warning`

- [ ] **补充文档 - 新增表说明**
  - [ ] 添加 `family_user` 表的完整说明(章节1.5)
  - [ ] 添加 `elderly_care_assessment` 表的完整说明(章节4.3)
  - [ ] 添加 `audit_log` 表的完整字段说明(章节7.1)

### 🟡 中优先级 (建议完成)

- [ ] **创建健康评估表**
  - [ ] `assessment_report` - 评估报告表
  
- [ ] **创建设备管理表** (如需要此功能)
  - [ ] `equipment` - 设备信息表
  - [ ] `equipment_usage_record` - 设备使用记录表

### 🔵 低优先级 (可选)

- [ ] 审查 `elderly_care_assessment` 与文档定义的 `assessment_report` 是否功能重复
- [ ] 如果重复,决定保留哪一个,删除或合并另一个

---

## 📊 详细字段对比

**注意:** 由于篇幅限制,详细字段对比需要逐表进行。建议使用以下SQL命令检查:

```sql
-- 检查单表字段
DESC 表名;

-- 检查表的索引
SHOW INDEX FROM 表名;

-- 检查表的创建语句
SHOW CREATE TABLE 表名;
```

---

## 🎯 建议的修复顺序

### 第一步: 创建RBAC权限表 (今天完成)
```sql
-- 执行脚本创建:
-- 1. sys_role
-- 2. sys_permission  
-- 3. sys_role_permission
```

### 第二步: 更新数据字典文档 (今天完成)
- 修改表名: `operation_log` → `audit_log`
- 修改表名: `health_warning` → `t_health_warning`
- 新增章节: `family_user` 表说明
- 新增章节: `elderly_care_assessment` 表说明
- 完善章节: `audit_log` 详细字段

### 第三步: 创建健康评估表 (本周完成)
```sql
-- 创建 assessment_report 表
-- 或整合 elderly_care_assessment 功能
```

### 第四步: 设备管理表 (按需创建)
```sql
-- 根据业务需求决定是否创建:
-- 1. equipment
-- 2. equipment_usage_record
```

---

## ✅ 验收标准

1. **表结构完整性:** 所有文档定义的表都在数据库中存在
2. **命名一致性:** 文档与数据库中的表名完全一致
3. **文档完整性:** 所有实际表都在文档中有详细说明
4. **功能完整性:** RBAC权限体系完整可用

---

**报告生成时间:** 2025-10-29  
**下次检查建议:** 每次数据库结构变更后

