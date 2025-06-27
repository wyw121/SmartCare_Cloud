Write-Host "Testing handle warning API..."

$body = @{
    handleRemark = "Contacted family, blood pressure is now stable, regular monitoring recommended"
    handlerName = "Dr. Zhang"
} | ConvertTo-Json

Write-Host "Request body: $body"

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/health-warning/1/handle" -Method PUT -ContentType "application/json" -Body $body
    Write-Host "Success!"
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}
