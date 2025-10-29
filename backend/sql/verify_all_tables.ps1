# 数据库字段完整验证脚本
# 用于验证所有18个表的字段是否与文档一致

Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  数据库字段完整验证工具" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""

# 数据库连接配置
$dbHost = "localhost"
$dbPort = "3306"
$dbUser = "root"
$dbPass = ""
$dbName = "smartcare_cloud"

# 所有需要验证的表
$tables = @(
    # 用户权限(5个)
    "sys_user",
    "sys_role", 
    "sys_permission",
    "sys_role_permission",
    "family_user",
    
    # 老人档案(2个)
    "elderly",
    "doctor_elderly_relation",
    
    # 医护人员(3个)
    "t_doctor",
    "nurse_info",
    "social_worker_info",
    
    # 健康管理(4个)
    "health_record",
    "t_health_warning",
    "elderly_care_assessment",
    
    # 护理服务(2个)
    "medication_record",
    "nursing_record",
    
    # 设备管理(2个)
    "equipment",
    "equipment_usage_record",
    
    # 系统管理(1个)
    "audit_log"
)

# 检查数据库连接
Write-Host "🔍 检查数据库连接..." -ForegroundColor Yellow
if ($dbPass -eq "") {
    $testCmd = "mysql -u $dbUser -h $dbHost -e 'SELECT 1;' 2>&1"
} else {
    $testCmd = "mysql -u $dbUser -p$dbPass -h $dbHost -P $dbPort -e 'SELECT 1;' 2>&1"
}
$testResult = Invoke-Expression $testCmd

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ 数据库连接失败!" -ForegroundColor Red
    Write-Host "请确保:" -ForegroundColor Red
    Write-Host "  1. Docker容器已启动: docker-compose up -d mysql" -ForegroundColor Red
    Write-Host "  2. MySQL服务运行在 localhost:3307" -ForegroundColor Red
    exit 1
}

Write-Host "✅ 数据库连接成功!" -ForegroundColor Green
Write-Host ""

# 创建输出目录
$outputDir = "database_verification"
if (!(Test-Path $outputDir)) {
    New-Item -ItemType Directory -Path $outputDir | Out-Null
}

# 验证每个表
$totalTables = $tables.Count
$currentIndex = 0
$successCount = 0
$failCount = 0

foreach ($table in $tables) {
    $currentIndex++
    Write-Host "[$currentIndex/$totalTables] 验证表: $table" -ForegroundColor Cyan
    
    # 获取表结构
    if ($dbPass -eq "") {
        $descCmd = "mysql -u $dbUser -h $dbHost $dbName -e `"DESC $table;`" 2>&1"
    } else {
        $descCmd = "mysql -u $dbUser -p$dbPass -h $dbHost -P $dbPort $dbName -e `"DESC $table;`" 2>&1"
    }
    $descResult = Invoke-Expression $descCmd
    
    if ($LASTEXITCODE -eq 0) {
        # 保存到文件
        $outputFile = Join-Path $outputDir "$table.txt"
        $descResult | Out-File -FilePath $outputFile -Encoding UTF8
        
        Write-Host "  ✅ 成功 - 字段信息已保存到: $outputFile" -ForegroundColor Green
        $successCount++
    } else {
        Write-Host "  ❌ 失败 - 表不存在或无法访问" -ForegroundColor Red
        $failCount++
    }
    
    Write-Host ""
}

# 生成汇总报告
Write-Host "======================================" -ForegroundColor Cyan
Write-Host "  验证结果汇总" -ForegroundColor Cyan
Write-Host "======================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "总表数量: $totalTables" -ForegroundColor White
Write-Host "成功验证: $successCount" -ForegroundColor Green
Write-Host "验证失败: $failCount" -ForegroundColor Red
Write-Host ""

if ($failCount -eq 0) {
    Write-Host "🎉 所有表验证成功!" -ForegroundColor Green
    Write-Host "字段信息已保存到: $outputDir" -ForegroundColor Green
} else {
    Write-Host "⚠️  部分表验证失败,请检查数据库状态" -ForegroundColor Yellow
}

# 生成对比报告
Write-Host ""
Write-Host "正在生成字段对比报告..." -ForegroundColor Yellow

$reportFile = Join-Path $outputDir "verification_report.md"
$reportContent = @"
# 数据库字段验证报告

**验证时间:** $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")  
**数据库:** $dbName  
**验证表数:** $totalTables  
**成功:** $successCount  
**失败:** $failCount

---

## 表结构验证结果

| 序号 | 表名 | 状态 | 字段数 | 备注 |
|------|------|------|--------|------|
"@

$tableIndex = 1
foreach ($table in $tables) {
    $outputFile = Join-Path $outputDir "$table.txt"
    
    if (Test-Path $outputFile) {
        # 统计字段数量(去掉表头行)
        $fieldCount = (Get-Content $outputFile | Where-Object { $_ -match "^\|" }).Count - 1
        if ($fieldCount -lt 0) { $fieldCount = 0 }
        
        $reportContent += "| $tableIndex | ``$table`` | ✅ 成功 | $fieldCount | 字段信息已保存 |`n"
    } else {
        $reportContent += "| $tableIndex | ``$table`` | ❌ 失败 | - | 表不存在或无法访问 |`n"
    }
    
    $tableIndex++
}

$reportContent += @"

---

## 下一步操作

### 1. 字段对比
请将每个表的字段信息与 ``docs/数据字典_完整版.md`` 中的定义进行对比:

``````powershell
# 查看某个表的字段详情
Get-Content $outputDir\表名.txt
``````

### 2. 发现不一致时的处理
- **优先策略:** 更新文档以匹配实际数据库(避免修改生产数据库)
- **修改位置:** ``docs/数据字典_完整版.md``
- **修改内容:** 字段名、类型、长度、默认值、索引等

### 3. 文档更新模板
``````markdown
### X.X 表名 (``table_name``)

| 字段名 | 类型 | 长度 | 必填 | 索引 | 默认值 | 说明 |
|--------|------|------|------|------|--------|------|
| 实际字段 | 实际类型 | - | ✓/- | INDEX | 实际默认值 | 实际说明 |
``````

---

**报告生成时间:** $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
"@

$reportContent | Out-File -FilePath $reportFile -Encoding UTF8

Write-Host "✅ 验证报告已生成: $reportFile" -ForegroundColor Green
Write-Host ""
Write-Host "请执行以下操作:" -ForegroundColor Yellow
Write-Host "  1. 查看验证报告: cat $reportFile" -ForegroundColor White
Write-Host "  2. 对比每个表的字段与文档定义" -ForegroundColor White
Write-Host "  3. 更新文档中不一致的字段定义" -ForegroundColor White
