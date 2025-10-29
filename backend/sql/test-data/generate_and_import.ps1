# =====================================================
# 智慧医养平台 - 测试数据生成和导入脚本
# 版本: V1.0
# 创建日期: 2025-10-28
# =====================================================

[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "智慧医养平台 - 测试数据生成工具 V1.0" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

$SCRIPT_PATH = $PSScriptRoot

# 检查Node.js环境
Write-Host "🔍 检查Node.js环境..." -ForegroundColor Yellow
try {
    $nodeVersion = node --version
    Write-Host "  ✓ Node.js版本: $nodeVersion" -ForegroundColor Green
} catch {
    Write-Host "  ✗ 未检测到Node.js，请先安装Node.js!" -ForegroundColor Red
    Write-Host "  下载地址: https://nodejs.org/" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "📋 数据生成配置:" -ForegroundColor Yellow
Write-Host "  脚本目录: $SCRIPT_PATH" -ForegroundColor Gray
Write-Host ""

# 生成基础数据
Write-Host "🚀 步骤1: 生成基础数据..." -ForegroundColor Cyan
Write-Host "  包含: 机构、用户、医生、护工、社工、老人、关联关系" -ForegroundColor Gray

$basicDataFile = Join-Path $SCRIPT_PATH "basic_data.sql"
$basicScriptFile = Join-Path $SCRIPT_PATH "generate_basic_data.js"

if (Test-Path $basicScriptFile) {
    try {
        node $basicScriptFile | Out-File -FilePath $basicDataFile -Encoding UTF8
        Write-Host "  ✓ 基础数据SQL文件生成成功: $basicDataFile" -ForegroundColor Green
    } catch {
        Write-Host "  ✗ 基础数据生成失败: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "  ✗ 脚本文件不存在: $basicScriptFile" -ForegroundColor Red
    exit 1
}

Write-Host ""

# 生成业务数据
Write-Host "🚀 步骤2: 生成业务数据..." -ForegroundColor Cyan
Write-Host "  包含: 健康记录、健康预警、护理记录、用药记录、巡诊记录" -ForegroundColor Gray

$businessDataFile = Join-Path $SCRIPT_PATH "business_data.sql"
$businessScriptFile = Join-Path $SCRIPT_PATH "generate_business_data.js"

if (Test-Path $businessScriptFile) {
    try {
        node $businessScriptFile | Out-File -FilePath $businessDataFile -Encoding UTF8
        Write-Host "  ✓ 业务数据SQL文件生成成功: $businessDataFile" -ForegroundColor Green
    } catch {
        Write-Host "  ✗ 业务数据生成失败: $($_.Exception.Message)" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "  ✗ 脚本文件不存在: $businessScriptFile" -ForegroundColor Red
    exit 1
}

Write-Host ""

# 询问是否导入数据库
Write-Host "📊 数据文件生成完成!" -ForegroundColor Green
Write-Host ""
Write-Host "生成的文件:" -ForegroundColor Yellow
Write-Host "  1. $basicDataFile" -ForegroundColor Gray
Write-Host "  2. $businessDataFile" -ForegroundColor Gray
Write-Host ""

$importChoice = Read-Host "是否立即导入到数据库? (yes/no)"

if ($importChoice -eq "yes") {
    Write-Host ""
    Write-Host "📥 开始导入数据..." -ForegroundColor Cyan
    
    # 数据库配置
    $DB_HOST = "localhost"
    $DB_PORT = "3306"
    $DB_NAME = "smartcare_cloud"
    $DB_USER = "root"
    
    Write-Host "  数据库地址: ${DB_HOST}:${DB_PORT}" -ForegroundColor Gray
    Write-Host "  数据库名称: $DB_NAME" -ForegroundColor Gray
    Write-Host ""
    
    $DB_PASSWORD = Read-Host "请输入MySQL root密码" -AsSecureString
    $BSTR = [System.Runtime.InteropServices.Marshal]::SecureStringToBSTR($DB_PASSWORD)
    $DB_PASSWORD_PLAIN = [System.Runtime.InteropServices.Marshal]::PtrToStringAuto($BSTR)
    
    Write-Host ""
    
    # 导入基础数据
    Write-Host "  [1/2] 导入基础数据..." -ForegroundColor Cyan
    try {
        Get-Content $basicDataFile | & mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p"$DB_PASSWORD_PLAIN" $DB_NAME 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "    ✓ 基础数据导入成功" -ForegroundColor Green
        } else {
            Write-Host "    ✗ 基础数据导入失败" -ForegroundColor Red
        }
    } catch {
        Write-Host "    ✗ 导入出错: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    # 导入业务数据
    Write-Host "  [2/2] 导入业务数据..." -ForegroundColor Cyan
    try {
        Get-Content $businessDataFile | & mysql -h $DB_HOST -P $DB_PORT -u $DB_USER -p"$DB_PASSWORD_PLAIN" $DB_NAME 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Host "    ✓ 业务数据导入成功" -ForegroundColor Green
        } else {
            Write-Host "    ✗ 业务数据导入失败" -ForegroundColor Red
        }
    } catch {
        Write-Host "    ✗ 导入出错: $($_.Exception.Message)" -ForegroundColor Red
    }
    
    # 清理密码
    $DB_PASSWORD_PLAIN = $null
    
    Write-Host ""
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host "✅ 数据导入完成!" -ForegroundColor Green
    Write-Host "================================================" -ForegroundColor Cyan
} else {
    Write-Host ""
    Write-Host "💡 提示: 您可以手动执行以下命令导入数据:" -ForegroundColor Yellow
    Write-Host "  mysql -uroot -p smartcare_db < basic_data.sql" -ForegroundColor Gray
    Write-Host "  mysql -uroot -p smartcare_db < business_data.sql" -ForegroundColor Gray
}

Write-Host ""
Write-Host "🎉 完成! 测试数据已准备就绪" -ForegroundColor Green
Write-Host ""
Write-Host "📋 测试账号:" -ForegroundColor Yellow
Write-Host "  系统管理员: admin / 123456" -ForegroundColor Gray
Write-Host "  业务管理员: business_admin1 / 123456" -ForegroundColor Gray
Write-Host "  医生: doctor1 / 123456" -ForegroundColor Gray
Write-Host "  护工: nurse1 / 123456" -ForegroundColor Gray
Write-Host "  社工: social_worker1 / 123456" -ForegroundColor Gray
Write-Host "  家属: family1 / 123456" -ForegroundColor Gray
