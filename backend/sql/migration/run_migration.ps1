# =====================================================
# 智慧医养平台 - 数据库迁移执行脚本
# 版本: V1.0
# 创建日期: 2025-10-28
# 说明: PowerShell脚本,用于执行数据库迁移
# =====================================================

# 设置编码
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "智慧医养平台 - 数据库迁移工具 V1.0" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# 配置参数
$DB_HOST = "localhost"
$DB_PORT = "3306"
$DB_NAME = "smartcare_cloud"
$DB_USER = "root"
$SCRIPT_PATH = "$PSScriptRoot"

Write-Host "📋 迁移配置:" -ForegroundColor Yellow
Write-Host "  数据库地址: ${DB_HOST}:${DB_PORT}" -ForegroundColor Gray
Write-Host "  数据库名称: $DB_NAME" -ForegroundColor Gray
Write-Host "  脚本目录: $SCRIPT_PATH" -ForegroundColor Gray
Write-Host ""

# 提示输入密码
$DB_PASSWORD = Read-Host "请输入MySQL root密码" -AsSecureString
$BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($DB_PASSWORD)
$DB_PASSWORD_PLAIN = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)

Write-Host ""
Write-Host "⚠️  警告: 此操作将修改数据库结构!" -ForegroundColor Yellow
Write-Host "  建议在执行前备份数据库" -ForegroundColor Yellow
$confirm = Read-Host "确认继续? (yes/no)"

if ($confirm -ne "yes") {
    Write-Host "❌ 已取消迁移操作" -ForegroundColor Red
    exit
}

Write-Host ""
Write-Host "🚀 开始执行数据库迁移..." -ForegroundColor Green
Write-Host ""

# 执行迁移脚本
$scripts = @(
    "001_create_new_tables.sql",
    "002_alter_existing_tables.sql",
    "003_create_indexes.sql"
)

$success = $true
$totalScripts = $scripts.Count
$currentScript = 0

foreach ($script in $scripts) {
    $currentScript++
    $scriptFile = Join-Path $SCRIPT_PATH $script
    
    Write-Host "[$currentScript/$totalScripts] 执行: $script" -ForegroundColor Cyan
    
    if (Test-Path $scriptFile) {
        try {
            # 使用mysql命令执行SQL脚本
            Get-Content $scriptFile | & mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p"$DB_PASSWORD_PLAIN" $DB_NAME 2>&1
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host "  ✓ 执行成功" -ForegroundColor Green
            } else {
                Write-Host "  ✗ 执行失败" -ForegroundColor Red
                $success = $false
            }
        } catch {
            Write-Host "  ✗ 执行出错: $($_.Exception.Message)" -ForegroundColor Red
            $success = $false
        }
    } else {
        Write-Host "  ✗ 文件不存在: $scriptFile" -ForegroundColor Red
        $success = $false
    }
    
    Write-Host ""
}

# 输出结果
Write-Host "================================================" -ForegroundColor Cyan
if ($success) {
    Write-Host "✅ 数据库迁移完成!" -ForegroundColor Green
    Write-Host ""
    Write-Host "📋 已完成的工作:" -ForegroundColor Yellow
    Write-Host "  1. ✓ 创建7个新增核心业务表" -ForegroundColor Gray
    Write-Host "  2. ✓ 创建2个补充表(机构信息、社工信息)" -ForegroundColor Gray
    Write-Host "  3. ✓ 为现有表添加缺失字段" -ForegroundColor Gray
    Write-Host "  4. ✓ 创建性能优化索引" -ForegroundColor Gray
    Write-Host ""
    Write-Host "🔄 下一步操作:" -ForegroundColor Yellow
    Write-Host "  1. 执行数据生成脚本: .\generate_test_data.ps1" -ForegroundColor Gray
    Write-Host "  2. 验证数据完整性" -ForegroundColor Gray
    Write-Host "  3. 开发实体类和Mapper接口" -ForegroundColor Gray
} else {
    Write-Host "❌ 数据库迁移失败!" -ForegroundColor Red
    Write-Host "请检查错误信息并修复问题后重试" -ForegroundColor Yellow
}
Write-Host "================================================" -ForegroundColor Cyan

# 清理敏感信息
$DB_PASSWORD_PLAIN = $null
