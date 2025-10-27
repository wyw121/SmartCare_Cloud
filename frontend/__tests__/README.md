# 前端测试页面说明

本目录包含**开发和调试专用**的测试页面，已从生产代码目录移动到测试目录。

## 📋 测试页面列表

### 1. test.vue
- **用途**: 基础功能测试
- **功能**: 测试组件渲染、数据绑定等基础功能

### 2. debug.vue
- **用途**: 调试工具页面
- **功能**: 查看状态、调试数据流

### 3. dev-tools.vue
- **用途**: 开发者工具集
- **功能**: 快速测试API、查看响应数据

### 4. chart-test.vue
- **用途**: 图表组件测试
- **功能**: 测试ECharts图表渲染和交互

### 5. permission-demo.vue
- **用途**: 权限系统演示
- **功能**: 展示权限控制效果

### 6. simple-test.vue
- **用途**: 简单功能测试
- **功能**: 快速验证单个功能点

### 7. doctor-simple-test.vue
- **用途**: 医生模块简单测试
- **功能**: 医生相关功能快速验证

### 8. user-switcher.vue
- **用途**: 用户角色切换工具
- **功能**: 快速切换不同角色进行测试

### 9. README.md
- **用途**: 测试页面说明文档

## 🚫 生产环境排除

这些测试页面已从路由配置中移除，不会在生产环境中加载。

### 原路由配置（已移除）
```javascript
// ❌ 已从 router/index.js 移除
{
  path: '/test',
  component: () => import('@/views/__tests__/test.vue')
}
```

## 🔧 如何使用测试页面

### 方法1: 临时添加路由（推荐）

在开发时临时在 `router/index.js` 中添加：

```javascript
// 仅开发环境，提交代码前删除此段
if (import.meta.env.DEV) {
  routes.unshift({
    path: '/test',
    component: () => import('../../__tests__/views/test.vue'),
    meta: { title: '测试页面', hidden: true }
  })
}
```

### 方法2: 直接在组件中导入

```vue
<script setup>
import TestComponent from '@/__tests__/views/test.vue'
</script>
```

### 方法3: 使用开发服务器代理

```javascript
// vite.config.js
export default {
  resolve: {
    alias: {
      '@tests': resolve(__dirname, '__tests__')
    }
  }
}
```

## 📦 生产构建排除

### Vite 配置示例
```javascript
// vite.config.js
export default defineConfig({
  build: {
    rollupOptions: {
      external: [
        // 排除测试文件
        /\/__tests__\//
      ]
    }
  }
})
```

## 🎯 最佳实践

### ✅ 推荐做法
1. **仅在开发分支使用测试页面**
2. **提交前检查路由配置，确保测试路由已移除**
3. **使用条件编译 `import.meta.env.DEV` 控制测试代码**
4. **生产打包前运行 `npm run build` 验证**

### ❌ 避免做法
1. ~~将测试路由提交到主分支~~
2. ~~在生产环境暴露测试接口~~
3. ~~使用测试页面进行生产数据操作~~

## 📝 迁移日志

**迁移时间**: 2025-10-27  
**原因**: 代码清理，将测试页面与生产代码分离  
**原路径**: `src/views/__tests__/`  
**新路径**: `__tests__/views/`  
**路由影响**: 已从 `router/index.js` 移除所有测试路由

## 🔍 检查清单

在提交代码前，请确认：

- [ ] `router/index.js` 中无测试路由
- [ ] 无 `import` 语句引用 `__tests__` 目录
- [ ] `npm run build` 构建成功
- [ ] 生产包中不包含测试文件（检查 `dist/` 目录）

## 🔗 相关文档

- [项目深度状态分析报告](../项目深度状态分析报告_最终版.md)
- [前端开发指南](../前端开发指南.md)
- [Vite 环境变量文档](https://vitejs.dev/guide/env-and-mode.html)

## 💡 提示

如果您经常需要使用测试页面，可以考虑：
1. 创建本地开发分支 `dev-with-tests`
2. 在该分支保留测试路由配置
3. 永不合并该分支到主分支
