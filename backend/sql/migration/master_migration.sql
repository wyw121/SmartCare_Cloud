-- =====================================================
-- 智慧医养平台 - 完整数据库迁移脚本
-- 版本: V1.0
-- 创建日期: 2025-10-28
-- 说明: 执行此脚本完成数据库架构升级
-- =====================================================

-- =====================================================
-- 执行前检查
-- =====================================================
SELECT '开始执行数据库迁移...' AS Message;
SELECT CONCAT('当前数据库: ', DATABASE()) AS CurrentDatabase;
SELECT CONCAT('MySQL版本: ', VERSION()) AS MySQLVersion;
SELECT NOW() AS StartTime;

-- =====================================================
-- 步骤1: 创建新增的数据表
-- =====================================================
SOURCE D:/repositories/SmartCare_Cloud/backend/sql/migration/001_create_new_tables.sql;

-- =====================================================
-- 步骤2: 为现有表添加字段
-- =====================================================
SOURCE D:/repositories/SmartCare_Cloud/backend/sql/migration/002_alter_existing_tables.sql;

-- =====================================================
-- 步骤3: 创建索引和约束
-- =====================================================
SOURCE D:/repositories/SmartCare_Cloud/backend/sql/migration/003_create_indexes.sql;

-- =====================================================
-- 迁移完成检查
-- =====================================================
SELECT '数据库迁移完成!' AS Message;
SELECT NOW() AS EndTime;

-- 查看所有表
SELECT '查看所有数据表:' AS Info;
SHOW TABLES;

-- 查看新增的表结构(示例)
SELECT '查看doctor_elderly_relation表结构:' AS Info;
DESC doctor_elderly_relation;

SELECT '查看nurse_info表结构:' AS Info;
DESC nurse_info;

SELECT '查看nursing_record表结构:' AS Info;
DESC nursing_record;

SELECT '查看medication_record表结构:' AS Info;
DESC medication_record;

-- =====================================================
-- 执行后建议
-- =====================================================
SELECT '
✅ 数据库迁移脚本执行完成!

📋 已完成的工作:
  1. ✓ 创建7个新增核心业务表
  2. ✓ 创建2个补充表(机构信息、社工信息)
  3. ✓ 为现有表添加缺失字段
  4. ✓ 创建性能优化索引

🔄 下一步操作:
  1. 执行数据生成脚本,导入测试数据
  2. 开发对应的实体类和Mapper
  3. 实现权限校验中间件
  4. 测试数据完整性和关联关系

⚠️ 注意事项:
  - 外键约束默认未启用,如需启用请查看003脚本
  - 建议在开发环境测试无误后再迁移生产环境
  - 迁移生产环境前务必备份数据库!

📚 相关文档:
  - 数据字典: docs/数据字典_完整版.md
  - 架构分析: docs/analysis/三级角色权限架构深度分析与优化方案.md
' AS NextSteps;
