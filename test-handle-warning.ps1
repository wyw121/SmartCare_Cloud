Write-Host "Testing handle warning API..."

$body = @{
    handleRemark = "已联系家属，老人血压已稳定，建议定期监测"
    handlerName = "医生张三"
} | ConvertTo-Json

Write-Host "Request body: $body"

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/health-warning/1/handle" -Method PUT -ContentType "application/json" -Body $body
    Write-Host "Success!"
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}
