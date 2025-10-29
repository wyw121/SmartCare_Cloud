# 修复 SQL 文件,移除不存在的字段

$content = Get-Content -Path "basic_data_new.sql" -Raw

# 1. 修复 INSERT INTO sys_user 的列定义
$content = $content -replace 'INSERT INTO sys_user \(id, username, password, real_name, gender, phone, email, role_code, role_name, organization_id, status\)', 'INSERT INTO sys_user (id, username, password, real_name, role_code, organization_id, status)'

# 2. 修复数据行:移除 gender, phone, email, role_name 字段
# 匹配格式: (id, 'user', 'pass', 'name', gender, 'phone', 'email', 'role_code', 'role_name', org_id, status)
# 替换为: (id, 'user', 'pass', 'name', 'role_code', org_id, status)
$lines = $content -split "`n"
$newLines = @()

foreach ($line in $lines) {
    if ($line -match '^\(\d+,') {
        # 匹配数据行并提取需要的字段
        if ($line -match '^\((\d+), ''([^'']+)'', ''([^'']+)'', ''([^'']+)'', \d, ''\d+'', ''[^'']*'', ''([^'']+)'', ''[^'']*'', (NULL|\d+), (0|1)\)') {
            $id = $Matches[1]
            $username = $Matches[2]
            $password = $Matches[3]
            $realName = $Matches[4]
            $roleCode = $Matches[5]
            $orgId = $Matches[6]
            $status = $Matches[7]
            
            $newLine = "($id, '$username', '$password', '$realName', '$roleCode', $orgId, $status)"
            $newLines += $newLine
        } else {
            $newLines += $line
        }
    } else {
        $newLines += $line
    }
}

$newContent = $newLines -join "`n"
$newContent | Set-Content -Path "basic_data_fixed.sql" -NoNewline

Write-Host "✓ SQL文件已修复并保存为 basic_data_fixed.sql" -ForegroundColor Green
