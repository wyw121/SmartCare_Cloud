# 添加行尾逗号

$content = Get-Content -Path "basic_data_fixed.sql" -Raw

# 在所有以)结尾且下一行以(开头的行添加逗号
$newline = [Environment]::NewLine
$content = $content -replace '(\))\s*\r?\n\s*(\()', "`$1,$newline`$2"

$content | Set-Content -Path "basic_data.sql" -NoNewline

Write-Host "✓ 已添加行尾逗号" -ForegroundColor Green
