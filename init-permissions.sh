#!/bin/bash
# 智慧医养平台权限系统初始化脚本

echo "🏥 智慧医养大数据公共服务平台"
echo "==================================="
echo "正在初始化三角色权限管理系统..."
echo ""

# 检查数据库连接
echo "📋 检查数据库连接..."
mysql -u root -p123456 -e "SELECT 1;" 2>/dev/null
if [ $? -ne 0 ]; then
    echo "❌ 数据库连接失败，请检查MySQL服务是否启动"
    exit 1
fi

echo "✅ 数据库连接成功"
echo ""

# 执行权限系统初始化SQL
echo "🔧 执行权限系统初始化..."
mysql -u root -p123456 smartcare_cloud < "$(dirname "$0")/backend/src/main/resources/sql/init_permissions.sql"

if [ $? -eq 0 ]; then
    echo "✅ 权限系统初始化完成"
    echo ""
    
    # 显示权限统计
    echo "📊 权限统计信息："
    mysql -u root -p123456 smartcare_cloud -e "
        SELECT '角色数量' as 统计项, COUNT(*) as 数量 FROM sys_role WHERE status = 1 AND is_deleted = 0
        UNION ALL
        SELECT '权限数量' as 统计项, COUNT(*) as 数量 FROM sys_permission WHERE status = 1 AND is_deleted = 0
        UNION ALL
        SELECT '角色权限关联' as 统计项, COUNT(*) as 数量 FROM sys_role_permission;
    "
    
    echo ""
    echo "🎭 默认角色信息："
    mysql -u root -p123456 smartcare_cloud -e "
        SELECT 
            role_code as '角色编码',
            role_name as '角色名称', 
            description as '描述',
            (SELECT COUNT(*) FROM sys_role_permission WHERE role_id = sys_role.id) as '权限数量'
        FROM sys_role 
        WHERE status = 1 AND is_deleted = 0 
        ORDER BY sort_order;
    "
    
    echo ""
    echo "👥 测试账户信息："
    echo "系统管理员: admin / 123456"
    echo "医生账户: doctor1 / 123456"  
    echo "家属账户: family1 / 123456"
    echo ""
    echo "🌐 权限测试页面: permission-system-test.html"
    echo "💻 前端开发服务器: npm run dev (端口3001)"
    echo "🔗 后端API服务器: mvn spring-boot:run (端口8080)"
    echo ""
    echo "🎉 权限系统初始化完成！"
    
else
    echo "❌ 权限系统初始化失败"
    exit 1
fi
