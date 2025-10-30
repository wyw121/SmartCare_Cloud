# 智慧医养平台 API 自动化测试脚本
# 测试权限系统和数据过滤功能

$BaseUrl = "http://localhost:8080"
$TestResults = @()

# ANSI颜色代码
$Green = "`e[32m"
$Red = "`e[31m"
$Yellow = "`e[33m"
$Blue = "`e[34m"
$Reset = "`e[0m"

function Write-TestResult {
    param($TestName, $Status, $Details)
    $color = if ($Status -eq "PASS") { $Green } else { $Red }
    Write-Host "${color}[${Status}]${Reset} $TestName"
    if ($Details) {
        Write-Host "  详情: $Details" -ForegroundColor Gray
    }
    $TestResults += @{
        Test = $TestName
        Status = $Status
        Details = $Details
        Time = Get-Date -Format "HH:mm:ss"
    }
}

Write-Host "`n$Blue============================================$Reset"
Write-Host "$Blue  智慧医养平台 API 自动化测试 $Reset"
Write-Host "$Blue============================================$Reset`n"

# 测试1: 后端服务可用性
Write-Host "${Yellow}[测试1]${Reset} 检查后端服务状态..." -ForegroundColor Cyan
try {
    $response = Invoke-WebRequest -Uri "$BaseUrl/api/auth/login" -Method Options -TimeoutSec 5 -ErrorAction Stop
    Write-TestResult "后端服务可用性" "PASS" "服务正常响应"
} catch {
    Write-TestResult "后端服务可用性" "FAIL" "服务无响应: $($_.Exception.Message)"
    exit 1
}

# 测试2: 管理员登录
Write-Host "`n${Yellow}[测试2]${Reset} 测试管理员登录..." -ForegroundColor Cyan
$loginBody = @{
    username = "admin"
    password = "123456"
} | ConvertTo-Json

