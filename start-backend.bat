@echo off
chcp 65001 >nul
echo 正在设置智慧医养平台数据库...
echo.

echo 请按照以下步骤手动创建数据库：
echo.
echo 1. 打开 MySQL Workbench 或命令行工具
echo 2. 使用 root 用户连接 MySQL（密码：123456）
echo 3. 执行以下 SQL 脚本：
echo    - 方法A：直接复制粘贴 create-database.sql 中的内容
echo    - 方法B：在 MySQL 中执行：source c:/开发/SmartCare_Cloud/create-database.sql
echo.
echo 数据库创建完成后，返回此窗口按任意键继续启动后端...
pause >nul

echo.
echo 正在启动后端服务...
cd backend
mvn spring-boot:run
