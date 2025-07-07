# 智慧医养平台权限系统初始化脚本 (PowerShell版本)

Write-Host '🏥 智慧医养大数据公共服务平台' -ForegroundColor Cyan
Write-Host '===================================' -ForegroundColor Cyan
Write-Host '正在初始化三角色权限管理系统...' -ForegroundColor Green
Write-Host ''

# 检查MySQL命令是否可用
$mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue
if (-not $mysqlPath) {
    Write-Host '❌ 未找到 mysql 命令，请确保 MySQL 已安装并添加到环境变量' -ForegroundColor Red
    Write-Host '💡 提示：如果使用 XAMPP，请添加 C:\xampp\mysql\bin 到系统 PATH' -ForegroundColor Yellow
    exit 1
}

# 检查数据库连接
Write-Host '📋 检查数据库连接...' -ForegroundColor Yellow
try {
    $result = mysql -u root -p123456 -e 'SELECT 1;' 2>$null
    if ($LASTEXITCODE -ne 0) {
        throw '数据库连接失败'
    }
    Write-Host '✅ 数据库连接成功' -ForegroundColor Green
} catch {
    Write-Host '❌ 数据库连接失败，请检查：' -ForegroundColor Red
    Write-Host '   1. MySQL 服务是否启动' -ForegroundColor Yellow
    Write-Host '   2. 用户名密码是否正确 (当前使用: root/123456)' -ForegroundColor Yellow
    Write-Host '   3. smartcare_cloud 数据库是否存在' -ForegroundColor Yellow
    exit 1
}

Write-Host ''

# 获取脚本所在目录
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$sqlFile = Join-Path $scriptDir 'backend\src\main\resources\sql\smart_care_permissions_system.sql'

# 检查SQL文件是否存在
if (-not (Test-Path $sqlFile)) {
    Write-Host "❌ 权限初始化SQL文件不存在: $sqlFile" -ForegroundColor Red
    exit 1
}

# 执行权限系统初始化SQL
Write-Host '🔧 执行权限系统初始化...' -ForegroundColor Yellow
try {
    $result = mysql -u root -p123456 smartcare_cloud -e "SOURCE `"$sqlFile`";" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host '✅ 权限系统初始化完成' -ForegroundColor Green
    } else {
        throw "SQL执行失败: $result"
    }
} catch {
    Write-Host "❌ 权限系统初始化失败: $_" -ForegroundColor Red
    exit 1
}

Write-Host ''

# 显示权限统计
Write-Host '📊 权限统计信息：' -ForegroundColor Cyan
mysql -u root -p123456 smartcare_cloud -e @'
SELECT '角色数量' as 统计项, COUNT(*) as 数量 FROM sys_role WHERE status = 1 AND is_deleted = 0
UNION ALL
SELECT '权限数量' as 统计项, COUNT(*) as 数量 FROM sys_permission WHERE status = 1 AND is_deleted = 0
UNION ALL
SELECT '角色权限关联' as 统计项, COUNT(*) as 数量 FROM sys_role_permission;
'@

Write-Host ''
Write-Host '🎭 默认角色信息：' -ForegroundColor Cyan
mysql -u root -p123456 smartcare_cloud -e @'
SELECT
    role_code as '角色编码',
    role_name as '角色名称',
    description as '描述',
    (SELECT COUNT(*) FROM sys_role_permission WHERE role_id = sys_role.id) as '权限数量'
FROM sys_role
WHERE status = 1 AND is_deleted = 0
ORDER BY sort_order;
'@

Write-Host ''
Write-Host '👥 测试账户信息：' -ForegroundColor Green
Write-Host '   系统管理员: admin / 123456' -ForegroundColor White
Write-Host '   医生账户: doctor1 / 123456' -ForegroundColor White
Write-Host '   家属账户: family1 / 123456' -ForegroundColor White

Write-Host ''
Write-Host '🌐 权限测试页面: permission-system-test.html' -ForegroundColor Yellow
Write-Host '💻 前端开发服务器: npm run dev (端口3001)' -ForegroundColor Yellow
Write-Host '🔗 后端API服务器: mvn spring-boot:run (端口8080)' -ForegroundColor Yellow

Write-Host ''
Write-Host '🎉 权限系统初始化完成！' -ForegroundColor Green
Write-Host ''
Write-Host '接下来可以：' -ForegroundColor Cyan
Write-Host '1. 启动后端服务: cd backend && mvn spring-boot:run' -ForegroundColor White
Write-Host '2. 启动前端服务: cd frontend && npm run dev' -ForegroundColor White
Write-Host '3. 访问系统: http://localhost:3001' -ForegroundColor White
Write-Host '4. 测试权限: 用浏览器打开 permission-system-test.html' -ForegroundColor White
