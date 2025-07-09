// 家属个人中心调试脚本
// 在浏览器控制台中运行此脚本来测试家属功能

console.log('🏥 智慧医养平台 - 家属个人中心调试脚本');

// 切换到家属角色的函数
window.switchToFamily = function() {
    console.log('🔄 切换到家属角色...');
    
    const familyData = {
        id: 3,
        username: 'family',
        password: '123456',
        name: '李家属',
        role: 'family',
        roleText: '家属',
        permissions: [
            'dashboard:view',
            'elderly:view',
            'health:view',
            'health-warning:view',
            'report:view'
        ],
        avatar: '',
        relationship: '儿子',
        elderlyIds: [1, 2],
        phone: '13800138002',
        email: 'family@smartcare.com',
        description: '家属用户，可查看关联老人的健康信息和预警',
        features: {
            elderly: ['view'],
            health: ['view'],
            report: ['view']
        }
    };
    
    const token = `dev_switch_token_family_${Date.now()}`;
    
    // 存储到localStorage
    localStorage.setItem('token', token);
    localStorage.setItem('userInfo', JSON.stringify(familyData));
    
    console.log('✅ 家属数据已设置:', familyData);
    console.log('🔑 Token已设置:', token);
    
    // 刷新页面
    window.location.reload();
};

// 检查当前状态的函数
window.checkUserState = function() {
    console.log('=== 当前用户状态 ===');
    console.log('Token:', localStorage.getItem('token'));
    console.log('UserInfo:', JSON.parse(localStorage.getItem('userInfo') || '{}'));
    
    // 如果有Vue应用的store，也打印store状态
    if (window.__PINIA__ && window.__PINIA__.state && window.__PINIA__.state.value) {
        const userStore = window.__PINIA__.state.value.user;
        if (userStore) {
            console.log('Store State:', {
                token: userStore.token,
                userInfo: userStore.userInfo,
                isLoggedIn: !!userStore.token
            });
        }
    }
    console.log('==================');
};

// 清除登录状态的函数
window.clearUserState = function() {
    console.log('🗑️ 清除用户状态...');
    localStorage.removeItem('token');
    localStorage.removeItem('userInfo');
    console.log('✅ 用户状态已清除');
    window.location.reload();
};

// 自动执行状态检查
checkUserState();

console.log('📝 可用的调试命令:');
console.log('- switchToFamily() : 切换到家属角色');
console.log('- checkUserState() : 检查当前用户状态');
console.log('- clearUserState() : 清除用户状态');
