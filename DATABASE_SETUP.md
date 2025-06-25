# 数据库安装配置指南

## 前置条件
确保您的系统已安装 MySQL 8.0 或更高版本。

## 数据库创建步骤

### 方法1：使用MySQL命令行（推荐）
1. 打开MySQL命令行客户端：
```bash
mysql -u root -p
```

2. 输入root密码后，执行以下SQL创建数据库：
```sql
CREATE DATABASE IF NOT EXISTS smartcare_cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE smartcare_cloud;
```

3. 导入初始化脚本：
```sql
SOURCE C:/开发/SmartCare_Cloud/backend/src/main/resources/sql/init.sql;
```

### 方法2：使用MySQL Workbench（图形界面）
1. 打开MySQL Workbench
2. 连接到本地MySQL服务器
3. 在Query面板中执行：
```sql
CREATE DATABASE IF NOT EXISTS smartcare_cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
4. 打开 `backend/src/main/resources/sql/init.sql` 文件并执行

### 方法3：使用phpMyAdmin（如果已安装）
1. 访问phpMyAdmin界面
2. 创建新数据库 `smartcare_cloud`
3. 字符集选择 `utf8mb4_unicode_ci`
4. 导入 `init.sql` 文件

## 数据库配置确认
确保 `backend/src/main/resources/application.yml` 中的数据库配置正确：
- 用户名：root
- 密码：123456
- 数据库：smartcare_cloud
- 端口：3306

如果您的MySQL配置不同，请相应修改 `application.yml` 文件。

## 验证安装
数据库创建成功后，可以通过以下命令验证：
```sql
SHOW DATABASES;
USE smartcare_cloud;
SHOW TABLES;
```

应该能看到 `smartcare_cloud` 数据库和相关的表结构。
