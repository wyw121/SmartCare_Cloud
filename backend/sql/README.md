# SQL脚本说明

本目录包含项目的数据库初始化和维护SQL脚本。

## 📋 脚本列表

### 核心表结构
- `create_user_table.sql` - 用户表创建脚本
- `create_role_system.sql` - 角色权限系统表创建脚本
- `create_family_tables_final.sql` - 家属关联表创建脚本（最终版）
- `create_family_permission_tables.sql` - 家属权限表创建脚本

### 优化与维护
- `database_optimization.sql` - 数据库优化SQL（索引、性能优化）
- `check_database_status.sql` - 数据库状态检查脚本

## 🚀 执行顺序

建议按以下顺序执行SQL脚本：

1. **基础表创建**
   ```sql
   source create_user_table.sql;
   source create_role_system.sql;
   ```

2. **业务表创建**
   ```sql
   source create_family_tables_final.sql;
   source create_family_permission_tables.sql;
   ```

3. **性能优化**
   ```sql
   source database_optimization.sql;
   ```

4. **状态检查**
   ```sql
   source check_database_status.sql;
   ```

## 📝 注意事项

- 执行前请备份现有数据库
- 确认MySQL版本 >= 8.0
- 检查字符集配置：UTF-8
- 生产环境执行前请先在测试环境验证

## 🔧 数据库配置

推荐配置（application.yml）：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/smartcare_cloud?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8
    username: root
    password: your_password
```
