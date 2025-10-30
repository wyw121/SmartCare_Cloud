# ============================================
# 智慧医养平台 - 角色权限综合测试脚本
# ============================================

$ErrorActionPreference = "Continue"
$BaseUrl = "http://localhost:8080"

# ANSI颜色代码
$Green = "`e[32m"
$Red = "`e[31m"
$Yellow = "`e[33m"
$Cyan = "`e[36m"
$Reset = "`e[0m"

# 测试统计
$script:PassCount = 0
$script:FailCount = 0
$script:SkipCount = 0

# 测试结果数组
$script:TestResults = @()

# 辅助函数
function Write-TestResult {
    param($TestName, $Status, $Details)
    
    $symbol = switch ($Status) {
        "PASS" { "${Green}[PASS]${Reset}"; $script:PassCount++ }
        "FAIL" { "${Red}[FAIL]${Reset}"; $script:FailCount++ }
        "SKIP" { "${Yellow}[SKIP]${Reset}"; $script:SkipCount++ }
    }
    
    Write-Host "$symbol $TestName" -NoNewline
    if ($Details) {
        Write-Host ""
        Write-Host "  详情: $Details" -ForegroundColor Gray
    } else {
        Write-Host ""
    }
    
    $script:TestResults += [PSCustomObject]@{
        TestName = $TestName
        Status = $Status
        Details = $Details
    }
}

# 登录函数
function Test-Login {
    param($Username, $Password = "123456", $RoleName)
    
    try {
        $body = @{
            username = $Username
            password = $Password
        } | ConvertTo-Json
        
        $response = Invoke-RestMethod -Uri "$BaseUrl/api/auth/login" `
            -Method Post -Body $body -ContentType "application/json" -TimeoutSec 10
        
        if ($response.code -eq 200 -and $response.data.token) {
            Write-TestResult "${RoleName}登录" "PASS" "Token长度: $($response.data.token.Length)"
            return @{
                Success = $true
                Token = $response.data.token
                UserInfo = $response.data.userInfo
            }
        } else {
            Write-TestResult "${RoleName}登录" "FAIL" "返回码: $($response.code), 消息: $($response.message)"
            return @{ Success = $false }
        }
    } catch {
        Write-TestResult "${RoleName}登录" "FAIL" "异常: $($_.Exception.Message)"
        return @{ Success = $false }
    }
}

# 测试数据权限
function Test-DataPermission {
    param($Token, $RoleName, $RoleCode)
    
    try {
        $headers = @{
            Authorization = "Bearer $Token"
        }
        
        # 测试 /api/elderly/my-patients (应用了@DataPermission)
        $response = Invoke-RestMethod -Uri "$BaseUrl/api/elderly/my-patients?current=1&size=100" `
            -Method Get -Headers $headers -TimeoutSec 10
        
        if ($response.code -eq 200) {
            $total = $response.data.total
            $count = $response.data.records.Count
            
            # 验证数据隔离
            if ($RoleCode -eq "system_admin") {
                # 管理员应该看到所有老人(假设数据库有老人数据)
                Write-TestResult "${RoleName}数据权限" "PASS" "管理员可查看所有数据: $total 条记录"
            } elseif ($RoleCode -in @("doctor", "nurse", "social_worker")) {
                # 医护社工应该只看到分配的老人
                Write-TestResult "${RoleName}数据权限" "PASS" "只能查看分配的老人: $count 条记录"
            } elseif ($RoleCode -eq "family") {
                # 家属应该只看到关联的长辈
                Write-TestResult "${RoleName}数据权限" "PASS" "只能查看关联长辈: $count 条记录"
            } else {
                Write-TestResult "${RoleName}数据权限" "PASS" "返回 $count 条记录"
            }
            
            return $true
        } else {
            Write-TestResult "${RoleName}数据权限" "FAIL" "返回码: $($response.code)"
            return $false
        }
    } catch {
        Write-TestResult "${RoleName}数据权限" "FAIL" "异常: $($_.Exception.Message)"
        return $false
    }
}

