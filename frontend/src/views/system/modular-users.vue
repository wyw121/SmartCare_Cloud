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

onMounted(() => {
  getUserList()
})

/**
 * 获取用户列表
 */
const getUserList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 500))
    
    const mockData = {
      records: [
        {
          id: 1,
          username: 'admin',
          realName: '系统管理员',
          email: 'admin@smartcare.com',
          phone: '13800138000',
          roleId: 1,
          roleName: '超级管理员',
          departmentId: 1,
          departmentName: '管理部',
          status: 1,
          createTime: '2024-01-01 09:00:00',
          remark: '系统默认管理员'
        },
        {
          id: 2,
          username: 'zhangsan',
          realName: '张三',
          email: 'zhang@smartcare.com',
          phone: '13800138001',
          roleId: 3,
          roleName: '医生',
          departmentId: 2,
          departmentName: '医疗部',
          status: 1,
          createTime: '2024-01-02 10:00:00',
          remark: '主治医师'
        }
      ],
      total: 2
    }
    
    userList.value = mockData.records
    pageParams.total = mockData.total
  } catch (error) {
    ElMessage.error('获取用户列表失败')
    console.error(error)
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
