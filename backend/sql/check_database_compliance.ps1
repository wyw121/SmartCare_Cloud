# ====================================================
# 数据库规范性检查脚本
# 检查实际数据库结构与文档定义是否一致
# ====================================================

Write-Host "================================================" -ForegroundColor Cyan
Write-Host "智慧医养平台 - 数据库规范性检查工具" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""

# 数据库连接参数
$DbHost = "localhost"
$DbPort = 3306
$DbName = "smartcare_cloud"
$DbUser = "root"

# 文档定义的表列表(按文档顺序)
$DocumentTables = @(
    # 用户与权限管理
    "sys_user",
    "sys_role",
    "sys_permission",
    "sys_role_permission",
    
    # 老人档案管理
    "elderly",
    "family_elderly_relation",
    
    # 医护人员管理
    "t_doctor",
    "doctor_elderly_relation",
    "nurse_info",
    "nurse_elderly_relation",
    "social_worker_info",
    
    # 健康管理
    "health_record",
    "health_warning",
    "assessment_report",
    
    # 护理与服务
    "nursing_record",
    "medication_record",
    "visit_record",
    
    # 设备管理
    "equipment",
    "equipment_usage_record",
    
    # 系统日志
    "operation_log",
    "organization_info"
)

Write-Host "📋 数据库连接信息:" -ForegroundColor Yellow
Write-Host "  主机: ${DbHost}:${DbPort}"
Write-Host "  数据库: $DbName"
Write-Host ""

# 输入密码
$DbPassword = Read-Host "请输入MySQL root密码" -AsSecureString
$DbPasswordPlain = [Runtime.InteropServices.Marshal]::PtrToStringAuto([Runtime.InteropServices.Marshal]::SecureStringToBSTR($DbPassword))

Write-Host ""
Write-Host "🔍 开始检查..." -ForegroundColor Green
Write-Host ""

# 1. 检查数据库是否存在
Write-Host "[1/4] 检查数据库是否存在..." -ForegroundColor Cyan
$checkDb = mysql -u$DbUser -p$DbPasswordPlain -e "SHOW DATABASES LIKE '$DbName';" 2>&1
if ($checkDb -match $DbName) {
    Write-Host "  ✓ 数据库存在" -ForegroundColor Green
} else {
    Write-Host "  ✗ 数据库不存在!" -ForegroundColor Red
    exit 1
}

# 2. 获取实际存在的表
Write-Host ""
Write-Host "[2/4] 获取数据库中的实际表..." -ForegroundColor Cyan
$actualTablesRaw = mysql -u$DbUser -p$DbPasswordPlain -D$DbName -e "SHOW TABLES;" 2>&1 | Select-Object -Skip 1
$actualTables = $actualTablesRaw | Where-Object { $_ -and $_.Trim() -ne "" }

Write-Host "  实际表数量: $($actualTables.Count)" -ForegroundColor White
Write-Host "  文档定义表数量: $($DocumentTables.Count)" -ForegroundColor White

# 3. 对比表的存在性
Write-Host ""
Write-Host "[3/4] 检查表的一致性..." -ForegroundColor Cyan
$missingTables = @()
$extraTables = @()
$existingTables = @()

# 检查文档定义的表是否都存在
foreach ($table in $DocumentTables) {
    if ($actualTables -contains $table) {
        $existingTables += $table
        Write-Host "  ✓ $table" -ForegroundColor Green
    } else {
        $missingTables += $table
        Write-Host "  ✗ $table (文档中定义但数据库中不存在)" -ForegroundColor Red
    }
}

# 检查是否有额外的表
foreach ($table in $actualTables) {
    if ($DocumentTables -notcontains $table) {
        $extraTables += $table
        Write-Host "  ⚠ $table (数据库中存在但文档中未定义)" -ForegroundColor Yellow
    }
}

# 4. 详细字段检查(仅检查存在的表)
Write-Host ""
Write-Host "[4/4] 详细字段检查(导出到文件)..." -ForegroundColor Cyan

$reportPath = "database_compliance_report.md"
$reportContent = @"
# 数据库规范性检查报告

**检查时间:** $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
**数据库:** $DbName

---

## 📊 总体统计

| 项目 | 数量 |
|------|------|
| 文档定义表数量 | $($DocumentTables.Count) |
| 实际数据库表数量 | $($actualTables.Count) |
| 匹配的表 | $($existingTables.Count) |
| 缺失的表 | $($missingTables.Count) |
| 额外的表 | $($extraTables.Count) |

---

## ❌ 缺失的表 ($($missingTables.Count))

"@

if ($missingTables.Count -eq 0) {
    $reportContent += "`n无缺失表`n"
} else {
    $reportContent += "`n| 表名 | 说明 |`n"
    $reportContent += "|------|------|`n"
    foreach ($table in $missingTables) {
        $reportContent += "| ``$table`` | 文档中定义但数据库中不存在 |`n"
    }
}

$reportContent += @"

---

## ⚠️ 额外的表 ($($extraTables.Count))

"@

if ($extraTables.Count -eq 0) {
    $reportContent += "`n无额外表`n"
} else {
    $reportContent += "`n| 表名 | 说明 |`n"
    $reportContent += "|------|------|`n"
    foreach ($table in $extraTables) {
        $reportContent += "| ``$table`` | 数据库中存在但文档中未定义 |`n"
    }
}

$reportContent += @"

---

## ✅ 匹配的表 ($($existingTables.Count))

"@

foreach ($table in $existingTables) {
    $desc = mysql -u$DbUser -p$DbPasswordPlain -D$DbName -e "DESC $table;" 2>&1 | Out-String
    $reportContent += @"

### $table

``````
$desc
``````

"@
}

$reportContent += @"

---

## 📋 建议

"@

if ($missingTables.Count -gt 0) {
    $reportContent += "`n### 🔴 缺失表处理`n`n"
    $reportContent += "以下表在文档中定义但数据库中不存在,需要创建:`n`n"
    foreach ($table in $missingTables) {
        $reportContent += "- [ ] 创建表 ``$table```n"
    }
}

if ($extraTables.Count -gt 0) {
    $reportContent += "`n### 🟡 额外表处理`n`n"
    $reportContent += "以下表在数据库中存在但文档中未定义,需要:`n"
    $reportContent += "1. 如果这些表是必需的,请更新文档添加说明`n"
    $reportContent += "2. 如果这些表是废弃的,请删除或标记为已删除`n`n"
    foreach ($table in $extraTables) {
        $reportContent += "- [ ] 处理表 ``$table```n"
    }
}

$reportContent += "`n---`n`n**检查完成!**`n"

# 保存报告
$reportContent | Out-File -FilePath $reportPath -Encoding UTF8

Write-Host ""
Write-Host "================================================" -ForegroundColor Cyan
Write-Host "✅ 检查完成!" -ForegroundColor Green
Write-Host "================================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "📊 检查结果:" -ForegroundColor Yellow
Write-Host "  ✓ 匹配的表: $($existingTables.Count)" -ForegroundColor Green
if ($missingTables.Count -gt 0) {
    Write-Host "  ✗ 缺失的表: $($missingTables.Count)" -ForegroundColor Red
}
if ($extraTables.Count -gt 0) {
    Write-Host "  ⚠ 额外的表: $($extraTables.Count)" -ForegroundColor Yellow
}
Write-Host ""
Write-Host "📄 详细报告已保存至: $reportPath" -ForegroundColor Cyan
Write-Host ""
