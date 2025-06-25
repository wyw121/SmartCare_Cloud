# 智慧医养平台 RBAC 权限管理系统

## 系统概述

本系统实现了基于角色的访问控制（RBAC），支持三类用户角色：
- **管理员（Admin）**: 拥有系统全部权限
- **医生（Doctor）**: 拥有医疗管理相关权限
- **家属（Family）**: 拥有查看权限

## 角色权限配置

### 1. 管理员（admin）
```javascript
{
  name: '系统管理员',
  permissions: ['*'],  // 拥有所有权限
  routes: ['dashboard', 'elderly', 'doctor', 'health-warning', 'reports', 'system', 'profile'],
  description: '拥有系统全部权限'
}
```

### 2. 医生（doctor）
```javascript
{
  name: '医生',
  permissions: [
    'elderly:view', 'elderly:edit',
    'health:manage', 'health:view', 'health:edit',
    'report:view', 'report:export',
    'dashboard:view'
  ],
  routes: ['dashboard', 'elderly', 'health-warning', 'reports', 'profile'],
  description: '医疗管理相关权限'
}
```

### 3. 家属（family）
```javascript
{
  name: '家属',
  permissions: [
    'elderly:view',
    'health:view',
    'report:view'
  ],
  routes: ['dashboard', 'elderly', 'health-warning', 'profile'],
  description: '查看老人信息和健康状况'
}
```

## 快速登录（开发模式）

在开发环境中，登录页面提供了三种角色的快速登录功能：

### 预设账号信息
| 角色 | 用户名 | 密码 | 描述 |
|------|--------|------|------|
| 管理员 | admin | 123456 | 系统管理员，拥有全部权限 |
| 医生 | doctor | 123456 | 医生角色，拥有医疗管理权限 |
| 家属 | family | 123456 | 家属角色，只有查看权限 |

### 使用方法
1. 访问登录页面 `/login`
2. 点击对应的角色卡片即可自动登录
3. 或者手动输入用户名和密码登录

## 权限控制实现

### 1. 权限工具函数

```javascript
// 检查权限
import { hasPermission, hasRole, isAdmin, isDoctor, isFamily } from '@/utils/permission'

// 使用示例
if (hasPermission('elderly:edit')) {
  // 用户有编辑老人信息的权限
}

if (hasRole('admin')) {
  // 用户是管理员
}

if (isDoctor()) {
  // 用户是医生
}
```

### 2. 权限指令

在模板中使用 `v-permission` 和 `v-role` 指令：

```vue
<!-- 基于权限控制显示 -->
<el-button v-permission="'elderly:edit'">编辑</el-button>
<el-button v-permission="['elderly:edit', 'elderly:delete']">批量操作</el-button>

<!-- 基于角色控制显示 -->
<el-button v-role="'admin'">管理员功能</el-button>
<el-button v-role="['admin', 'doctor']">医生功能</el-button>
```

### 3. 路由守卫

系统自动检查用户是否有访问特定页面的权限：

```javascript
// 路由守卫会自动验证
router.beforeEach((to, from, next) => {
  const userRole = userStore.userRole
  
  if (!canAccessRoute(to.name, userRole)) {
    next('/') // 重定向到首页
  } else {
    next() // 允许访问
  }
})
```

### 4. 菜单过滤

侧边栏菜单根据用户角色自动过滤：

```javascript
// 菜单会根据用户角色自动显示/隐藏
const routes = computed(() => {
  const userRole = userStore.userRole
  return filterMenusByRole(menuItems, userRole)
})
```

## 页面权限示例

### 医生列表页面权限控制

```vue
<!-- 新增按钮：只有管理员和医生可以看到 -->
<el-button v-permission="'doctor:add'">新增医生</el-button>

<!-- 编辑按钮：只有管理员可以看到 -->
<el-button v-permission="'doctor:edit'">编辑</el-button>

<!-- 删除按钮：只有管理员可以看到 -->
<el-button v-permission="'doctor:delete'">删除</el-button>
```

### 老人管理页面权限控制

```vue
<!-- 家属可以查看，医生和管理员可以编辑 -->
<el-button v-permission="'elderly:view'">查看详情</el-button>
<el-button v-permission="'elderly:edit'">编辑信息</el-button>
```

## 权限演示页面

访问 `/permission-demo` 查看完整的权限演示，包括：

1. **角色权限对比**: 不同角色可见的功能按钮
2. **权限指令演示**: v-permission 和 v-role 指令效果
3. **快速角色切换**: 开发模式下的角色切换功能
4. **API权限测试**: 模拟API权限验证

## 开发指南

### 1. 添加新的权限

在 `utils/permission.js` 中的 `ROLE_PERMISSIONS` 配置中添加：

```javascript
export const ROLE_PERMISSIONS = {
  admin: {
    permissions: ['*'], // 管理员拥有所有权限
  },
  doctor: {
    permissions: [
      'elderly:view', 'elderly:edit',
      'new:permission', // 添加新权限
    ],
  }
}
```

### 2. 添加新的页面路由

在路由配置中添加权限要求：

```javascript
{
  path: '/new-page',
  name: 'NewPage',
  component: () => import('@/views/new-page.vue'),
  meta: { 
    title: '新页面',
    roles: ['admin', 'doctor'], // 指定可访问的角色
    permissions: ['new:permission'] // 指定需要的权限
  }
}
```

### 3. 在组件中使用权限控制

```vue
<template>
  <!-- 基于权限显示内容 -->
  <div v-permission="'feature:access'">
    有权限才能看到的内容
  </div>
  
  <!-- 基于角色显示内容 -->
  <div v-role="['admin', 'doctor']">
    管理员和医生才能看到的内容
  </div>
</template>

<script setup>
import { hasPermission, hasRole } from '@/utils/permission'

// 在逻辑中判断权限
if (hasPermission('feature:access')) {
  // 执行需要权限的操作
}
</script>
```

## 生产环境配置

在生产环境中，建议：

1. **关闭开发模式快速登录**:
   ```javascript
   const isDev = ref(false) // 在 login/index.vue 中设置为 false
   ```

2. **接入真实的认证系统**:
   - 替换 mock 登录为真实 API
   - 实现 JWT token 验证
   - 从后端获取用户权限信息

3. **加强安全性**:
   - 添加路由级别的权限验证
   - 实现细粒度的 API 权限控制
   - 添加操作日志记录

## 故障排除

### 1. 权限不生效
- 检查用户是否已登录
- 确认权限配置是否正确
- 查看浏览器控制台是否有错误

### 2. 菜单不显示
- 确认用户角色是否在允许的路由列表中
- 检查 `filterMenusByRole` 函数逻辑

### 3. 路由访问被拒绝
- 确认路由守卫配置
- 检查 `canAccessRoute` 函数返回值

## 后续扩展

1. **细粒度权限控制**: 基于资源的权限控制（如只能查看自己负责的老人）
2. **动态权限**: 支持运行时动态修改用户权限
3. **权限继承**: 支持角色权限继承机制
4. **审计日志**: 记录所有权限相关的操作日志
