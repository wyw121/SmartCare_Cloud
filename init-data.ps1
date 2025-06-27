# 初始化健康预警数据
$headers = @{
    'Content-Type' = 'application/json'
}

Write-Host "正在初始化健康预警数据..."

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/temp/db-init/health-warning" -Method POST -Headers $headers
    Write-Host "初始化结果: $($response | ConvertTo-Json -Depth 10)"
} catch {
    Write-Host "初始化失败: $($_.Exception.Message)"
    Write-Host "错误详情: $($_.ErrorDetails.Message)"
}
