# 修复 basic_data.sql 文件,删除不存在的字段值

$content = Get-Content -Path "basic_data.sql" -Raw

# 替换 sys_user INSERT 语句中的字段值
# 原格式: (id, 'username', 'password', 'real_name', gender, 'phone', 'email', 'role_code', 'role_name', org_id, status)
# 新格式: (id, 'username', 'password', 'real_name', 'role_code', org_id, status)
$content = $content -replace "\((\d+), '([^']+)', '([^']+)', '([^']+)', \d, '\d+', '[^']+', '([^']+)', '[^']+', (NULL|\d+), 1\)", "(`$1, '`$2', '`$3', '`$4', '`$5', `$6, 1)"

$content | Set-Content -Path "basic_data.sql" -NoNewline

Write-Host "✓ basic_data.sql 修复完成!" -ForegroundColor Green
