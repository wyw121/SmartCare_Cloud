@echo off
echo 正在修复智慧医养平台数据库...
echo.

REM 检查MySQL是否安装
where mysql >nul 2>nul
if %errorlevel% neq 0 (
    echo 错误: 未找到MySQL命令行工具
    echo 请确保MySQL已安装并已添加到PATH环境变量中
    echo.
    echo 手动执行步骤:
    echo 1. 打开MySQL Workbench或命令行
    echo 2. 使用root用户登录 ^(密码: 123456^)
    echo 3. 执行fix-database.sql文件中的SQL语句
    pause
    exit /b 1
)

echo 正在连接MySQL并执行数据库修复脚本...
mysql -u root -p123456 < fix-database.sql

if %errorlevel% equ 0 (
    echo.
    echo ✅ 数据库修复成功！
    echo.
    echo 现在可以访问老人档案管理页面:
    echo 前端: http://localhost:3000/elderly/list
    echo 后端API: http://localhost:8080/api/elderly/page
    echo.
) else (
    echo.
    echo ❌ 数据库修复失败
    echo 请检查MySQL服务是否运行，用户名密码是否正确
    echo.
    echo 手动执行步骤:
    echo 1. 启动MySQL服务
    echo 2. 打开MySQL Workbench或命令行
    echo 3. 使用root用户登录 ^(密码: 123456^)
    echo 4. 执行fix-database.sql文件中的SQL语句
    echo.
)

pause
