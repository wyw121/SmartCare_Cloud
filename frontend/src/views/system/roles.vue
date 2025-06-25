<template>
  <div class="roles-container">
    <div class="header-section">
      <h2>角色管理</h2>
      <p>管理系统角色权限，配置用户访问控制</p>
    </div>

    <!-- 搜索和操作栏 -->
    <div class="toolbar">
      <div class="search-box">
        <el-input
          v-model="searchForm.keyword"
          placeholder="请输入角色名称搜索"
          style="width: 300px"
          clearable
          @keyup.enter="handleSearch"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
      
      <div class="action-buttons">
        <el-button type="primary" @click="handleAdd">
          <el-icon><Plus /></el-icon>
          新增角色
        </el-button>
      </div>
    </div>

    <!-- 角色列表 -->
    <div class="table-container">
      <el-table
        v-loading="loading"
        :data="roleList"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column prop="id" label="角色ID" width="80" align="center" />
        <el-table-column prop="name" label="角色名称" width="150" />
        <el-table-column prop="code" label="角色代码" width="150" />
        <el-table-column prop="description" label="角色描述" min-width="200" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="280" align="center" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              编辑
            </el-button>
            <el-button type="success" size="small" @click="handleSetPermissions(row)">
              权限配置
            </el-button>
            <el-button 
              :type="row.status === 1 ? 'warning' : 'success'" 
              size="small" 
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="handleDelete(row)"
              :disabled="row.code === 'SUPER_ADMIN'"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 角色表单弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? '编辑角色' : '新增角色'"
      width="500px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="formRef"
        :model="roleForm"
        :rules="roleRules"
        label-width="100px"
      >
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="roleForm.name" placeholder="请输入角色名称" />
        </el-form-item>
        
        <el-form-item label="角色代码" prop="code">
          <el-input 
            v-model="roleForm.code" 
            placeholder="请输入角色代码" 
            :disabled="isEdit"
          />
        </el-form-item>
        
        <el-form-item label="角色描述" prop="description">
          <el-input 
            v-model="roleForm.description" 
            type="textarea" 
            :rows="3" 
            placeholder="请输入角色描述" 
          />
        </el-form-item>
        
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="roleForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="handleDialogClose">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">
          确定
        </el-button>
      </template>
    </el-dialog>

    <!-- 权限配置弹窗 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="权限配置"
      width="800px"
      :before-close="handlePermissionDialogClose"
    >
      <div class="permission-header">
        <h4>为角色 "{{ currentRole.name }}" 配置权限</h4>
      </div>
      
      <el-tree
        ref="permissionTreeRef"
        :data="permissionTree"
        :props="treeProps"
        node-key="id"
        show-checkbox
        :default-checked-keys="checkedPermissions"
        :default-expanded-keys="expandedKeys"
        @check="handlePermissionCheck"
      >
        <template #default="{ node, data }">
          <span class="permission-node">
            <el-icon v-if="data.icon">
              <component :is="data.icon" />
            </el-icon>
            <span>{{ data.name }}</span>
            <el-tag v-if="data.type" size="small" :type="getPermissionTypeTag(data.type)">
              {{ getPermissionTypeName(data.type) }}
            </el-tag>
          </span>
        </template>
      </el-tree>
      
      <template #footer>
        <el-button @click="handlePermissionDialogClose">取消</el-button>
        <el-button type="primary" :loading="permissionSubmitLoading" @click="handlePermissionSubmit">
          保存权限
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const permissionSubmitLoading = ref(false)
const dialogVisible = ref(false)
const permissionDialogVisible = ref(false)
const isEdit = ref(false)
const formRef = ref()
const permissionTreeRef = ref()
const roleList = ref([])
const currentRole = ref({})
const checkedPermissions = ref([])
const expandedKeys = ref([])

// 搜索表单
const searchForm = reactive({
  keyword: ''
})

// 角色表单
const roleForm = reactive({
  id: null,
  name: '',
  code: '',
  description: '',
  status: 1
})

