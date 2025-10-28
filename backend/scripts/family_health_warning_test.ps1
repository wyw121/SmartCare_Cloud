#!/usr/bin/env pwsh
# 家属端健康预警功能验证脚本

$headers = @{
    "Authorization" = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0LWZhbWlseSIsInVzZXJJZCI6MywicGVybWlzc2lvbnMiOltdLCJpYXQiOjE3MzYwNjY0MzQsImV4cCI6MTczNjY3MTIzNH0.vkKwJ3vFXZzEz5CjSnAYjPmQ2oC8M_EiCK_HO2iuPfE"
}

Write-Host "======================================" -ForegroundColor Green
Write-Host "智慧医养平台 - 家属端健康预警功能验证" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Green

Write-Host "`n1. 测试家属关联老人列表API..." -ForegroundColor Yellow
try {
    $elderlyResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/family/elderly/list" -Headers $headers
    Write-Host "✅ 成功获取关联老人列表，共 $($elderlyResponse.data.Count) 位老人" -ForegroundColor Green

    # 显示老人基本信息
    foreach ($elderly in $elderlyResponse.data) {
        Write-Host "   - 老人ID: $($elderly.id), 姓名: $($elderly.name), 房间号: $($elderly.roomNumber)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ 获取关联老人列表失败: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

Write-Host "`n2. 测试健康预警统计API..." -ForegroundColor Yellow
try {
    $statsResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/family/warnings/statistics" -Headers $headers
    Write-Host "✅ 成功获取健康预警统计" -ForegroundColor Green
    Write-Host "   - 总预警数: $($statsResponse.data.total)" -ForegroundColor Cyan
    Write-Host "   - 高危: $($statsResponse.data.high), 中危: $($statsResponse.data.medium), 低危: $($statsResponse.data.low), 紧急: $($statsResponse.data.urgent)" -ForegroundColor Cyan
} catch {
    Write-Host "❌ 获取健康预警统计失败: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n3. 测试单个老人预警详情API..." -ForegroundColor Yellow
try {
    # 测试第一个老人的预警信息
    $firstElderlyId = $elderlyResponse.data[0].id
    $warningsResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/family/elderly/$firstElderlyId/warnings" -Headers $headers
    Write-Host "✅ 成功获取老人ID $firstElderlyId 的预警信息，共 $($warningsResponse.data.Count) 条预警" -ForegroundColor Green

    # 显示预警详情
    foreach ($warning in $warningsResponse.data) {
        Write-Host "   - 预警: $($warning.message), 级别: $($warning.level)" -ForegroundColor Cyan
    }
} catch {
    Write-Host "❌ 获取单个老人预警信息失败: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n4. 验证权限控制..." -ForegroundColor Yellow
try {
    # 测试访问不属于家属的老人（应该被拒绝）
    $unauthorizedResponse = Invoke-RestMethod -Uri "http://localhost:8080/api/family/elderly/99/warnings" -Headers $headers -ErrorAction Stop
    Write-Host "⚠️  权限控制可能存在问题，能访问未关联的老人信息" -ForegroundColor Orange
} catch {
    if ($_.Exception.Response.StatusCode -eq 403 -or $_.Exception.Message -like "*无权限*") {
        Write-Host "✅ 权限控制正常，无法访问未关联的老人信息" -ForegroundColor Green
    } else {
        Write-Host "❓ 访问未关联老人时发生其他错误: $($_.Exception.Message)" -ForegroundColor Orange
    }
}

Write-Host "`n======================================" -ForegroundColor Green
Write-Host "家属端健康预警功能验证完成！" -ForegroundColor Green
Write-Host "======================================" -ForegroundColor Green

# 检查数据库关联关系
Write-Host "`n5. 数据库关联关系验证..." -ForegroundColor Yellow
try {
    & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p123456 smartcare_cloud -e "SELECT fu.real_name AS 家属姓名, fer.elderly_id AS 关联老人ID, fer.relationship_name AS 关系, fer.contact_priority AS 优先级 FROM family_user fu JOIN family_elderly_relation fer ON fu.id = fer.family_user_id WHERE fu.sys_user_id = 3 ORDER BY fer.contact_priority;"
    Write-Host "✅ 数据库关联关系验证完成" -ForegroundColor Green
} catch {
    Write-Host "❌ 数据库关联关系验证失败: $($_.Exception.Message)" -ForegroundColor Red
}
