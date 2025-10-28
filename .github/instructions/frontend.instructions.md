---
applyTo: "frontend/**/*.vue,frontend/**/*.js,frontend/**/*.ts"
---

# 前端开发特定指令 - Vue 3 + Element Plus

## 适用范围
本指令适用于 `frontend/` 目录下的所有Vue组件、JavaScript和TypeScript文件。

## 必须使用的技术栈

### 核心框架
- **Vue**: 3.4.0 - 使用 Composition API (`<script setup>`)
- **UI组件库**: Element Plus 2.4.4
- **构建工具**: Vite 5.0.10
- **状态管理**: Pinia 2.1.7
- **路由**: Vue Router 4.x
- **HTTP客户端**: Axios 1.6.x

## Vue组件开发规范

### 组件模板 - 列表页面
```vue
<template>
  <div class="container">
    <!-- 搜索表单 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="名称">
          <el-input v-model="searchForm.name" placeholder="请输入名称" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card class="toolbar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <el-table :data="tableData" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="名称" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        v-model:current-page="pagination.current"
        v-model:page-size="pagination.size"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleQuery"
        @current-change="handleQuery"
      />
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getList, create, update, remove } from '@/api/example'

// 响应式数据
const loading = ref(false)
const tableData = ref([])
const searchForm = reactive({
  name: ''
})
const pagination = reactive({
  current: 1,
  size: 20,
  total: 0
})

// 对话框
const dialogVisible = ref(false)
const dialogTitle = ref('新增')
const formRef = ref(null)
const form = reactive({
  id: null,
  name: ''
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入名称', trigger: 'blur' }
  ]
}

// 查询数据
const handleQuery = async () => {
  loading.value = true
  try {
    const { data } = await getList({
      current: pagination.current,
      size: pagination.size,
      ...searchForm
    })
    tableData.value = data.records
    pagination.total = data.total
  } catch (error) {
    ElMessage.error('查询失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  handleQuery()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, {
    name: ''
  })
  handleSearch()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该记录吗?', '提示', {
      type: 'warning'
    })
    await remove(row.id)
    ElMessage.success('删除成功')
    handleQuery()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

// 提交表单
const handleSubmit = async () => {
  const valid = await formRef.value.validate()
  if (!valid) return
  
  try {
    if (form.id) {
      await update(form.id, form)
      ElMessage.success('修改成功')
    } else {
      await create(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    handleQuery()
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 对话框关闭
const handleDialogClose = () => {
  formRef.value?.resetFields()
  Object.assign(form, {
    id: null,
    name: ''
  })
}

// 初始化
onMounted(() => {
  handleQuery()
})
</script>

<style scoped>
.container {
  padding: 20px;
}

.search-card,
.toolbar {
  margin-bottom: 20px;
}

.el-pagination {
  margin-top: 20px;
  justify-content: flex-end;
}
</style>
```

## API接口封装规范

### API模块模板
```javascript
import request from '@/utils/request'

/**
 * 获取列表
 */
export function getList(params) {
  return request({
    url: '/api/example/list',
    method: 'get',
    params
  })
}

/**
 * 根据ID获取详情
 */
export function getById(id) {
  return request({
    url: `/api/example/${id}`,
    method: 'get'
  })
}

/**
 * 新增
 */
export function create(data) {
  return request({
    url: '/api/example',
    method: 'post',
    data
  })
}

/**
 * 更新
 */
export function update(id, data) {
  return request({
    url: `/api/example/${id}`,
    method: 'put',
    data
  })
}

/**
 * 删除
 */
export function remove(id) {
  return request({
    url: `/api/example/${id}`,
    method: 'delete'
  })
}
```

## Pinia Store规范

### Store模块模板
```javascript
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useExampleStore = defineStore('example', () => {
  // 状态
  const data = ref([])
  const loading = ref(false)
  
  // 计算属性
  const count = computed(() => data.value.length)
  
  // 方法
  const fetchData = async () => {
    loading.value = true
    try {
      // API调用
      const response = await getList()
      data.value = response.data
    } finally {
      loading.value = false
    }
  }
  
  const addItem = (item) => {
    data.value.push(item)
  }
  
  const removeItem = (id) => {
    const index = data.value.findIndex(item => item.id === id)
    if (index > -1) {
      data.value.splice(index, 1)
    }
  }
  
  return {
    data,
    loading,
    count,
    fetchData,
    addItem,
    removeItem
  }
})
```

## 命名规范

### 文件命名
- **组件文件**: kebab-case (例: `elderly-list.vue`, `health-warning.vue`)
- **API文件**: camelCase (例: `elderly.js`, `healthWarning.js`)
- **Store文件**: camelCase (例: `user.js`, `elderly.js`)

### 代码命名
- **组件名**: PascalCase (例: `ElderlyList`, `HealthWarning`)
- **变量/函数**: camelCase (例: `elderlyData`, `fetchElderlyList`)
- **常量**: UPPER_SNAKE_CASE (例: `API_BASE_URL`, `MAX_PAGE_SIZE`)
- **事件处理函数**: handle前缀 (例: `handleClick`, `handleSubmit`)

## 开发要求

### 1. Composition API
- **必须使用** `<script setup>` 语法
- **禁止使用** Options API (`data`, `methods`, `computed` 选项)
- 使用 `ref` 和 `reactive` 定义响应式数据
- 使用 `computed` 定义计算属性
- 使用生命周期组合式函数 (`onMounted`, `onUnmounted` 等)

### 2. 组件组织
- 按功能模块组织组件
- 公共组件放在 `src/components/` 目录
- 页面组件放在 `src/views/` 目录
- 组件应该单一职责,避免过于复杂

### 3. 样式规范
- 使用 `scoped` 样式避免污染全局
- 使用 Element Plus 的主题变量
- 避免使用内联样式
- 使用CSS变量定义颜色、间距等

### 4. 性能优化
- 合理使用 `v-show` 和 `v-if`
- 列表渲染使用 `:key` 绑定唯一标识
- 大列表使用虚拟滚动
- 图片使用懒加载

### 5. 错误处理
- API调用使用 try-catch 捕获异常
- 使用 `ElMessage` 显示操作结果
- 使用 `ElMessageBox` 进行确认操作
- 表单使用 `rules` 进行验证

### 6. 代码注释
- 复杂业务逻辑添加注释
- API函数添加JSDoc注释
- 组件props添加描述

## 禁止事项

❌ **禁止使用Options API**: 必须使用Composition API  
❌ **禁止直接修改props**: 使用 `emit` 向父组件传递事件  
❌ **禁止在template中写复杂逻辑**: 应提取为computed或method  
❌ **禁止硬编码API地址**: 必须使用统一的request封装  
❌ **禁止忽略错误处理**: 所有API调用必须有错误处理  

## 必须遵循

✅ **必须使用Composition API** (`<script setup>`)  
✅ **必须使用Element Plus组件**  
✅ **必须进行表单验证**  
✅ **必须处理loading状态**  
✅ **必须使用统一的API封装**  
✅ **必须使用Pinia进行状态管理** (跨组件共享状态时)  