// 表单验证规则
const roleRules = {
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  code: [
    { required: true, message: '请输入角色代码', trigger: 'blur' },
    { pattern: /^[A-Z_]+$/, message: '角色代码只能包含大写字母和下划线', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入角色描述', trigger: 'blur' }
  ]
}

// 权限树配置
const treeProps = {
  children: 'children',
  label: 'name'
}

// 权限树数据
const permissionTree = ref([
  {
    id: 1,
    name: '系统管理',
    type: 'menu',
    icon: 'Setting',
    children: [
      {
        id: 11,
        name: '用户管理',
        type: 'menu',
        children: [
          { id: 111, name: '查看用户', type: 'button' },
          { id: 112, name: '新增用户', type: 'button' },
          { id: 113, name: '编辑用户', type: 'button' },
          { id: 114, name: '删除用户', type: 'button' }
        ]
      },
      {
        id: 12,
        name: '角色管理',
        type: 'menu',
        children: [
          { id: 121, name: '查看角色', type: 'button' },
          { id: 122, name: '新增角色', type: 'button' },
          { id: 123, name: '编辑角色', type: 'button' },
          { id: 124, name: '删除角色', type: 'button' },
          { id: 125, name: '权限配置', type: 'button' }
        ]
      }
    ]
  },
  {
    id: 2,
    name: '老人档案管理',
    type: 'menu',
    icon: 'User',
    children: [
      { id: 21, name: '查看档案', type: 'button' },
      { id: 22, name: '新增档案', type: 'button' },
      { id: 23, name: '编辑档案', type: 'button' },
      { id: 24, name: '删除档案', type: 'button' }
    ]
  },
  {
    id: 3,
    name: '健康管理',
    type: 'menu',
    icon: 'Monitor',
    children: [
      {
        id: 31,
        name: '健康预警',
        type: 'menu',
        children: [
          { id: 311, name: '查看预警', type: 'button' },
          { id: 312, name: '处理预警', type: 'button' }
        ]
      },
      {
        id: 32,
        name: '健康记录',
        type: 'menu',
        children: [
          { id: 321, name: '查看记录', type: 'button' },
          { id: 322, name: '新增记录', type: 'button' },
          { id: 323, name: '编辑记录', type: 'button' }
        ]
      }
    ]
  }
])

/**
 * 页面加载时获取数据
 */
onMounted(() => {
  getRoleList()
})

/**
 * 获取角色列表
 */
const getRoleList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    // 模拟数据
    const mockData = [
      {
        id: 1,
        name: '超级管理员',
        code: 'SUPER_ADMIN',
        description: '系统超级管理员，拥有所有权限',
        status: 1,
        createTime: '2024-01-01 00:00:00'
      },
      {
        id: 2,
        name: '系统管理员',
        code: 'SYSTEM_ADMIN',
        description: '系统管理员，负责系统配置和用户管理',
        status: 1,
        createTime: '2024-01-01 00:00:00'
      },
      {
        id: 3,
        name: '医生',
        code: 'DOCTOR',
        description: '医生角色，负责老人健康管理',
        status: 1,
        createTime: '2024-01-01 00:00:00'
      },
      {
        id: 4,
        name: '护士',
        code: 'NURSE',
        description: '护士角色，协助医生进行护理工作',
        status: 1,
        createTime: '2024-01-01 00:00:00'
      }
    ]
    
    roleList.value = mockData
  } catch (error) {
    ElMessage.error('获取角色列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

/**
 * 搜索角色
 */
const handleSearch = () => {
  getRoleList()
}

/**
 * 重置搜索
 */
const resetSearch = () => {
  searchForm.keyword = ''
  handleSearch()
}

/**
 * 新增角色
 */
const handleAdd = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

/**
 * 编辑角色
 */
const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(roleForm, { ...row })
  dialogVisible.value = true
}

/**
 * 删除角色
 */
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该角色吗？', '删除确认', {
      type: 'warning'
    })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    ElMessage.success('删除成功')
    getRoleList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 切换角色状态
 */
const handleToggleStatus = async (row) => {
  try {
    const action = row.status === 1 ? '禁用' : '启用'
    await ElMessageBox.confirm(`确定要${action}该角色吗？`, `${action}确认`, {
      type: 'warning'
    })
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    row.status = row.status === 1 ? 0 : 1
    ElMessage.success(`${action}成功`)
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('操作失败')
    }
  }
}

/**
 * 配置权限
 */
const handleSetPermissions = (row) => {
  currentRole.value = { ...row }
  
  // 模拟已有权限
  checkedPermissions.value = [111, 112, 21, 22, 311, 321]
  expandedKeys.value = [1, 2, 3, 11, 31, 32]
  
  permissionDialogVisible.value = true
}

/**
 * 权限选择变化
 */
const handlePermissionCheck = (data, checked) => {
  console.log('权限变化:', data, checked)
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
    getRoleList()
  } catch (error) {
    ElMessage.error(isEdit.value ? '编辑失败' : '新增失败')
  } finally {
    submitLoading.value = false
  }
}

/**
 * 提交权限配置
 */
const handlePermissionSubmit = async () => {
  permissionSubmitLoading.value = true
  try {
    const checkedNodes = permissionTreeRef.value.getCheckedKeys()
    const halfCheckedNodes = permissionTreeRef.value.getHalfCheckedKeys()
    
    console.log('选中的权限:', checkedNodes)
    console.log('半选中的权限:', halfCheckedNodes)
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success('权限配置成功')
    permissionDialogVisible.value = false
  } catch (error) {
    ElMessage.error('权限配置失败')
  } finally {
    permissionSubmitLoading.value = false
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
 * 关闭权限弹窗
 */
const handlePermissionDialogClose = () => {
  permissionDialogVisible.value = false
  currentRole.value = {}
  checkedPermissions.value = []
}

/**
 * 重置表单
 */
const resetForm = () => {
  Object.assign(roleForm, {
    id: null,
    name: '',
    code: '',
    description: '',
    status: 1
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
    menu: '菜单',
    button: '按钮'
  }
  return typeMap[type] || '未知'
}
</script>

<style lang="scss" scoped>
.roles-container {
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 6px;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.table-container {
  margin-top: 20px;
}

.permission-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
  
  h4 {
    color: #303133;
    font-size: 16px;
    font-weight: 600;
    margin: 0;
  }
}

.permission-node {
  display: flex;
  align-items: center;
  gap: 8px;
  
  .el-icon {
    color: #409eff;
  }
  
  .el-tag {
    margin-left: auto;
  }
}

:deep(.el-tree) {
  .el-tree-node__content {
    height: 35px;
    line-height: 35px;
  }
  
  .el-tree-node__label {
    font-size: 14px;
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
</style>