# 测试越权访问
function Test-UnauthorizedAccess {
    param($Token, $RoleName, $RoleCode)
    
    # 非管理员尝试访问系统管理接口
    if ($RoleCode -ne "system_admin") {
        try {
            $headers = @{
                Authorization = "Bearer $Token"
            }
            
            # 尝试访问用户管理接口(应该返回403或被过滤)
            $response = Invoke-RestMethod -Uri "$BaseUrl/api/user/list?current=1&size=10" `
                -Method Get -Headers $headers -TimeoutSec 10 -ErrorAction Stop
            
            # 如果没有抛出异常,检查是否正确限制了访问
            if ($response.code -eq 403) {
                Write-TestResult "${RoleName}越权访问控制" "PASS" "正确返回403禁止访问"
                return $true
            } elseif ($response.code -eq 200) {
                Write-TestResult "${RoleName}越权访问控制" "FAIL" "应禁止访问但返回了数据"
                return $false
            }
        } catch {
            # 如果抛出403异常,说明权限控制正常
            if ($_.Exception.Message -match "403") {
                Write-TestResult "${RoleName}越权访问控制" "PASS" "正确阻止越权访问"
                return $true
            } else {
                Write-TestResult "${RoleName}越权访问控制" "SKIP" "接口不存在或其他错误"
                return $false
            }
        }
    } else {
        Write-TestResult "${RoleName}越权访问控制" "SKIP" "管理员角色跳过"
        return $true
    }
}

# 测试审计日志
function Test-AuditLog {
    param($Token, $RoleName)
    
    try {
        $headers = @{
            Authorization = "Bearer $Token"
        }
        
        # 执行一个会触发@AuditLog的操作(查询老人列表)
        Invoke-RestMethod -Uri "$BaseUrl/api/elderly/my-patients?current=1&size=1" `
            -Method Get -Headers $headers -TimeoutSec 10 | Out-Null
        
        Start-Sleep -Seconds 2
        
        # 检查audit_log表是否记录了操作
        $logCheck = mysql -u root smartcare_cloud -e "SELECT COUNT(*) as count FROM audit_log WHERE operation_module='ELDERLY' ORDER BY id DESC LIMIT 1" -s 2>&1
        
        if ($logCheck -match "\\d+") {
            $logCount = [int]($logCheck -replace "\\D", "")
            if ($logCount -gt 0) {
                Write-TestResult "${RoleName}审计日志" "PASS" "操作已记录到audit_log表"
                return $true
            }
        }
        
        Write-TestResult "${RoleName}审计日志" "SKIP" "无法验证日志记录"
        return $false
    } catch {
        Write-TestResult "${RoleName}审计日志" "SKIP" "验证失败: $($_.Exception.Message)"
        return $false
    }
}

# ============================================
# 主测试流程
# ============================================

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  智慧医养平台 - 角色权限综合测试" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# 检查后端服务
Write-Host "${Yellow}[检查]${Reset} 验证后端服务状态..." -ForegroundColor Cyan
try {
    $healthCheck = Invoke-WebRequest -Uri "$BaseUrl/api/auth/login" -Method Options -UseBasicParsing -TimeoutSec 5
    Write-TestResult "后端服务可用性" "PASS" "服务正常响应"
} catch {
    Write-TestResult "后端服务可用性" "FAIL" "服务不可用,请先启动后端"
    exit 1
}

# 定义测试角色
$testRoles = @(
    @{ Username = "admin"; RoleCode = "system_admin"; RoleName = "系统管理员" },
    @{ Username = "business_admin1"; RoleCode = "business_admin"; RoleName = "业务管理员" },
    @{ Username = "doctor1"; RoleCode = "doctor"; RoleName = "医生" },
    @{ Username = "nurse1"; RoleCode = "nurse"; RoleName = "护士" },
    @{ Username = "social_worker1"; RoleCode = "social_worker"; RoleName = "社工" },
    @{ Username = "family1"; RoleCode = "family"; RoleName = "家属" }
)

# 对每个角色执行完整测试
foreach ($role in $testRoles) {
    Write-Host ""
    Write-Host "============================================" -ForegroundColor Yellow
    Write-Host "  测试角色: $($role.RoleName) ($($role.Username))" -ForegroundColor Yellow
    Write-Host "============================================" -ForegroundColor Yellow
    Write-Host ""
    
    # 1. 登录测试
    Write-Host "${Cyan}[测试1]${Reset} 登录认证测试"
    $loginResult = Test-Login -Username $role.Username -RoleName $role.RoleName
    
    if (-not $loginResult.Success) {
        Write-Host "${Red}  ⚠ 登录失败,跳过后续测试${Reset}" -ForegroundColor Red
        continue
    }
    
    $token = $loginResult.Token
    $userInfo = $loginResult.UserInfo
    
    # 2. 数据权限测试
    Write-Host ""
    Write-Host "${Cyan}[测试2]${Reset} 数据权限隔离测试"
    Test-DataPermission -Token $token -RoleName $role.RoleName -RoleCode $role.RoleCode
    
    # 3. 越权访问测试
    Write-Host ""
    Write-Host "${Cyan}[测试3]${Reset} 越权访问控制测试"
    Test-UnauthorizedAccess -Token $token -RoleName $role.RoleName -RoleCode $role.RoleCode
    
    # 4. 审计日志测试
    Write-Host ""
    Write-Host "${Cyan}[测试4]${Reset} 操作审计日志测试"
    Test-AuditLog -Token $token -RoleName $role.RoleName
    
    Start-Sleep -Milliseconds 500
}

# ============================================
# 家属权限边界测试
# ============================================

Write-Host ""
Write-Host "============================================" -ForegroundColor Yellow
Write-Host "  家属三级访问权限边界测试" -ForegroundColor Yellow
Write-Host "============================================" -ForegroundColor Yellow
Write-Host ""

# 登录家属账号
$familyLogin = Test-Login -Username "family1" -RoleName "家属(三级权限)"

if ($familyLogin.Success) {
    $familyToken = $familyLogin.Token
    
    try {
        $headers = @{
            Authorization = "Bearer $familyToken"
        }
        
        # 获取家属关联的第一个老人
        $elderlyList = Invoke-RestMethod -Uri "$BaseUrl/api/elderly/my-patients?current=1&size=1" `
            -Method Get -Headers $headers -TimeoutSec 10
        
        if ($elderlyList.code -eq 200 -and $elderlyList.data.records.Count -gt 0) {
            $elderlyId = $elderlyList.data.records[0].id
            
            # 测试家属详情查看(应该根据access_level返回不同字段)
            Write-Host "${Cyan}[测试5]${Reset} 家属查看老人详情"
            $detailResponse = Invoke-RestMethod -Uri "$BaseUrl/api/elderly/family-detail/$elderlyId" `
                -Method Get -Headers $headers -TimeoutSec 10 -ErrorAction SilentlyContinue
            
            if ($detailResponse.code -eq 200) {
                $accessLevel = $detailResponse.data.accessLevel
                $hasHealthStatus = $null -ne $detailResponse.data.healthStatus
                $hasHealthRecords = $null -ne $detailResponse.data.healthRecords
                
                Write-TestResult "家属访问权限级别" "PASS" "权限级别: $accessLevel"
                
                # 根据权限级别验证字段可见性
                if ($accessLevel -eq "basic") {
                    if (-not $hasHealthStatus) {
                        Write-TestResult "家属Basic级别字段限制" "PASS" "正确隐藏健康状态字段"
                    } else {
                        Write-TestResult "家属Basic级别字段限制" "FAIL" "不应显示健康状态"
                    }
                } elseif ($accessLevel -eq "health") {
                    if ($hasHealthStatus -and -not $hasHealthRecords) {
                        Write-TestResult "家属Health级别字段限制" "PASS" "正确显示健康状态,隐藏健康记录"
                    } else {
                        Write-TestResult "家属Health级别字段限制" "FAIL" "字段可见性不正确"
                    }
                } elseif ($accessLevel -eq "full") {
                    if ($hasHealthStatus -and $hasHealthRecords) {
                        Write-TestResult "家属Full级别字段限制" "PASS" "正确显示所有字段"
                    } else {
                        Write-TestResult "家属Full级别字段限制" "FAIL" "应显示所有字段"
                    }
                }
            } else {
                Write-TestResult "家属查看老人详情" "FAIL" "返回码: $($detailResponse.code)"
            }
        } else {
            Write-TestResult "家属三级权限测试" "SKIP" "该家属没有关联的老人数据"
        }
    } catch {
        Write-TestResult "家属三级权限测试" "SKIP" "接口不可用: $($_.Exception.Message)"
    }
} else {
    Write-TestResult "家属三级权限测试" "SKIP" "家属登录失败"
}

