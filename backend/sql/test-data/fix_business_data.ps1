# 修复 business_data.sql 文件,移除不存在的字段

$content = Get-Content -Path "business_data.sql" -Raw

# 修复 health_record 表的 INSERT 语句
# 移除: record_time, blood_oxygen, recorder_id
$content = $content -replace 'INSERT INTO health_record \(elderly_id, record_date, record_time, record_type, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar, blood_oxygen, recorder_id\)', 'INSERT INTO health_record (elderly_id, record_date, record_type, blood_pressure_high, blood_pressure_low, heart_rate, temperature, blood_sugar)'

# 修复数据行,移除 record_time, blood_oxygen, recorder_id
# 原格式: (elderly_id, 'date', 'time', 'type', bph, bpl, hr, temp, bs, bo, recorder_id)
# 新格式: (elderly_id, 'date', 'type', bph, bpl, hr, temp, bs)
$content = $content -replace "\((\d+), '([^']+)', '\d+:\d+:\d+', '([^']+)', (\d+), (\d+), (\d+), ([\d.]+), ([\d.]+), \d+, \d+\)", '($1, ''$2'', ''$3'', $4, $5, $6, $7, $8)'

$content | Set-Content -Path "business_data_fixed.sql" -NoNewline

Write-Host "✓ business_data.sql 修复完成!" -ForegroundColor Green
