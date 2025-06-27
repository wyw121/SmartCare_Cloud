Write-Host "Testing health warning page API..."

$body = @{
    pageNum = 1
    pageSize = 10
} | ConvertTo-Json

Write-Host "Request body: $body"

try {
    $response = Invoke-RestMethod -Uri "http://localhost:8080/api/health-warning/page" -Method POST -ContentType "application/json" -Body $body
    Write-Host "Success!"
    $response | ConvertTo-Json -Depth 10
} catch {
    Write-Host "Error: $($_.Exception.Message)"
}
