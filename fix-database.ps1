# 智慧医养平台数据库修复脚本
Write-Host "正在修复智慧医养平台数据库..." -ForegroundColor Green
Write-Host ""

# 检查MySQL是否可用
$mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue
if (-not $mysqlPath) {
    Write-Host "错误: 未找到MySQL命令行工具" -ForegroundColor Red
    Write-Host "请确保MySQL已安装并已添加到PATH环境变量中" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "手动执行步骤:" -ForegroundColor Cyan
    Write-Host "1. 打开MySQL Workbench或命令行"
    Write-Host "2. 使用root用户登录 (密码: 123456)"
    Write-Host "3. 执行fix-database.sql文件中的SQL语句"
    Write-Host ""
    Read-Host "按Enter键退出"
    exit 1
}

Write-Host "正在连接MySQL并执行数据库修复脚本..." -ForegroundColor Yellow

# 执行SQL脚本
try {
    $result = cmd /c "mysql -u root -p123456 < fix-database.sql 2>&1"

    if ($LASTEXITCODE -eq 0) {
        Write-Host ""
        Write-Host "✅ 数据库修复成功！" -ForegroundColor Green
        Write-Host ""
        Write-Host "现在可以访问老人档案管理页面:" -ForegroundColor Cyan
        Write-Host "前端: http://localhost:3000/elderly/list" -ForegroundColor Blue
        Write-Host "后端API: http://localhost:8080/api/elderly/page" -ForegroundColor Blue
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "❌ 数据库修复失败" -ForegroundColor Red
        Write-Host "错误信息: $result" -ForegroundColor Yellow
        Write-Host ""
        Write-Host "手动执行步骤:" -ForegroundColor Cyan
        Write-Host "1. 启动MySQL服务"
        Write-Host "2. 打开MySQL Workbench或命令行"
        Write-Host "3. 使用root用户登录 (密码: 123456)"
        Write-Host "4. 执行fix-database.sql文件中的SQL语句"
        Write-Host ""
    }
} catch {
    Write-Host "执行失败: $_" -ForegroundColor Red
}

Read-Host "按Enter键退出"
