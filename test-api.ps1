# 测试健康预警API
$headers = @{
    'Content-Type' = 'application/json'
}

$body = @{
    pageNum = 1
    pageSize = 10
} | ConvertTo-Json

Write-Host "正在测试健康预警分页API..."
Write-Host "请求体: $body"

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/health-warning/page" -Method POST -Headers $headers -Body $body
    Write-Host "API响应: $($response | ConvertTo-Json -Depth 10)"
} catch {
    Write-Host "API调用失败: $($_.Exception.Message)"
    Write-Host "错误详情: $($_.ErrorDetails.Message)"
}
