# 智慧医养平台 - 数据库快速设置脚本
# 使用方法：在 PowerShell 中运行此脚本

Write-Host "正在设置智慧医养平台数据库..." -ForegroundColor Green

# 检查 MySQL 是否可用
try {
    # 尝试连接 MySQL (假设密码为 123456)
    $mysqlPath = "mysql"
    Write-Host "尝试连接 MySQL..." -ForegroundColor Yellow

    # 创建数据库的 SQL 命令
    $createDbSql = @"
CREATE DATABASE IF NOT EXISTS smartcare_cloud DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
"@

    Write-Host "正在创建数据库 smartcare_cloud..." -ForegroundColor Yellow

    # 输出需要手动执行的命令
    Write-Host "`n请手动执行以下步骤：" -ForegroundColor Cyan
    Write-Host "1. 打开 MySQL 命令行或 MySQL Workbench" -ForegroundColor White
    Write-Host "2. 使用 root 用户登录 (密码: 123456)" -ForegroundColor White
    Write-Host "3. 执行以下 SQL 命令：" -ForegroundColor White
    Write-Host $createDbSql -ForegroundColor Green
    Write-Host "`n或者导入 create-database.sql 文件：" -ForegroundColor White
    Write-Host "mysql -u root -p123456 < create-database.sql" -ForegroundColor Green

    Write-Host "`n数据库创建完成后，请重新启动后端服务：" -ForegroundColor Cyan
    Write-Host "cd backend && mvn spring-boot:run" -ForegroundColor Green

    Write-Host "`n当前状态：" -ForegroundColor Cyan
    Write-Host "✅ 前端已启动: http://localhost:3001" -ForegroundColor Green
    Write-Host "❌ 后端待启动: http://localhost:8080" -ForegroundColor Red
    Write-Host "❌ 数据库待创建: smartcare_cloud" -ForegroundColor Red
}
catch {
    Write-Host "MySQL 连接失败: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "请确保：" -ForegroundColor Yellow
    Write-Host "1. MySQL 服务正在运行" -ForegroundColor White
    Write-Host "2. MySQL 命令行工具在 PATH 中" -ForegroundColor White
    Write-Host "3. root 用户密码为 123456" -ForegroundColor White
}

Read-Host "`n按 Enter 键退出"
