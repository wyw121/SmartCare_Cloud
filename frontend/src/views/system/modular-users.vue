<template>
  <div class="users-container">
    <PageHeader 
      title="用户管理"
      subtitle="管理系统用户信息、权限和状态"
    />

    <!-- 搜索区域 -->
    <UserSearchSection 
      :roles="roles"
      @search="handleSearch"
      @reset="handleReset"
    />

    <!-- 用户表格 -->
    <UserTableSection
      :user-list="userList"
      :loading="loading"
      :pagination="pageParams"
      @add="handleAddUser"
      @edit="handleEditUser"
      @delete="handleDeleteUser"
      @batch-delete="handleBatchDelete"
      @toggle-status="handleToggleStatus"
      @refresh="getUserList"
      @page-change="handlePageChange"
      @page-size-change="handlePageSizeChange"
    />

    <!-- 用户表单对话框 -->
    <UserFormDialog
      v-model:visible="dialogVisible"
      :is-edit="isEdit"
      :user-data="currentUser"
      :roles="roles"
      :departments="departments"
      @submit="handleSubmitUser"
    />
  </div>
</template>

<script setup>
import PageHeader from '@/components/common/PageHeader.vue'
import UserFormDialog from '@/components/system/UserFormDialog.vue'
import UserSearchSection from '@/components/system/UserSearchSection.vue'
import UserTableSection from '@/components/system/UserTableSection.vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { onMounted, reactive, ref } from 'vue'
import { getUserList as fetchUsers, createUser, updateUser, deleteUser, batchDeleteUsers, toggleUserStatus, getRoles, getDepartments } from '@/api/system'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const userList = ref([])
const currentUser = ref({})

// 分页参数
const pageParams = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 搜索参数
const searchParams = reactive({
  keyword: '',
  status: null,
  roleId: null
})

// 角色和部门数据
const roles = ref([
  { id: 1, name: '超级管理员' },
  { id: 2, name: '系统管理员' },
  { id: 3, name: '医生' },
  { id: 4, name: '护士' },
  { id: 5, name: '普通用户' }
])

const departments = ref([
  { id: 1, name: '管理部' },
  { id: 2, name: '医疗部' },
  { id: 3, name: '护理部' },
  { id: 4, name: '技术部' }
])

onMounted(async () => {
  await loadRoles()
  await loadDepartments()
  await getUserList()
})

/**
 * 加载角色列表
 */
const loadRoles = async () => {
  try {
    const response = await getRoles()
    if (response && response.data) {
      roles.value = response.data
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
  }
}

/**
 * 加载部门列表
 */
const loadDepartments = async () => {
  try {
    const response = await getDepartments()
    if (response && response.data) {
      departments.value = response.data
    }
  } catch (error) {
    console.error('获取部门列表失败:', error)
  }
}

/**
 * 获取用户列表
 */
const getUserList = async () => {
  loading.value = true
  try {
    const response = await fetchUsers({
      ...searchParams,
      current: pageParams.current,
      size: pageParams.size
    })
    
    if (response && response.data) {
      userList.value = response.data.records || response.data
      pageParams.total = response.data.total || response.data.length
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败')
    console.error(error)
    // 失败时使用空数据
    userList.value = []
    pageParams.total = 0
  } finally {
    loading.value = false
  }
}

/**
 * 搜索用户
 */
const handleSearch = (params) => {
  Object.assign(searchParams, params)
  pageParams.current = 1
  getUserList()
}

/**
 * 重置搜索
 */
const handleReset = () => {
  Object.assign(searchParams, {
    keyword: '',
    status: null,
    roleId: null
  })
  pageParams.current = 1
  getUserList()
}

/**
 * 新增用户
 */
const handleAddUser = () => {
  isEdit.value = false
  currentUser.value = {}
  dialogVisible.value = true
}

/**
 * 编辑用户
 */
const handleEditUser = (row) => {
  isEdit.value = true
  currentUser.value = { ...row }
  dialogVisible.value = true
}

/**
 * 删除用户
 */
const handleDeleteUser = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该用户吗？', '删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟删除API
    await new Promise(resolve => setTimeout(resolve, 500))
    ElMessage.success('删除成功')
    getUserList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

/**
 * 批量删除用户
 */
const handleBatchDelete = async (selectedUsers) => {
  if (!selectedUsers.length) {
    ElMessage.warning('请选择要删除的用户')
    return
  }
  
  try {
    await ElMessageBox.confirm(`确定要删除选中的 ${selectedUsers.length} 个用户吗？`, '批量删除确认', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟批量删除API
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('批量删除成功')
    getUserList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('批量删除失败')
    }
  }
}

/**
 * 切换用户状态
 */
const handleToggleStatus = async (row) => {
  try {
    // 模拟切换状态API
    await new Promise(resolve => setTimeout(resolve, 500))
    row.status = row.status === 1 ? 0 : 1
    ElMessage.success('状态更新成功')
  } catch (error) {
    ElMessage.error('状态更新失败')
  }
}

/**
 * 提交用户数据
 */
const handleSubmitUser = async (userData) => {
  try {
    // 模拟提交API
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    ElMessage.success(isEdit.value ? '更新成功' : '创建成功')
    dialogVisible.value = false
    getUserList()
  } catch (error) {
    ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
  }
}

/**
 * 分页变化
 */
const handlePageChange = (page) => {
  pageParams.current = page
  getUserList()
}

/**
 * 页面大小变化
 */
const handlePageSizeChange = (size) => {
  pageParams.size = size
  pageParams.current = 1
  getUserList()
}
</script>

<style scoped>
.users-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}
</style>
