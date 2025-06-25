# 前端Vue组件生成

## 目标
为智慧医养平台生成标准的Vue 3组件，使用Composition API

## 必需信息
如果未提供，请询问：
- 组件名称和功能
- 需要的数据字段
- 交互操作类型

## Vue组件开发规范

### 组件结构
```vue
<template>
  <!-- 使用Element Plus组件 -->
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
// API导入

// 响应式数据定义
// 方法定义
// 生命周期钩子
</script>

<style scoped>
/* 组件样式 */
</style>
```

### 设计要求
- 使用Element Plus组件库
- 遵循Material Design设计风格
- 响应式布局，支持移动端
- 美观现代的UI界面

### 数据管理
- 使用 `ref()` 定义响应式数据
- 使用 `reactive()` 定义复杂对象
- 合理使用 `computed()` 计算属性
- 使用 `watch()` 监听数据变化

### API交互
- 使用axios进行HTTP请求
- 统一的错误处理机制
- 加载状态管理
- 成功/失败消息提示

### 表单处理
- 使用Element Plus的Form组件
- 完整的表单验证规则
- 友好的错误提示
- 支持重置和提交操作

### 表格功能
- 支持分页、排序、筛选
- 批量操作功能
- 导出功能
- 自适应列宽

### 权限控制
- 基于用户角色显示/隐藏功能
- 按钮级权限控制
- 菜单访问权限

## 生成要求
1. 完整的组件模板代码
2. 包含错误处理和加载状态
3. 美观的UI设计
4. 完整的功能实现
5. 适当的注释说明
