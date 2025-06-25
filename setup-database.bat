@echo off
echo 正在启动智慧医养平台...
echo.

echo [1/4] 检查数据库连接...
mysql --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误：未找到MySQL命令行工具
    echo 请确保MySQL已安装并添加到PATH环境变量中
    pause
    exit /b 1
)

echo [2/4] 创建数据库...
echo CREATE DATABASE IF NOT EXISTS smartcare_cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci; | mysql -u root -p123456
if %errorlevel% neq 0 (
    echo 错误：数据库创建失败，请检查MySQL连接
    echo 请确认：
    echo - MySQL服务正在运行
    echo - root用户密码为123456
    echo - 或手动修改 backend/src/main/resources/application.yml 中的数据库配置
    pause
    exit /b 1
)

echo [3/4] 导入数据库表结构...
mysql -u root -p123456 smartcare_cloud < backend/src/main/resources/sql/init.sql
if %errorlevel% neq 0 (
    echo 警告：表结构导入失败，但数据库已创建
    echo 这不会影响应用启动，表结构会在应用启动时自动创建
)

echo [4/4] 启动应用...
echo.
echo 数据库设置完成！
echo.
echo 现在可以启动应用：
echo 前端: http://localhost:3001 (已启动)
echo 后端: cd backend && mvn spring-boot:run
echo.
pause
