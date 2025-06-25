<template>
  <div class="permissions-container">
    <div class="header-section">
      <h2>权限管理</h2>
      <p>管理系统权限菜单，配置功能访问控制</p>
    </div>

    <!-- 操作栏 -->
    <div class="toolbar">
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增权限
        </el-button>
        <el-button @click="handleExpandAll">
          <el-icon><Expand /></el-icon>
          展开全部
        </el-button>
        <el-button @click="handleCollapseAll">
          <el-icon><Fold /></el-icon>
          收起全部
        </el-button>
      </div>
    </div>

    <!-- 权限树表格 -->
    <div class="table-container">
      <el-table
        ref="tableRef"
        v-loading="loading"
        :data="permissionList"
        row-key="id"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        :default-expand-all="defaultExpandAll"
        border
        style="width: 100%"
      >
        <el-table-column prop="name" label="权限名称" min-width="200">
          <template #default="{ row }">
            <div class="permission-name">
              <el-icon v-if="row.icon" class="permission-icon">
                <component :is="row.icon" />
              </el-icon>
              <span>{{ row.name }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column prop="code" label="权限代码" width="180" />
        
        <el-table-column prop="type" label="权限类型" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getPermissionTypeTag(row.type)">
              {{ getPermissionTypeName(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="path" label="路由路径" width="180" />
        
        <el-table-column prop="component" label="组件路径" width="200" />
        
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="createTime" label="创建时间" width="160" />
        
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="success" size="small" @click="handleAddChild(row)">
              新增子权限
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 权限表单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑权限' : '新增权限'"
      width="600px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="permissionForm"
        :rules="permissionRules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限名称" prop="name">
              <el-input v-model="permissionForm.name" placeholder="请输入权限名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限代码" prop="code">
              <el-input v-model="permissionForm.code" placeholder="请输入权限代码" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限类型" prop="type">
              <el-select v-model="permissionForm.type" placeholder="请选择权限类型" style="width: 100%">
                <el-option label="目录" value="catalog" />
                <el-option label="菜单" value="menu" />
                <el-option label="按钮" value="button" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="父级权限" prop="parentId">
              <el-tree-select
                v-model="permissionForm.parentId"
                :data="parentPermissionTree"
                :props="{ value: 'id', label: 'name', children: 'children' }"
                placeholder="请选择父级权限"
                style="width: 100%"
                clearable
                check-strictly
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20" v-if="permissionForm.type !== 'button'">
          <el-col :span="12">
            <el-form-item label="路由路径" prop="path">
              <el-input v-model="permissionForm.path" placeholder="请输入路由路径" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="permissionForm.component" placeholder="请输入组件路径" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="图标" prop="icon">
              <el-select v-model="permissionForm.icon" placeholder="请选择图标" style="width: 100%" clearable>
                <el-option
                  v-for="icon in iconList"
                  :key="icon.value"
                  :label="icon.label"
                  :value="icon.value"
                >
                  <div style="display: flex; align-items: center; gap: 8px;">
                    <el-icon><component :is="icon.value" /></el-icon>
                    <span>{{ icon.label }}</span>
                  </div>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="排序" prop="sort">
              <el-input-number v-model="permissionForm.sort" :min="0" placeholder="排序值" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="permissionForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="备注">
          <el-input 
            v-model="permissionForm.remark" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入备注信息" 
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { Expand, Fold, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { computed, onMounted, reactive, ref } from 'vue'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const isAddChild = ref(false)
const defaultExpandAll = ref(false)
const tableRef = ref()
const formRef = ref()
const permissionList = ref([])

// 权限表单
const permissionForm = reactive({
  id: null,
  parentId: null,
  name: '',
  code: '',
  type: 'menu',
  path: '',
  component: '',
  icon: '',
  sort: 0,
  status: 1,
  remark: ''
})

// 表单验证规则
const permissionRules = {
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入权限代码', trigger: 'blur' },
    { pattern: /^[A-Z_:]+$/, message: '权限代码只能包含大写字母、下划线和冒号', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择权限类型', trigger: 'change' }
  ],
  path: [
    { 
      validator: (rule, value, callback) => {
        if (permissionForm.type !== 'button' && !value) {
          callback(new Error('请输入路由路径'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}

// 图标列表
const iconList = ref([
  { label: '仪表板', value: 'Dashboard' },
  { label: '用户', value: 'User' },
  { label: '设置', value: 'Setting' },
  { label: '监控', value: 'Monitor' },
  { label: '文档', value: 'Document' },
  { label: '数据分析', value: 'DataAnalysis' },
  { label: '警告', value: 'Warning' },
  { label: 'CPU', value: 'Cpu' },
  { label: '列表', value: 'List' },
  { label: '饼图', value: 'PieChart' }
])

// 父级权限树（用于选择）
const parentPermissionTree = computed(() => {
  const buildTree = (list, parentId = null) => {
    return list
      .filter(item => item.parentId === parentId && item.type !== 'button')
      .map(item => ({
        id: item.id,
        name: item.name,
        children: buildTree(list, item.id)
      }))
  }
  
  return [
    { id: null, name: '根目录', children: buildTree(permissionList.value) }
  ]
})

/**
 * 页面加载时获取数据
 */
onMounted(() => {
  getPermissionList()
})

/**
 * 获取权限列表
 */
const getPermissionList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    const mockData = [
      {
        id: 1,
        parentId: null,
        name: '系统管理',
        code: 'SYSTEM',
        type: 'catalog',
        path: '/system',
        component: 'Layout',
        icon: 'Setting',
        sort: 1,
        status: 1,
        createTime: '2024-01-01 00:00:00',
        children: [
          {
            id: 11,
            parentId: 1,
            name: '用户管理',
            code: 'SYSTEM:USER',
            type: 'menu',
            path: '/system/users',
            component: 'system/users',
            icon: 'User',
            sort: 1,
            status: 1,
            createTime: '2024-01-01 00:00:00',
            children: [
              {
                id: 111,
                parentId: 11,
                name: '查看用户',
                code: 'SYSTEM:USER:VIEW',
                type: 'button',
                path: '',
                component: '',
                icon: '',
                sort: 1,
                status: 1,
                createTime: '2024-01-01 00:00:00'
              },
              {
                id: 112,
                parentId: 11,
                name: '新增用户',
                code: 'SYSTEM:USER:ADD',
                type: 'button',
                path: '',
                component: '',
                icon: '',
                sort: 2,
                status: 1,
                createTime: '2024-01-01 00:00:00'
              }
            ]
          },
          {
            id: 12,
            parentId: 1,
            name: '角色管理',
            code: 'SYSTEM:ROLE',
            type: 'menu',
            path: '/system/roles',
            component: 'system/roles',
            icon: 'Avatar',
            sort: 2,
            status: 1,
            createTime: '2024-01-01 00:00:00'
          }
        ]
      },
      {
        id: 2,
        parentId: null,
        name: '老人档案管理',
        code: 'ELDERLY',
        type: 'catalog',
        path: '/elderly',
        component: 'Layout',
        icon: 'User',
        sort: 2,
        status: 1,
        createTime: '2024-01-01 00:00:00',
        children: [
          {
            id: 21,
            parentId: 2,
            name: '老人档案列表',
            code: 'ELDERLY:LIST',
            type: 'menu',
            path: '/elderly/list',
            component: 'elderly/list',
            icon: 'List',
            sort: 1,
            status: 1,
            createTime: '2024-01-01 00:00:00'
          }
        ]
      }
    ]
    
    permissionList.value = mockData
  } catch (error) {
    ElMessage.error('获取权限列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

/**
 * 新增权限
 */
const handleAdd = () => {
  isEdit.value = false
  isAddChild.value = false
  resetForm()
  dialogVisible.value = true
}

/**
 * 新增子权限
 */
const handleAddChild = (row) => {
  isEdit.value = false
  isAddChild.value = true
  resetForm()
  permissionForm.parentId = row.id
  dialogVisible.value = true
}

/**
 * 编辑权限
 */
const handleEdit = (row) => {
  isEdit.value = true
  isAddChild.value = false
  Object.assign(permissionForm, { ...row })
  dialogVisible.value = true
}

/**
 * 删除权限
 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该权限吗？删除后子权限也会被删除！', '删除确认', {
      type: 'warning'
    })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('删除成功')
    getPermissionList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 展开全部
 */
const handleExpandAll = () => {
  defaultExpandAll.value = true
  // 重新渲染表格
  getPermissionList()
}

/**
 * 收起全部
 */
const handleCollapseAll = () => {
  defaultExpandAll.value = false
  // 重新渲染表格
  getPermissionList()
}

/**
 * 提交表单
 */
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
    
    submitLoading.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(isEdit.value ? '编辑成功' : '新增成功')
    dialogVisible.value = false
    getPermissionList()
  } catch (error) {
    ElMessage.error(isEdit.value ? '编辑失败' : '新增失败')
  } finally {
    submitLoading.value = false
  }
}

/**
 * 关闭弹窗
 */
const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

/**
 * 重置表单
 */
const resetForm = () => {
  Object.assign(permissionForm, {
    id: null,
    parentId: null,
    name: '',
    code: '',
    type: 'menu',
    path: '',
    component: '',
    icon: '',
    sort: 0,
    status: 1,
    remark: ''
  })
  
  if (formRef.value) {
    formRef.value.clearValidate()
  }
}

/**
 * 获取权限类型标签
 */
const getPermissionTypeTag = (type) => {
  const typeMap = {
    catalog: 'warning',
    menu: 'primary',
    button: 'success'
  }
  return typeMap[type] || 'info'
}

/**
 * 获取权限类型名称
 */
const getPermissionTypeName = (type) => {
  const typeMap = {
    catalog: '目录',
    menu: '菜单',
    button: '按钮'
  }
  return typeMap[type] || '未知'
}
</script>

<style lang="scss" scoped>
.permissions-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}

.header-section {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
  
  h2 {
    color: #303133;
    font-size: 24px;
    font-weight: 600;
    margin: 0 0 8px 0;
  }
  
  p {
    color: #606266;
    font-size: 14px;
    margin: 0;
  }
}

.toolbar {
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.table-container {
  margin-top: 20px;
}

.permission-name {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .permission-icon {
    color: #409eff;
  }
}

:deep(.el-table) {
  .el-table__header {
    th {
      background-color: #fafafa;
      color: #606266;
      font-weight: 600;
    }
  }
  
  .el-table__row {
    &.el-table__row--level-1 {
      background-color: #f8f9fa;
    }
    
    &.el-table__row--level-2 {
      background-color: #f0f9ff;
    }
  }
}

:deep(.el-dialog) {
  .el-dialog__header {
    padding: 20px 20px 10px;
    border-bottom: 1px solid #ebeef5;
  }
  
  .el-dialog__body {
    padding: 20px;
  }
}

:deep(.el-tree-select) {
  width: 100%;
}
</style>
