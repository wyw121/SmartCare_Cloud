# 数据库与文档一致性修正方案

## 📊 问题分析

通过详细对比,发现以下主要问题:

### 1. sys_user 表字段差异

| 文档定义字段 | 实际字段 | 状态 |
|-------------|---------|------|
| gender, phone, email | ❌ 缺失 | 需添加 |
| role_name | ❌ 缺失 | 需添加 |
| doctor_title, doctor_speciality | ❌ 缺失 | 需添加 |
| family_relationship | ❌ 缺失 | 需添加 |
| create_by, update_by | ❌ 缺失 | 需添加 |
| create_time, update_time | created_time, updated_time | 字段名不一致 |

### 2. t_health_warning 表字段差异

| 文档定义字段 | 实际字段 | 状态 |
|-------------|---------|------|
| warning_time | trigger_time | 字段名不一致 |
| indicator_name, indicator_value, normal_range | ❌ 缺失 | 已被trigger_data替代 |
| warning_message | content | 字段名不一致 |
| handle_method | ❌ 缺失 | 需添加或用handle_result替代 |

### 3. 表名不一致

| 文档 | 实际 | 建议 |
|------|------|------|
| operation_log | audit_log | 统一为audit_log |
| health_warning | t_health_warning | 统一为t_health_warning |

---

## 🎯 修正策略

### 策略A: 修改数据库以符合文档 (不推荐)
- **优点**: 文档无需改动
- **缺点**: 
  - 需要修改大量数据库字段
  - 影响已有代码
  - 可能导致数据丢失
  - 工作量大,风险高

### 策略B: 修改文档以符合数据库 (✅ 推荐)
- **优点**:
  - 不影响数据库和代码
  - 风险最小
  - 工作量较小
  - 保持系统稳定性
- **缺点**: 需要更新文档

---

## ✅ 采用策略B: 更新文档

### 修正内容

#### 1. sys_user 表 - 简化版本(符合实际)

**实际字段列表:**
- id, username, password, real_name
- role_code, organization_id, avatar, status
- created_time, updated_time

**文档需要移除的字段:**
- gender, phone, email, role_name
- doctor_title, doctor_speciality, family_relationship
- create_by, update_by, is_deleted

**字段名修正:**
- create_time → created_time
- update_time → updated_time

#### 2. t_health_warning 表 - 更新字段名

**字段名修正:**
- warning_time → trigger_time
- warning_message → content + title

**新增实际存在的字段:**
- elderly_name, title, trigger_data, data_source
- handle_remark, is_notified_family, is_notified_doctor

#### 3. 表名统一

- `operation_log` (文档) → `audit_log` (实际)
- `health_warning` (文档) → `t_health_warning` (实际)

#### 4. 新增缺失表的说明

需要补充以下表的完整文档:
- `family_user` - 家属用户信息表
- `elderly_care_assessment` - 老人照护评估表

---

## 📝 修正后的数据字典章节

### 1.1 系统用户表 (`sys_user`) - 修正版

**表说明:** 存储所有系统用户的基础信息

| 字段名 | 类型 | 长度 | 必填 | 索引 | 默认值 | 说明 |
|--------|------|------|------|------|--------|------|
| id | BIGINT | - | ✓ | PRIMARY | AUTO | 用户ID |
| username | VARCHAR | 50 | ✓ | UNIQUE | - | 登录用户名 |
| password | VARCHAR | 255 | ✓ | - | - | 密码(BCrypt加密) |
| real_name | VARCHAR | 50 | ✓ | INDEX | - | 真实姓名 |
| role_code | VARCHAR | 50 | ✓ | INDEX | admin | 角色编码 |
| organization_id | BIGINT | - | - | INDEX | - | 所属机构ID |
| avatar | VARCHAR | 200 | - | - | - | 头像URL |
| status | TINYINT | - | ✓ | INDEX | 1 | 状态: 1-启用, 0-禁用 |
| created_time | DATETIME | - | ✓ | - | NOW() | 创建时间 |
| updated_time | DATETIME | - | ✓ | - | NOW() | 更新时间 |

**说明:**
- 详细的用户信息(电话、邮箱、性别等)存储在对应的专业人员表中:
  - 医生信息 → `t_doctor` 表
  - 护工信息 → `nurse_info` 表
  - 社工信息 → `social_worker_info` 表
  - 家属信息 → `family_user` 表

---

### 4.2 健康预警表 (`t_health_warning`) - 修正版

**表说明:** 健康异常预警记录

