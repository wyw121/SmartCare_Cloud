#!/usr/bin/env pwsh
# 为所有实体类添加Lombok @Data注解的脚本

$entityFiles = @(
    "User.java",
    "Role.java", 
    "Permission.java",
    "HealthWarning.java",
    "HealthRecord.java",
    "FamilyElderlyRelation.java",
    "Equipment.java",
    "Doctor.java"
)

$entityPath = "d:\repositories\SmartCare_Cloud\backend\src\main\java\com\smartcare\cloud\entity"

foreach ($file in $entityFiles) {
    $filePath = Join-Path $entityPath $file
    
    if (Test-Path $filePath) {
        Write-Host "处理文件: $file"
        
        $content = Get-Content $filePath -Raw -Encoding UTF8
        
        # 检查是否已有@Data注解
        if ($content -match '@Data') {
            Write-Host "  ✓ 已有@Data注解,跳过" -ForegroundColor Green
            continue
        }
        
        # 检查是否已导入Lombok
        if ($content -notmatch 'import lombok\.Data;') {
            # 在imports后添加lombok导入
            $content = $content -replace '(import com\.baomidou\.mybatisplus\.annotation\.\w+;)(\s+)', "`$1`n`$2import lombok.Data;`n"
        }
        
        # 在类定义前添加@Data注解
        $content = $content -replace '(@TableName\("[\w_]+"\))(\s+)(public class)', "`$1`n@Data`n`$3"
        
        # 保存文件
        Set-Content -Path $filePath -Value $content -Encoding UTF8 -NoNewline
        
        Write-Host "  ✓ 已添加@Data注解" -ForegroundColor Green
    } else {
        Write-Host "  ✗ 文件不存在: $file" -ForegroundColor Red
    }
}

Write-Host "`n完成!" -ForegroundColor Cyan