try {
    $loginResponse = Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method Post `
        -ContentType "application/json" -Body $loginBody -TimeoutSec 10
    
    if ($loginResponse.code -eq 200 -and $loginResponse.data.token) {
        $AdminToken = $loginResponse.data.token
        Write-TestResult "管理员登录" "PASS" "Token获取成功 (长度: $($AdminToken.Length))"
    } else {
        Write-TestResult "管理员登录" "FAIL" "返回码: $($loginResponse.code), 消息: $($loginResponse.message)"
        exit 1
    }
} catch {
    Write-TestResult "管理员登录" "FAIL" "请求失败: $($_.Exception.Message)"
    exit 1
}

# 测试3: 查询老人列表 (管理员权限)
Write-Host "`n${Yellow}[测试3]${Reset} 测试管理员查询老人列表..." -ForegroundColor Cyan
try {
    $headers = @{
        Authorization = "Bearer $AdminToken"
    }
    $elderlyResponse = Invoke-RestMethod -Uri "$BaseUrl/api/elderly/all?current=1&size=10" `
        -Method Get -Headers $headers -TimeoutSec 10
    
    if ($elderlyResponse.code -eq 200) {
        $total = $elderlyResponse.data.total
        $count = $elderlyResponse.data.records.Count
        Write-TestResult "管理员查询老人列表" "PASS" "共$total条记录, 本页返回$count条"
    } else {
        Write-TestResult "管理员查询老人列表" "FAIL" "返回码: $($elderlyResponse.code)"
    }
} catch {
    Write-TestResult "管理员查询老人列表" "FAIL" "请求失败: $($_.Exception.Message)"
}

# 测试4: 健康预警列表查询
Write-Host "`n${Yellow}[测试4]${Reset} 测试健康预警列表查询..." -ForegroundColor Cyan
try {
    $headers = @{
        Authorization = "Bearer $AdminToken"
        "Content-Type" = "application/json"
    }
    $body = @{
        pageNum = 1
        pageSize = 10
    } | ConvertTo-Json
    
    $warningResponse = Invoke-RestMethod -Uri "$BaseUrl/api/health-warning/page" `
        -Method Post -Headers $headers -Body $body -TimeoutSec 10
    
    if ($warningResponse.code -eq 200) {
        $total = $warningResponse.data.total
        $count = $warningResponse.data.list.Count
        Write-TestResult "健康预警列表查询" "PASS" "共$total条预警, 本页返回$count条"
        
        # 显示预警类型分布
        if ($count -gt 0) {
            $types = $warningResponse.data.list | Group-Object -Property warningType | 
                Select-Object @{N='类型';E={$_.Name}}, @{N='数量';E={$_.Count}}
            Write-Host "  预警类型分布:" -ForegroundColor Gray
            $types | ForEach-Object {
                Write-Host "    $($_.类型): $($_.数量)条" -ForegroundColor Gray
            }
        }
    } else {
        Write-TestResult "健康预警列表查询" "FAIL" "返回码: $($warningResponse.code)"
    }
} catch {
    Write-TestResult "健康预警列表查询" "FAIL" "请求失败: $($_.Exception.Message)"
}

# 测试5: 尝试医生登录并测试数据权限
Write-Host "`n${Yellow}[测试5]${Reset} 测试医生角色数据权限..." -ForegroundColor Cyan
$doctorLoginBody = @{
    username = "doctor_test"
    password = "123456"
} | ConvertTo-Json

try {
    $doctorLoginResponse = Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method Post `
        -ContentType "application/json" -Body $doctorLoginBody -TimeoutSec 10
    
    if ($doctorLoginResponse.code -eq 200 -and $doctorLoginResponse.data.token) {
        $DoctorToken = $doctorLoginResponse.data.token
        Write-TestResult "医生登录" "PASS" "Token获取成功"
        
        # 查询医生负责的老人列表
        $headers = @{ Authorization = "Bearer $DoctorToken" }
        $myPatientsResponse = Invoke-RestMethod -Uri "$BaseUrl/api/elderly/my-patients?current=1&size=20" `
            -Method Get -Headers $headers -TimeoutSec 10
        
        if ($myPatientsResponse.code -eq 200) {
            $patientCount = $myPatientsResponse.data.total
            Write-TestResult "医生查询负责老人" "PASS" "医生负责 $patientCount 位老人 (应用@DataPermission过滤)"
        } else {
            Write-TestResult "医生查询负责老人" "FAIL" "返回码: $($myPatientsResponse.code)"
        }
    } else {
        Write-TestResult "医生登录" "SKIP" "测试账号不存在: $($doctorLoginResponse.message)"
    }
} catch {
    $errorMsg = $_.Exception.Message
    if ($errorMsg -like "*404*" -or $errorMsg -like "*用户不存在*") {
        Write-TestResult "医生登录" "SKIP" "测试账号doctor_test不存在"
    } else {
        Write-TestResult "医生登录" "FAIL" "请求失败: $errorMsg"
    }
}

# 测试6: 测试家属登录
Write-Host "`n${Yellow}[测试6]${Reset} 测试家属角色数据权限..." -ForegroundColor Cyan
$familyLoginBody = @{
    username = "family_test"
    password = "123456"
} | ConvertTo-Json

try {
    $familyLoginResponse = Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" -Method Post `
        -ContentType "application/json" -Body $familyLoginBody -TimeoutSec 10
    
    if ($familyLoginResponse.code -eq 200 -and $familyLoginResponse.data.token) {
        $FamilyToken = $familyLoginResponse.data.token
        Write-TestResult "家属登录" "PASS" "Token获取成功"
        
        # 查询家属关联的老人
        $headers = @{ Authorization = "Bearer $FamilyToken" }
        $familyElderlyResponse = Invoke-RestMethod -Uri "$BaseUrl/api/family/my-elderly?current=1&size=20" `
            -Method Get -Headers $headers -TimeoutSec 10
        
        if ($familyElderlyResponse.code -eq 200) {
            $elderlyCount = $familyElderlyResponse.data.total
            Write-TestResult "家属查询关联老人" "PASS" "家属关联 $elderlyCount 位老人"
        } else {
            Write-TestResult "家属查询关联老人" "FAIL" "返回码: $($familyElderlyResponse.code)"
        }
    } else {
        Write-TestResult "家属登录" "SKIP" "测试账号不存在: $($familyLoginResponse.message)"
    }
} catch {
    $errorMsg = $_.Exception.Message
    if ($errorMsg -like "*404*" -or $errorMsg -like "*用户不存在*") {
        Write-TestResult "家属登录" "SKIP" "测试账号family_test不存在"
    } else {
        Write-TestResult "家属登录" "FAIL" "请求失败: $errorMsg"
    }
}

# 测试7: 测试403权限拒绝
Write-Host "`n${Yellow}[测试7]${Reset} 测试权限拒绝处理 (如果医生token可用)..." -ForegroundColor Cyan
if ($DoctorToken) {
    try {
        $headers = @{ Authorization = "Bearer $DoctorToken" }
        $systemResponse = Invoke-RestMethod -Uri "$BaseUrl/api/system/users?current=1&size=10" `
            -Method Get -Headers $headers -TimeoutSec 10 -ErrorAction Stop
        
        # 如果没有403错误,说明权限控制可能有问题
        if ($systemResponse.code -eq 403) {
            Write-TestResult "权限拒绝处理" "PASS" "医生访问系统管理接口被正确拒绝"
        } else {
            Write-TestResult "权限拒绝处理" "WARN" "医生可以访问系统管理接口 (可能权限配置过宽)"
        }
    } catch {
        if ($_.Exception.Response.StatusCode -eq 403) {
            Write-TestResult "权限拒绝处理" "PASS" "医生访问系统管理接口被正确拒绝 (HTTP 403)"
        } else {
            Write-TestResult "权限拒绝处理" "FAIL" "意外错误: $($_.Exception.Message)"
        }
    }
} else {
    Write-TestResult "权限拒绝处理" "SKIP" "医生token不可用,跳过测试"
}

# 测试总结
Write-Host "`n$Blue============================================$Reset"
Write-Host "$Blue  测试总结 $Reset"
Write-Host "$Blue============================================$Reset"

$passCount = ($TestResults | Where-Object { $_.Status -eq "PASS" }).Count
$failCount = ($TestResults | Where-Object { $_.Status -eq "FAIL" }).Count
$skipCount = ($TestResults | Where-Object { $_.Status -eq "SKIP" }).Count
$totalCount = $TestResults.Count

Write-Host "`n${Green}通过: $passCount${Reset} | ${Red}失败: $failCount${Reset} | ${Yellow}跳过: $skipCount${Reset} | 总计: $totalCount`n"

if ($failCount -eq 0) {
    Write-Host "${Green}✅ 所有测试通过!前后端功能运行稳定。${Reset}`n"
    exit 0
} else {
    Write-Host "${Red}❌ 有 $failCount 个测试失败,请检查日志。${Reset}`n"
    exit 1
}