| 字段名 | 类型 | 长度 | 必填 | 索引 | 默认值 | 说明 |
|--------|------|------|------|------|--------|------|
| id | BIGINT | - | ✓ | PRIMARY | AUTO | 预警ID |
| elderly_id | BIGINT | - | ✓ | INDEX | - | 老人ID |
| elderly_name | VARCHAR | 50 | ✓ | - | - | 老人姓名 |
| warning_type | VARCHAR | 30 | ✓ | INDEX | - | 预警类型 |
| warning_level | INT | - | ✓ | INDEX | - | 预警级别: 1-低, 2-中, 3-高, 4-紧急 |
| title | VARCHAR | 200 | ✓ | - | - | 预警标题 |
| content | TEXT | ✓ | - | - | 预警内容描述 |
| trigger_data | VARCHAR | 255 | - | - | - | 触发数据(JSON格式) |
| data_source | VARCHAR | 50 | - | - | - | 数据来源 |
| trigger_time | DATETIME | - | ✓ | INDEX | - | 触发时间 |
| status | INT | - | ✓ | INDEX | 0 | 状态: 0-待处理, 1-已分配, 2-处理中, 3-已完成 |
| assigned_doctor_id | BIGINT | - | - | INDEX | - | 分配医生ID |
| assigned_time | DATETIME | - | - | - | - | 分配时间 |
| handle_deadline | DATETIME | - | - | INDEX | - | 处理时限 |
| handler_id | BIGINT | - | - | - | - | 处理人ID |
| handler_name | VARCHAR | 50 | - | - | - | 处理人姓名 |
| handle_time | DATETIME | - | - | - | - | 处理时间 |
| handle_result | TEXT | - | - | - | - | 处理结果 |
| handle_evaluation | VARCHAR | 500 | - | - | - | 处理评价 |
| handle_remark | TEXT | - | - | - | - | 处理备注 |
| is_notified_family | TINYINT | - | ✓ | - | 0 | 是否通知家属: 0-否, 1-是 |
| is_notified_doctor | TINYINT | - | ✓ | - | 0 | 是否通知医生: 0-否, 1-是 |
| remark | VARCHAR | 500 | - | - | - | 备注 |
| created_time | DATETIME | - | ✓ | - | NOW() | 创建时间 |
| updated_time | DATETIME | - | ✓ | - | NOW() | 更新时间 |
| create_by | VARCHAR | 50 | - | - | - | 创建者 |
| update_by | VARCHAR | 50 | - | - | - | 更新者 |
| is_deleted | TINYINT | - | ✓ | - | 0 | 删除标记 |

---

### 1.5 家属用户表 (`family_user`) - 新增

**表说明:** 存储家属用户的详细信息

| 字段名 | 类型 | 长度 | 必填 | 索引 | 默认值 | 说明 |
|--------|------|------|------|------|--------|------|
| id | BIGINT | - | ✓ | PRIMARY | AUTO | 家属ID |
| user_id | BIGINT | - | ✓ | UNIQUE | - | 关联系统用户ID |
| name | VARCHAR | 50 | ✓ | INDEX | - | 姓名 |
| gender | TINYINT | - | - | - | - | 性别: 1-男, 0-女 |
| phone | VARCHAR | 20 | - | - | - | 联系电话 |
| id_card | VARCHAR | 18 | - | - | - | 身份证号 |
| address | VARCHAR | 200 | - | - | - | 住址 |
| status | TINYINT | - | ✓ | INDEX | 1 | 状态: 0-停用, 1-启用 |
| created_time | DATETIME | - | ✓ | - | NOW() | 创建时间 |
| updated_time | DATETIME | - | ✓ | - | NOW() | 更新时间 |
| is_deleted | TINYINT | - | ✓ | - | 0 | 删除标记 |

---

### 4.4 老人照护评估表 (`elderly_care_assessment`) - 新增

**表说明:** 老人照护能力评估记录

| 字段名 | 类型 | 长度 | 必填 | 索引 | 默认值 | 说明 |
|--------|------|------|------|------|--------|------|
| id | BIGINT | - | ✓ | PRIMARY | AUTO | 评估ID |
| elderly_id | BIGINT | - | ✓ | INDEX | - | 老人ID |
| assessment_date | DATE | - | ✓ | INDEX | - | 评估日期 |
| assessor_id | BIGINT | - | ✓ | INDEX | - | 评估人ID |
| assessor_name | VARCHAR | 50 | - | - | - | 评估人姓名 |
| care_level | TINYINT | - | - | - | - | 照护等级: 1-一级, 2-二级, 3-三级 |
| total_score | INT | - | - | - | - | 总分 |
| assessment_detail | JSON | - | - | - | - | 评估详情 |
| conclusion | TEXT | - | - | - | - | 评估结论 |
| suggestions | TEXT | - | - | - | - | 建议 |
| next_assessment_date | DATE | - | - | - | - | 下次评估日期 |
| created_time | DATETIME | - | ✓ | - | NOW() | 创建时间 |
| updated_time | DATETIME | - | ✓ | - | NOW() | 更新时间 |
| is_deleted | TINYINT | - | ✓ | - | 0 | 删除标记 |

---

### 7.1 审计日志表 (`audit_log`) - 修正表名

**表说明:** 系统操作审计跟踪

*(内容与原 operation_log 相同,仅表名改为 audit_log)*

---

## 🔧 待补充的表

以下表需要检查实际结构并补充到文档:

1. ✅ `sys_role` - 已创建并有完整定义
2. ✅ `sys_permission` - 已创建并有完整定义
3. ✅ `sys_role_permission` - 已创建并有完整定义
4. ✅ `family_user` - 已补充定义
5. ✅ `elderly_care_assessment` - 已补充定义
6. ✅ `audit_log` - 已修正表名
7. ✅ `t_health_warning` - 已修正字段

---

## 📋 执行清单

- [x] 创建RBAC权限表 (sys_role, sys_permission, sys_role_permission)
- [ ] 更新文档 - sys_user 表定义
- [ ] 更新文档 - t_health_warning 表定义
- [ ] 更新文档 - 添加 family_user 表
- [ ] 更新文档 - 添加 elderly_care_assessment 表
- [ ] 更新文档 - operation_log 改为 audit_log
- [ ] 更新文档 - health_warning 改为 t_health_warning
- [ ] 检查其他表的字段并更新文档

---

**建议:** 使用策略B,更新文档以符合实际数据库结构,确保文档的真实性和可靠性。