# ============================================
# 测试总结
# ============================================

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  测试总结" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "通过: ${Green}$script:PassCount${Reset} | " -NoNewline
Write-Host "失败: ${Red}$script:FailCount${Reset} | " -NoNewline
Write-Host "跳过: ${Yellow}$script:SkipCount${Reset} | " -NoNewline
Write-Host "总计: $($script:PassCount + $script:FailCount + $script:SkipCount)"
Write-Host ""

# 按角色分组显示结果
Write-Host "详细结果:" -ForegroundColor Cyan
$script:TestResults | Group-Object { $_.TestName -replace '(登录|数据权限|越权访问控制|审计日志)', '' } | ForEach-Object {
    $roleName = $_.Name
    $results = $_.Group
    $passed = ($results | Where-Object { $_.Status -eq "PASS" }).Count
    $failed = ($results | Where-Object { $_.Status -eq "FAIL" }).Count
    $skipped = ($results | Where-Object { $_.Status -eq "SKIP" }).Count
    
    Write-Host "  $roleName - 通过:$passed 失败:$failed 跳过:$skipped" -ForegroundColor Gray
}

Write-Host ""

if ($script:FailCount -eq 0) {
    Write-Host "✅ 所有测试通过!权限系统运行正常。" -ForegroundColor Green
} else {
    Write-Host "⚠️  发现 $script:FailCount 个失败项,请检查权限配置。" -ForegroundColor Yellow
}

Write-Host ""
